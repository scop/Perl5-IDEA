/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.idea.formatter;

import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.common.DefaultInjectedLanguageBlockBuilder;
import com.intellij.psi.formatter.common.InjectedLanguageBlockBuilder;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.formatter.PerlFormattingModelBuilder;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 08.03.2016.
 */
public class HTMLMasonFormattingModelBuilder extends PerlFormattingModelBuilder implements HTMLMasonElementTypes {
  private static final TokenSet SIMPLE_OPENERS = TokenSet.create(
    HTML_MASON_BLOCK_OPENER,
    HTML_MASON_CALL_OPENER,
    HTML_MASON_CALL_FILTERING_OPENER
  );

  @NotNull
  @Override
  public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
    CommonCodeStyleSettings commonSettings = settings.getCommonSettings(PerlLanguage.INSTANCE);
    PerlCodeStyleSettings perlSettings = settings.getCustomSettings(PerlCodeStyleSettings.class);
    SpacingBuilder spacingBuilder = createSpacingBuilder(commonSettings, perlSettings);
    InjectedLanguageBlockBuilder injectedLanguageBlockBuilder = new DefaultInjectedLanguageBlockBuilder(settings);
    PerlFormattingBlock block = new HTMLMasonFormattingBlock(element.getNode(), null, null, commonSettings, perlSettings, spacingBuilder,
                                                             injectedLanguageBlockBuilder);
    return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
  }

  @Override
  protected SpacingBuilder createSpacingBuilder(@NotNull CommonCodeStyleSettings settings, @NotNull PerlCodeStyleSettings perlSettings) {
    return super.createSpacingBuilder(settings, perlSettings)
      .after(SIMPLE_OPENERS).spaces(1)
      .before(HTML_MASON_BLOCK_CLOSER).spaces(1)
      .before(HTML_MASON_CALL_CLOSER).spaces(1)
      .before(HTML_MASON_CALL_CLOSER_UNMATCHED).spaces(1)
      .between(HTML_MASON_CALL_CLOSE_TAG_START, HTML_MASON_TAG_CLOSER).spaces(0)
      .after(HTML_MASON_CALL_CLOSE_TAG_START).spaces(1)
      .beforeInside(HTML_MASON_TAG_CLOSER, HTML_MASON_CALL_CLOSE_TAG).spaces(1)
      ;
  }
}
