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

package com.perl5.lang.perl.psi.stubs.imports.runtime;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlDoExpr;
import com.perl5.lang.perl.psi.impl.PsiPerlDoExprImpl;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class PerlDoExprElementType extends IStubElementType<PerlRuntimeImportStub, PerlDoExpr> implements PsiElementProvider {
  public PerlDoExprElementType(@NotNull String debugName) {
    super(debugName, PerlLanguage.INSTANCE);
  }

  @Override
  public PerlDoExpr createPsi(@NotNull PerlRuntimeImportStub stub) {
    return new PsiPerlDoExprImpl(stub, this);
  }

  @Override
  public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlDoExprImpl(node);
  }

  @Override
  public @NotNull PerlRuntimeImportStub createStub(@NotNull PerlDoExpr psi, StubElement parentStub) {
    return new PerlRuntimeImportStubImpl(
      parentStub,
      this,
      psi.getImportPath()
    );
  }

  @Override
  public @NotNull String getExternalId() {
    return "perl." + super.toString();
  }

  @Override
  public void serialize(@NotNull PerlRuntimeImportStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    String importPath = stub.getImportPath();
    if (importPath == null) {
      importPath = "";
    }

    dataStream.writeName(importPath);
  }

  @Override
  public @NotNull PerlRuntimeImportStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    String importPath = PerlStubSerializationUtil.readString(dataStream);
    if (StringUtil.isEmpty(importPath)) {
      importPath = null;
    }
    return new PerlRuntimeImportStubImpl(parentStub, this, importPath);
  }

  @Override
  public void indexStub(@NotNull PerlRuntimeImportStub stub, @NotNull IndexSink sink) {

  }
}
