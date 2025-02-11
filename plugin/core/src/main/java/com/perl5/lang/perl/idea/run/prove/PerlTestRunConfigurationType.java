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

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.run.PerlRunConfigurationFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PerlTestRunConfigurationType extends ConfigurationTypeBase {
  private final @NotNull PerlRunConfigurationFactory myTestConfigurationFactory;
  public PerlTestRunConfigurationType() {
    super("PerlTestRunConfiguration",
          PerlBundle.message("perl.run.prove.configuration.group.title"),
          PerlBundle.message("perl.run.prove.configuration.group.description"),
          PerlIcons.PERL_TEST_CONFIGURATION);
    myTestConfigurationFactory = new PerlRunConfigurationFactory(this) {
      @Override
      public @NotNull String getId() {
        return "Perl5 Test";
      }

      @Override
      public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new PerlTestRunConfiguration(project, this, "Unnamed");
      }
    };
    addFactory(myTestConfigurationFactory);
  }

  public @NotNull PerlRunConfigurationFactory getTestConfigurationFactory() {
    return myTestConfigurationFactory;
  }

  public static @NotNull PerlTestRunConfigurationType getInstance() {
    return Objects.requireNonNull(CONFIGURATION_TYPE_EP.findExtension(PerlTestRunConfigurationType.class));
  }
}
