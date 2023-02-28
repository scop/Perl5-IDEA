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

package base;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.perl5.lang.perl.idea.sdk.versionManager.asdf.AsdfTestUtil;
import org.jetbrains.annotations.NotNull;

public class AsdfLocalInterpreterConfigurator extends PerlInterpreterConfigurator {
  public static final PerlInterpreterConfigurator INSTANCE = new AsdfLocalInterpreterConfigurator();
  private static final String ASDF_HOME = "~/.asdf/bin/asdf";
  private static final String DISTRIBUTION_ID = PerlPlatformTestCase.PERL_TEST_VERSION;

  private AsdfLocalInterpreterConfigurator() {
  }

  @Override
  void setUpPerlInterpreter(@NotNull Project project) {
    addSdk(FileUtil.expandUserHome(ASDF_HOME), DISTRIBUTION_ID, AsdfTestUtil.getVersionManagerHandler(), project);
  }

  @Override
  public String toString() {
    return "asdf: " + DISTRIBUTION_ID;
  }
}
