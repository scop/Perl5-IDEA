// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;

public interface PsiPerlFuncDefinition extends PerlSubDefinitionElement, StubBasedPsiElement<PerlSubDefinitionStub> {

  @Nullable
  PsiPerlAttributes getAttributes();

  @NotNull
  PsiPerlBlock getBlock();

  @Nullable
  PsiPerlSignatureContent getSignatureContent();

}
