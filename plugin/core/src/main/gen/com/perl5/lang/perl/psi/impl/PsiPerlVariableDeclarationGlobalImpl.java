// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import com.perl5.lang.perl.psi.mixins.PerlVariableDeclarationExprMixin;
import com.perl5.lang.perl.psi.*;

public class PsiPerlVariableDeclarationGlobalImpl extends PerlVariableDeclarationExprMixin implements PsiPerlVariableDeclarationGlobal {

  public PsiPerlVariableDeclarationGlobalImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiPerlVisitor visitor) {
    visitor.visitVariableDeclarationGlobal(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) accept((PsiPerlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiPerlAttributes getAttributes() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlAttributes.class);
  }

  @Override
  @NotNull
  public List<PsiPerlUndefExpr> getUndefExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlUndefExpr.class);
  }

  @Override
  @NotNull
  public List<PsiPerlVariableDeclarationElement> getVariableDeclarationElementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlVariableDeclarationElement.class);
  }

}
