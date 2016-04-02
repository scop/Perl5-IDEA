/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser;

import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;

/**
 * Created by hurricup on 28.12.2015.
 */
public interface PerlParser extends PerlElementTypes
{
	// these tokens are not being marked as bad characters
	TokenSet BAD_CHARACTER_FORBIDDEN_TOKENS = TokenSet.create(
			RESERVED_PACKAGE,
			RIGHT_BRACE,
			REGEX_QUOTE_CLOSE,
			SEMICOLON
	);

	// this is my attempt to make recovery using consumable tokenset. The point is that there is no need to modify it
	TokenSet STATEMENT_RECOVERY_CONSUMABLE_TOKENS = TokenSet.create(
			ANNOTATION_ABSTRACT_KEY,
			ANNOTATION_DEPRECATED_KEY,
			ANNOTATION_METHOD_KEY,
			ANNOTATION_OVERRIDE_KEY,
			ANNOTATION_RETURNS_KEY,
			ANNOTATION_UNKNOWN_KEY,
			COLON,
			COMMENT_ANNOTATION,
			COMMENT_BLOCK,
			COMMENT_LINE,
			FORMAT,
			FORMAT_TERMINATOR,
			HANDLE,
			HEREDOC,
			HEREDOC_END,
			HEREDOC_QQ,
			HEREDOC_QX,
			LABEL,
			OPERATOR_AND,
			OPERATOR_AND_ASSIGN,
			OPERATOR_ASSIGN,
			OPERATOR_BITWISE_AND_ASSIGN,
			OPERATOR_BITWISE_OR,
			OPERATOR_BITWISE_OR_ASSIGN,
			OPERATOR_BITWISE_XOR,
			OPERATOR_BITWISE_XOR_ASSIGN,
			OPERATOR_CMP_NUMERIC,
			OPERATOR_COMMA,
			OPERATOR_COMMA_ARROW,
			OPERATOR_CONCAT,
			OPERATOR_CONCAT_ASSIGN,
			OPERATOR_DEREFERENCE,
			OPERATOR_DIV,
			OPERATOR_DIV_ASSIGN,
			OPERATOR_EQ_NUMERIC,
			OPERATOR_FLIP_FLOP,
			OPERATOR_GE_NUMERIC,
			OPERATOR_GT_NUMERIC,
			OPERATOR_LE_NUMERIC,
			OPERATOR_LT_NUMERIC,
			OPERATOR_MINUS_ASSIGN,
			OPERATOR_MINUS_UNARY,
			OPERATOR_MOD_ASSIGN,
			OPERATOR_MUL_ASSIGN,
			OPERATOR_NE_NUMERIC,
			OPERATOR_NOT_RE,
			OPERATOR_OR,
			OPERATOR_OR_ASSIGN,
			OPERATOR_OR_DEFINED,
			OPERATOR_OR_DEFINED_ASSIGN,
			OPERATOR_PLUS_ASSIGN,
			OPERATOR_PLUS_UNARY,
			OPERATOR_POW,
			OPERATOR_POW_ASSIGN,
			OPERATOR_RE,
			OPERATOR_SHIFT_LEFT,
			OPERATOR_SHIFT_LEFT_ASSIGN,
			OPERATOR_SHIFT_RIGHT,
			OPERATOR_SHIFT_RIGHT_ASSIGN,
			OPERATOR_SMARTMATCH,
			OPERATOR_X_ASSIGN,
			PARSABLE_STRING_USE_VARS,
			QUESTION,
			QUOTE_DOUBLE,
			QUOTE_DOUBLE_CLOSE,
			QUOTE_SINGLE,
			QUOTE_SINGLE_CLOSE,
			QUOTE_TICK,
			QUOTE_TICK_CLOSE,
			REGEX_MODIFIER,
			REGEX_QUOTE,
			REGEX_QUOTE_E,
			REGEX_QUOTE_OPEN_E,
			REGEX_TOKEN,
			RIGHT_ANGLE,
			RIGHT_BRACKET,
			RIGHT_PAREN,
			STRING_CONTENT,
			STRING_PACKAGE,
			STRING_PLUS,
			STRING_WHITESPACE,
			SUB,
			SUB_PROTOTYPE_TOKEN,
			VARIABLE_NAME,
			VERSION_ELEMENT
	);

