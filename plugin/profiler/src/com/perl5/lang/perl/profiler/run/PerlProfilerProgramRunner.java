/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.profiler.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.runners.DefaultProgramRunnerKt;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.profiler.DefaultProfilerExecutorGroup;
import com.intellij.profiler.ProfilerToolWindowManager;
import com.perl5.lang.perl.profiler.configuration.PerlProfilerConfigurationState;
import com.perl5.lang.perl.idea.run.GenericPerlProgramRunner;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class PerlProfilerProgramRunner extends GenericPerlProgramRunner {
  private static final Logger LOG = Logger.getInstance(PerlProfilerProgramRunner.class);

  @Override
  public @NotNull String getRunnerId() {
    return "Perl Profiler";
  }

  @Override
  public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
    var executorSettings = getExecutorSettings(executorId);
    return executorSettings != null && executorSettings.canRun(profile);
  }

  private static @Nullable DefaultProfilerExecutorGroup.ProfilerExecutorSettings getExecutorSettings(@NotNull String executorId) {
    var executorGroup = DefaultProfilerExecutorGroup.Companion.getInstance();
    if (executorGroup == null) {
      return null;
    }
    return executorGroup.getRegisteredSettings(executorId);
  }

  @Override
  protected @Nullable PerlRunProfileState createState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment)
    throws ExecutionException {

    var executorSettings = getExecutorSettings(executionEnvironment.getExecutor().getId());
    if (executorSettings == null) {
      var message = "Unable to find executor settings";
      LOG.error(message);
      throw new ExecutionException(message);
    }
    var runProfile = executionEnvironment.getRunProfile();
    if (!executorSettings.canRun(runProfile)) {
      var message = "Unable to run " + runProfile + " with " + executorSettings;
      LOG.error(message);
      throw new ExecutionException(message);
    }
    var perlProfilerConfigurationState = executorSettings.getState();
    if (!(perlProfilerConfigurationState instanceof PerlProfilerConfigurationState)) {
      LOG.error("PerlProfilerConfigurationState expected, got: " + perlProfilerConfigurationState);
      throw new ExecutionException("Wrong profiler configuration state: " + perlProfilerConfigurationState);
    }

    return new PerlProfilerRunProfileState(executionEnvironment, (PerlProfilerConfigurationState)perlProfilerConfigurationState);
  }

  @Override
  public void execute(@NotNull ExecutionEnvironment environment) throws ExecutionException {

    ExecutionManager.getInstance(environment.getProject()).startRunProfile(
      environment, state -> {
        var descriptor = DefaultProgramRunnerKt.executeState(state, environment, this);
        if (descriptor == null) {
          return null;
        }
        if (!(state instanceof PerlProfilerRunProfileState)) {
          LOG.error("PerlProfilerRunProfileState expected, got " + state);
          throw new ExecutionException("Incorrect run configuration state, see logs for details");
        }
        var profilerProcess = new PerlProfilerProcess(environment, descriptor, ((PerlProfilerRunProfileState)state));
        ProfilerToolWindowManager.getInstance(environment.getProject()).addProfilerProcessTab(profilerProcess, false);
        return descriptor;
      });
  }
}
