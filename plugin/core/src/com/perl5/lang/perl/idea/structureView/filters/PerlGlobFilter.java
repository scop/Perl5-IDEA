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

package com.perl5.lang.perl.idea.structureView.filters;

import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.structureView.elements.PerlGlobStructureViewElement;
import com.perl5.lang.perl.idea.structureView.elements.PerlStructureViewElement;
import org.jetbrains.annotations.NotNull;


public class PerlGlobFilter extends PerlFilter {
  public static final PerlGlobFilter INSTANCE = new PerlGlobFilter();
  private static final String ID = "SHOW_GLOBS";

  @Override
  protected boolean isMyElement(@NotNull PerlStructureViewElement treeElement) {
    return treeElement instanceof PerlGlobStructureViewElement;
  }

  @Override
  public @NotNull ActionPresentation getPresentation() {
    return new ActionPresentationData("Show typeglobs", null, PerlIcons.GLOB_GUTTER_ICON);
  }

  @Override
  public @NotNull String getName() {
    return ID;
  }
}
