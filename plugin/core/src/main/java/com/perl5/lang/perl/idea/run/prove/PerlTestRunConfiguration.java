/*
 * Copyright 2015-2023 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.idea.run.prove;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil;
import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerConsoleView;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.annotations.Tag;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.execution.PerlTerminalExecutionConsole;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.CONSOLE;
import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.NONE;
import static com.perl5.lang.perl.util.PerlRunUtil.PERL5OPT;
import static com.perl5.lang.perl.util.PerlRunUtil.getPerlRunIncludeArguments;

@VisibleForTesting
public class PerlTestRunConfiguration extends GenericPerlRunConfiguration {
  private static final String PROVE = "prove";
  private static final String TEST_HARNESS = "Test::Harness";
  static final int DEFAULT_JOBS_NUMBER = 1;
  private static final String PROVE_FORMAT_PARAMETER = "--formatter";
  private static final String PROVE_FRAMEWORK_NAME = TEST_HARNESS;
  private static final Pattern MISSING_FILTER_PATTERN = Pattern.compile("Can't load module (\\S+) at .+?/prove line");
  private static final String PROVE_PLUGIN_NAMESPACE = "App::Prove::Plugin";
  private static final List<String> PROVE_DEFAULT_PARAMETERS = Arrays.asList("--merge", "--recurse");
  private static final String PROVE_JOBS_SHORT_PREFIX = "-j";
  private static final String PROVE_JOBS_PARAMETER = "--jobs";
  private static final String HARNESS_OPTIONS = "HARNESS_OPTIONS";
  private static final String HARNESS_OPTIONS_SEPARATOR = ":";
  private static final String HARNESS_OPTIONS_JOBS = "j";
  private static final String HARNESS_OPTIONS_FORMATTER = "f";
  private static final String HARNESS_OPTIONS_FORMATTER_CLASS = "TAP-Formatter-Camelcade";
  private static final String HARNESS_PERL_SWITCHES = "HARNESS_PERL_SWITCHES";
  private static final Logger LOG = Logger.getInstance(PerlTestRunConfiguration.class);
  @Tag("JOBS_NUMBER")
  private int myJobsNumber = DEFAULT_JOBS_NUMBER;
  @Tag("TEST_SCRIPT_PARAMETERS")
  private String myTestScriptParameters = "";

  public PerlTestRunConfiguration(Project project,
                                  @NotNull ConfigurationFactory factory,
                                  String name) {
    super(project, factory, name);
  }

  int getJobsNumber() {
    return myJobsNumber;
  }

  @VisibleForTesting
  public void setJobsNumber(int jobsNumber) {
    myJobsNumber = jobsNumber;
  }

  @Nullable
  String getTestScriptParameters() {
    return myTestScriptParameters;
  }

  void setTestScriptParameters(@Nullable String testScriptParameters) {
    myTestScriptParameters = testScriptParameters;
  }

  @NotNull
  List<String> getTestScriptParametersList() {
    String testScriptParameters = getTestScriptParameters();
    return StringUtil.isEmpty(testScriptParameters) ? Collections.emptyList() : StringUtil.split(testScriptParameters, " ");
  }

  @Override
  public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new PerlTestRunConfigurationEditor(getProject());
  }

  @Override
  public String suggestedName() {
    List<VirtualFile> testsVirtualFiles = computeTargetFiles();
    if (testsVirtualFiles.size() > 1) {
      VirtualFile firstTest = testsVirtualFiles.remove(0);
      return PerlBundle.message("perl.run.prove.configuration.name.multi", firstTest.getName(), testsVirtualFiles.size());
    }
    else if (testsVirtualFiles.size() == 1) {
      return PerlBundle.message("perl.run.prove.configuration.name.single", testsVirtualFiles.get(0).getName());
    }
    return super.suggestedName();
  }

  @Override
  protected @NotNull PerlCommandLine createBaseCommandLine(@NotNull PerlRunProfileState perlRunProfileState) throws ExecutionException {
    ExecutionEnvironment executionEnvironment = perlRunProfileState.getEnvironment();
    Project project = executionEnvironment.getProject();
    List<String> additionalPerlParameters = perlRunProfileState.getAdditionalPerlParameters(this);
    Map<String, String> additionalEnvironmentVariables = perlRunProfileState.getAdditionalEnvironmentVariables();

    Sdk perlSdk = getEffectiveSdk();
    VirtualFile proveScript = PerlRunUtil.findLibraryScriptWithNotification(perlSdk, getProject(), PROVE, TEST_HARNESS);
    if (proveScript == null) {
      throw new ExecutionException(PerlBundle.message("perl.run.error.prove.missing", perlSdk.getName()));
    }

    PerlHostData<?, ?> perlHostData = PerlHostData.notNullFrom(perlSdk);

    Set<String> proveParameters = new LinkedHashSet<>(PROVE_DEFAULT_PARAMETERS);
    proveParameters.addAll(getScriptParameters());

    VirtualFile workingDirectory = computeExplicitWorkingDirectory();

    List<String> testsPaths = new ArrayList<>();
    for (VirtualFile testVirtualFile : computeTargetFiles()) {
      if (testVirtualFile == null) {
        continue;
      }
      String virtualFilePath = testVirtualFile.getPath();
      if (workingDirectory != null && VfsUtil.isAncestor(workingDirectory, testVirtualFile, true)) {
        testsPaths.add(VfsUtil.getRelativePath(testVirtualFile, workingDirectory));
      }
      else {
        testsPaths.add(perlHostData.getRemotePath(virtualFilePath));
      }
    }

    String remotePath = perlHostData.getRemotePath(proveScript.getPath());
    if (StringUtil.isEmpty(remotePath)) {
      throw new ExecutionException(PerlBundle.message("dialog.message.unable.to.map.remote.path.for", remotePath, perlHostData));
    }

    var perlRunIncludeArguments = getPerlRunIncludeArguments(perlHostData, project);

    PerlCommandLine commandLine = new PerlCommandLine(getEffectiveInterpreterPath())
      .withParameters(perlRunIncludeArguments)
      .withParameters(remotePath)
      .withParameters(proveParameters)
      .withParameters(testsPaths)
      .withProject(project)
      .withSdk(perlSdk);

    List<String> testScriptParametersList = getTestScriptParametersList();
    if (!testScriptParametersList.isEmpty()) {
      commandLine.withParameters("::");
      commandLine.withParameters(testScriptParametersList);
    }

    List<String> perlParametersList = new ArrayList<>(getPerlParametersList());
    perlParametersList.addAll(additionalPerlParameters);
    perlParametersList.addAll(perlRunIncludeArguments);

    // environment
    Map<String, String> environment = new HashMap<>(getEnvs());
    environment.putAll(additionalEnvironmentVariables);
    PerlRunUtil.updatePerl5Opt(environment, perlParametersList);

    List<String> harnessOptions = new ArrayList<>();
    harnessOptions.add(HARNESS_OPTIONS_JOBS + Integer.toString(perlRunProfileState.isParallelRunAllowed() ? getJobsNumber() : 1));
    harnessOptions.add(HARNESS_OPTIONS_FORMATTER + HARNESS_OPTIONS_FORMATTER_CLASS);

    environment.put(HARNESS_OPTIONS, String.join(HARNESS_OPTIONS_SEPARATOR, harnessOptions));
    var perl5Opt = environment.remove(PERL5OPT);
    if (StringUtil.isNotEmpty(perl5Opt)) {
      environment.put(HARNESS_PERL_SWITCHES, perl5Opt);
    }

    commandLine.withEnvironment(environment);
    commandLine.withParentEnvironmentType(isPassParentEnvs() ? CONSOLE : NONE);
    return commandLine;
  }

  @Override
  protected @NotNull List<String> getScriptParameters() {

    List<String> userParameters = super.getScriptParameters();
    for (Iterator<String> iterator = userParameters.iterator(); iterator.hasNext(); ) {
      String userParameter = iterator.next();
      if (StringUtil.startsWith(userParameter, PROVE_JOBS_SHORT_PREFIX)) {
        iterator.remove();
      }
      else if (PROVE_FORMAT_PARAMETER.equals(userParameter) || PROVE_JOBS_PARAMETER.equals(userParameter)) {
        iterator.remove();
        if (iterator.hasNext()) {
          iterator.next();
          iterator.remove();
        }
      }
    }
    return userParameters;
  }

  @Override
  public boolean isReconnect() {
    return true;
  }

  @Override
  public @NotNull ConsoleView createConsole(@NotNull PerlRunProfileState runProfileState) throws ExecutionException {
    PerlSMTRunnerConsoleProperties consoleProperties =
      new PerlSMTRunnerConsoleProperties(this, PROVE_FRAMEWORK_NAME, runProfileState.getEnvironment().getExecutor());
    String splitterPropertyName = SMTestRunnerConnectionUtil.getSplitterPropertyName(PROVE_FRAMEWORK_NAME);
    var project = getProject();
    SMTRunnerConsoleView consoleView = new PerlSMTRunnerConsoleView(consoleProperties, splitterPropertyName)
      .withHostData(PerlHostData.notNullFrom(getEffectiveSdk()));
    SMTestRunnerConnectionUtil.initConsoleView(consoleView, PROVE_FRAMEWORK_NAME);
    PerlTerminalExecutionConsole.updatePredefinedFiltersLater(project, consoleView);
    return consoleView;
  }

  @Override
  protected boolean isUsePty() {
    return false;
  }

  @Override
  protected @NotNull ProcessHandler doPatchProcessHandler(@NotNull ProcessHandler processHandler) {
    try {
      Sdk effectiveSdk = getEffectiveSdk();
      processHandler.addProcessListener(new ProcessAdapter() {
        @Override
        public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
          String inputText = event.getText();
          Matcher matcher = MISSING_FILTER_PATTERN.matcher(inputText);
          if (matcher.find()) {
            String libraryName = PROVE_PLUGIN_NAMESPACE + PerlPackageUtil.NAMESPACE_SEPARATOR + matcher.group(1);
            PerlRunUtil.showMissingLibraryNotification(getProject(), effectiveSdk, Collections.singletonList(libraryName));
          }
        }
      });
    }
    catch (ExecutionException e) {
      LOG.warn("Missing effective sdk for test configuration: " + getName());
    }
    return processHandler;
  }

  @Override
  protected void checkConfigurationScriptPath() throws RuntimeConfigurationException {
    if (StringUtil.isEmptyOrSpaces(getScriptPath())) {
      throw new RuntimeConfigurationException(PerlBundle.message("perl.run.error.no.test.set"));
    }
    if (computeTargetFiles().isEmpty()) {
      throw new RuntimeConfigurationException(PerlBundle.message("perl.run.error.no.tests.found"));
    }
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException {
    super.checkConfiguration();
    if (PerlRunUtil.findScript(getProject(), PROVE) == null) {
      throw new RuntimeConfigurationException(PerlBundle.message("perl.run.error.no.prove.found"));
    }
  }
}
