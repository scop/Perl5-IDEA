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

package com.perl5.lang.mason.idea.folding;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mason.elementType.MasonElementTypes;
import com.perl5.lang.perl.idea.folding.PerlFoldingBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 07.01.2016.
 */
public class MasonFoldingBuilder extends PerlFoldingBuilder implements MasonElementTypes
{
	protected static final TokenSet COMMENT_EXCLUDED_TOKENS = TokenSet.orSet(
			PerlFoldingBuilder.COMMENT_EXCLUDED_TOKENS,
			TokenSet.create(
					MASON_BLOCK_OPENER,
					MASON_BLOCK_CLOSER,
					MASON_LINE_OPENER
			));

	@NotNull
	@Override
	protected TokenSet getCommentExcludedTokens()
	{
		return COMMENT_EXCLUDED_TOKENS;
	}

	@Nullable
	@Override
	protected IElementType getTemplateBlockElementType()
	{
		return MASON_TEMPLATE_BLOCK_HTML;
	}
}
