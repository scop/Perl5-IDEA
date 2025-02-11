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

package com.perl5.lang.perl.idea.configuration.module;

import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.platform.GeneratorPeerImpl;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkConfigurable;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class PerlProjectGeneratorPeerBase<Settings extends PerlProjectGenerationSettings> extends GeneratorPeerImpl<Settings>
  implements UnnamedConfigurable {
  private final @NotNull Perl5SdkConfigurable mySdkConfigurable;
  private final AtomicNotNullLazyValue<JComponent> myComponentProvider = AtomicNotNullLazyValue.createValue(
    () -> initializeComponent(super.getComponent()));

  public PerlProjectGeneratorPeerBase(@NotNull Settings settings) {
    super(settings, new JPanel(new VerticalFlowLayout()));
    mySdkConfigurable = new Perl5SdkConfigurable(getSettings(), null);
  }

  @Override
  public final @NotNull JComponent getComponent() {
    return myComponentProvider.getValue();
  }

  @Override
  public void disposeUIResources() {
    mySdkConfigurable.disposeUIResources();
  }

  protected @NotNull JComponent initializeComponent(@NotNull JComponent component) {
    component.add(mySdkConfigurable.createComponent());
    mySdkConfigurable.setEnabled(PerlProjectManager.getSdk(getSettings().getProject()) == null);
    return component;
  }

  @Override
  public final @NotNull JComponent createComponent() {
    return getComponent();
  }

  @Override
  public boolean isModified() {
    throw new RuntimeException("NYI");
  }

  @Override
  public void apply() {
    mySdkConfigurable.apply();
  }

  @Override
  public void reset() {
    mySdkConfigurable.reset();
  }
}