	// stop tokens for statement recovery
	TokenSet STATEMENT_RECOVERY_TOKENS = TokenSet.create(
			SEMICOLON,

			POD,

			LEFT_ANGLE,
			LEFT_PAREN,
			LEFT_BRACKET,
			LEFT_BRACE,
			RIGHT_BRACE,
			REGEX_QUOTE_CLOSE,

			REGEX_QUOTE_OPEN,
			REGEX_QUOTE_OPEN_X,
			QUOTE_DOUBLE_OPEN,
			QUOTE_SINGLE_OPEN,
			QUOTE_TICK_OPEN,

			TAG,
			TAG_END,
			TAG_DATA,

			BLOCK_NAME,

			RESERVED_IF,
			RESERVED_UNLESS,
			RESERVED_ELSIF,
			RESERVED_ELSE,
			RESERVED_RETURN,
			RESERVED_GIVEN,
			RESERVED_WHEN,
			RESERVED_DEFAULT,
			RESERVED_WHILE,
			RESERVED_UNTIL,
			RESERVED_FOR,
			RESERVED_FOREACH,
			RESERVED_CONTINUE,

			RESERVED_FORMAT,
			RESERVED_SUB,
			RESERVED_PACKAGE,
			RESERVED_USE,
			RESERVED_NO,
			RESERVED_REQUIRE,
			RESERVED_EVAL,
			RESERVED_DO,

			STRING_IDENTIFIER, // this is actually from hash, not block

			RESERVED_UNDEF,

			RESERVED_PRINT,
			RESERVED_PRINTF,
			RESERVED_SAY,

			RESERVED_GREP,
			RESERVED_MAP,
			RESERVED_SORT,

			RESERVED_QW,

			RESERVED_QQ,
			RESERVED_Q,
			RESERVED_QX,

			RESERVED_TR,
			RESERVED_Y,

			RESERVED_S,
			RESERVED_QR,
			RESERVED_M,

			RESERVED_NEXT,
			RESERVED_LAST,
			RESERVED_GOTO,
			RESERVED_CONTINUE,
			RESERVED_REDO,

			OPERATOR_PLUS,
			OPERATOR_HEREDOC,
			OPERATOR_PLUS_PLUS,
			OPERATOR_MINUS,
			OPERATOR_MINUS_MINUS,
			OPERATOR_FILETEST,
			OPERATOR_REFERENCE,
			OPERATOR_HELLIP,    // nyi statement

			// these may be labels
			OPERATOR_AND_LP,
			OPERATOR_OR_LP,
			OPERATOR_XOR_LP,
			OPERATOR_X,
			OPERATOR_EQ_STR,
			OPERATOR_NE_STR,
			OPERATOR_GT_STR,
			OPERATOR_LT_STR,
			OPERATOR_GE_STR,
			OPERATOR_LE_STR,
			OPERATOR_CMP_STR,
			OPERATOR_BITWISE_NOT,

			OPERATOR_NOT,
			OPERATOR_NOT_LP,

			IDENTIFIER,
			PACKAGE,
			PACKAGE_CORE_IDENTIFIER,
			PACKAGE_IDENTIFIER,
			PACKAGE_PRAGMA_CONSTANT,
			PACKAGE_PRAGMA_VARS,

			SIGIL_SCALAR,
			SIGIL_ARRAY,
			SIGIL_SCALAR_INDEX,

			SIGIL_HASH,
			OPERATOR_MOD,

			SIGIL_GLOB,
			OPERATOR_MUL,

			SIGIL_CODE,
			OPERATOR_BITWISE_AND,

			NUMBER,
			NUMBER_CONSTANT,
			NUMBER_SIMPLE,
			NUMBER_VERSION,

			RESERVED_MY,
			RESERVED_OUR,
			RESERVED_STATE,
			RESERVED_LOCAL,

			ANNOTATION_PREFIX
	);


	// Tokens which consumed and counted as semicolon
	TokenSet CONSUMABLE_SEMI_TOKENS = TokenSet.create(
			SEMICOLON
	);

	// Tokens which makes semicolon optional, like block close brace
	TokenSet UNCONSUMABLE_SEMI_TOKENS = TokenSet.create(
			RIGHT_BRACE,
			REGEX_QUOTE_CLOSE,
			TAG_END,
			TAG_DATA
	);

	TokenSet ANON_HASH_TOKEN_SUFFIXES = TokenSet.create(
			RIGHT_BRACE
			, RIGHT_PAREN
			, RIGHT_BRACKET
			, SEMICOLON
			, COLON

			, OPERATOR_HELLIP,
			OPERATOR_FLIP_FLOP,
			OPERATOR_CONCAT,

			OPERATOR_AND,
			OPERATOR_OR,
			OPERATOR_OR_DEFINED,
			OPERATOR_NOT,

			COLON,

			OPERATOR_AND_LP,
			OPERATOR_OR_LP,
//			OPERATOR_XOR_LP,
			OPERATOR_NOT_LP,

			OPERATOR_COMMA,
			OPERATOR_COMMA_ARROW,

			OPERATOR_DEREFERENCE
	);

}
