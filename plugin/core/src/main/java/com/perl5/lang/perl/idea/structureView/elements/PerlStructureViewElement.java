/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.structureView.elements;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationBase;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.parser.constant.psi.light.PerlLightConstantDefinitionElement;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.psi.properties.PerlIdentifierOwner;
import com.perl5.lang.perl.util.*;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.idea.structureView.PodStructureViewElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public abstract class PerlStructureViewElement extends PsiTreeElementBase<PsiElement> implements SortableTreeElement {
  protected boolean myIsInherited;
  private @Nullable PerlExportDescriptor myExportDescriptor;

  public PerlStructureViewElement(PsiElement psiElement) {
    super(psiElement);
  }

  public PerlStructureViewElement setInherited() {
    this.myIsInherited = true;
    return this;
  }

  public boolean isInherited() {
    return myIsInherited;
  }

  public boolean isImported() {
    return myExportDescriptor != null;
  }

  public PerlStructureViewElement setImported(@NotNull PerlExportDescriptor exportDescriptor) {
    myExportDescriptor = exportDescriptor;
    return this;
  }

  public @Nullable PerlExportDescriptor getExportDescriptor() {
    return myExportDescriptor;
  }

  @Override
  public @NotNull String getAlphaSortKey() {
    PsiElement element = getElement();
    if (!(element instanceof PsiNamedElement)) {
      return "";
    }
    PerlExportDescriptor exportDescriptor = getExportDescriptor();
    if (exportDescriptor != null) {
      return exportDescriptor.getImportedName();
    }
    String name = ((PsiNamedElement)element).getName();
    if (name == null) {
      name = "Empty named " + element;
    }
    return name;
  }

  @Override
  public @Nullable String getPresentableText() {
    throw new RuntimeException("Should not be invoked or should be overrode");
  }

  @Override
  public @NotNull ItemPresentation getPresentation() {

    ItemPresentation itemPresentation = createPresentation();

    if ((isInherited() || isImported()) && itemPresentation instanceof PerlItemPresentationBase) {
      if (getValue() instanceof PerlDeprecatable && ((PerlDeprecatable)getValue()).isDeprecated()) {
        ((PerlItemPresentationBase)itemPresentation).setAttributesKey(PerlSyntaxHighlighter.UNUSED_DEPRECATED);
      }
      else {
        ((PerlItemPresentationBase)itemPresentation).setAttributesKey(CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES);
      }
    }

    if (isImported() && itemPresentation instanceof PerlItemPresentationSimple) {
      PerlExportDescriptor exportDescriptor = getExportDescriptor();
      assert exportDescriptor != null;
      ((PerlItemPresentationSimple)itemPresentation).setPresentableText(exportDescriptor.getImportedName());
    }

    return itemPresentation;
  }

  protected @NotNull ItemPresentation createPresentation() {
    ItemPresentation result = null;
    PsiElement element = getElement();
    if (element instanceof NavigationItem) {
      result = ((NavigationItem)element).getPresentation();
    }

    return result == null ? new PerlItemPresentationSimple(element, "FIXME") : result;
  }

  @Override
  public @NotNull Collection<StructureViewTreeElement> getChildrenBase() {

    PsiElement psiElement = getElement();
    if (psiElement == null) {
      return Collections.emptyList();
    }

    List<StructureViewTreeElement> result = new ArrayList<>();

    Set<String> implementedMethods = new HashSet<>();

    if (psiElement instanceof PerlFile) {
      FileViewProvider viewProvider = ((PerlFile)psiElement).getViewProvider();
      PsiFile podFile = viewProvider.getPsi(PodLanguage.INSTANCE);
      if (podFile != null && podFile.getChildren().length > 1) {
        result.add(new PodStructureViewElement(podFile));
      }

      Language targetLanguage = null;
      for (Language language : viewProvider.getLanguages()) {
        if (language == PerlLanguage.INSTANCE) {
          targetLanguage = language;
          break;
        }
        else if (targetLanguage == null && language.isKindOf(PerlLanguage.INSTANCE)) {
          targetLanguage = language;
        }
      }

      if (targetLanguage != null) {
        viewProvider.getPsi(targetLanguage).accept(new PerlRecursiveVisitor() {
          @Override
          public void visitNamespaceDefinitionElement(@NotNull PerlNamespaceDefinitionElement o) {
            result.add(new PerlNamespaceStructureViewElement(o));
            super.visitNamespaceDefinitionElement(o);
          }

          @Override
          protected boolean shouldVisitLightElements() {
            return true;
          }
        });
      }
    }
    if (psiElement instanceof PerlNamespaceDefinitionElement) {
      // global variables
      for (PerlVariableDeclarationElement child : PsiTreeUtil.findChildrenOfType(psiElement, PerlVariableDeclarationElement.class)) {
        if (child.isGlobalDeclaration() && psiElement.isEquivalentTo(PerlPackageUtil.getNamespaceContainerForElement(child))) {
          result.add(new PerlVariableDeclarationStructureViewElement(child));
        }
      }

      Project project = psiElement.getProject();
      GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);

      // imported scalars
      for (PerlExportDescriptor exportDescritptor : ((PerlNamespaceDefinitionElement)psiElement).getImportedScalarDescriptors()) {
        String canonicalName = exportDescritptor.getTargetCanonicalName();

        Collection<PerlVariableDeclarationElement> variables = PerlScalarUtil.getGlobalScalarDefinitions(project, canonicalName);

        for (PerlVariableDeclarationElement variable : variables) {
          result.add(new PerlVariableDeclarationStructureViewElement(variable).setImported(exportDescritptor));
        }

        // globs
        Collection<PsiPerlGlobVariable> items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName, projectScope);
        if (items.isEmpty()) {
          items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName);
        }

        for (PerlGlobVariableElement item : items) {
          result.add(new PerlGlobStructureViewElement(item).setImported(exportDescritptor));
        }
      }

      // imported arrays
      for (PerlExportDescriptor exportDescritptor : ((PerlNamespaceDefinitionElement)psiElement).getImportedArrayDescriptors()) {
        String canonicalName = exportDescritptor.getTargetCanonicalName();

        Collection<PerlVariableDeclarationElement> variables = PerlArrayUtil.getGlobalArrayDefinitions(project, canonicalName);

        for (PerlVariableDeclarationElement variable : variables) {
          result.add(new PerlVariableDeclarationStructureViewElement(variable).setImported(exportDescritptor));
        }

        // globs
        Collection<PsiPerlGlobVariable> items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName, projectScope);
        if (items.isEmpty()) {
          items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName);
        }

        for (PerlGlobVariableElement item : items) {
          result.add(new PerlGlobStructureViewElement(item).setImported(exportDescritptor));
        }
      }

      // imported hashes
      for (PerlExportDescriptor exportDescritptor : ((PerlNamespaceDefinitionElement)psiElement).getImportedHashDescriptors()) {
        String canonicalName = exportDescritptor.getTargetCanonicalName();

        Collection<PerlVariableDeclarationElement> variables = PerlHashUtil.getGlobalHashDefinitions(project, canonicalName);

        for (PerlVariableDeclarationElement variable : variables) {
          result.add(new PerlVariableDeclarationStructureViewElement(variable).setImported(exportDescritptor));
        }

        // globs
        Collection<PsiPerlGlobVariable> items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName, projectScope);
        if (items.isEmpty()) {
          items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName);
        }

        for (PerlGlobVariableElement item : items) {
          result.add(new PerlGlobStructureViewElement(item).setImported(exportDescritptor));
        }
      }

      // Imported subs
      for (PerlExportDescriptor exportDescritptor : ((PerlNamespaceDefinitionElement)psiElement).getImportedSubsDescriptors()) {
        String canonicalName = exportDescritptor.getTargetCanonicalName();

        // declarations
        Collection<PerlSubDeclarationElement> subDeclarations = PerlSubUtil.getSubDeclarations(project, canonicalName, projectScope);
        if (subDeclarations.isEmpty()) {
          subDeclarations = PerlSubUtil.getSubDeclarations(project, canonicalName);
        }

        for (PerlSubDeclarationElement item : subDeclarations) {
          result.add(new PerlSubStructureViewElement(item).setImported(exportDescritptor));
        }

        // definitions
        Collection<PerlSubDefinitionElement> subDefinitions = PerlSubUtil.getSubDefinitions(project, canonicalName, projectScope);
        if (subDefinitions.isEmpty()) {
          subDefinitions = PerlSubUtil.getSubDefinitions(project, canonicalName);
        }

        for (PerlSubDefinitionElement item : subDefinitions) {
          result.add(new PerlSubStructureViewElement(item).setImported(exportDescritptor));
        }

        // globs
        Collection<PsiPerlGlobVariable> items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName, projectScope);
        if (items.isEmpty()) {
          items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName);
        }

        for (PerlGlobVariableElement item : items) {
          result.add(new PerlGlobStructureViewElement(item).setImported(exportDescritptor));
        }
      }

      psiElement.accept(new PerlRecursiveVisitor() {
        @Override
        public void visitPerlSubDefinitionElement(@NotNull PerlSubDefinitionElement child) {
          if (psiElement.isEquivalentTo(PerlPackageUtil.getNamespaceContainerForElement(child))) {
            implementedMethods.add(child.getName());

            result.add(new PerlSubStructureViewElement(child));
          }
          super.visitPerlSubDefinitionElement(child);
        }

        @Override
        public void visitSubDeclarationElement(@NotNull PerlSubDeclarationElement child) {
          if (psiElement.isEquivalentTo(PerlPackageUtil.getNamespaceContainerForElement(child))) {
            result.add(new PerlSubStructureViewElement(child));
          }
          super.visitSubDeclarationElement(child);
        }

        @Override
        protected boolean shouldVisitLightElements() {
          return true;
        }

        @Override
        public void visitGlobVariable(@NotNull PsiPerlGlobVariable child) {
          if (child.isLeftSideOfAssignment() && psiElement.isEquivalentTo(PerlPackageUtil.getNamespaceContainerForElement(child))) {
            implementedMethods.add(child.getName());
            result.add(new PerlGlobStructureViewElement(child));
          }
          super.visitGlobVariable(child);
        }
      });
    }

    // inherited elements
    if (psiElement instanceof PerlNamespaceDefinitionWithIdentifier) {
      List<StructureViewTreeElement> inheritedResult = new ArrayList<>();

      String packageName = ((PerlNamespaceDefinitionElement)psiElement).getNamespaceName();

      if (packageName != null) {
        for (PsiElement element : PerlMro.getVariants(psiElement, packageName, true)) {
          if (element instanceof PerlIdentifierOwner identifierOwner && !implementedMethods.contains((identifierOwner).getName())) {
            if (element instanceof PerlLightConstantDefinitionElement perlLightConstantDefinitionElement) {
              inheritedResult.add(new PerlSubStructureViewElement(perlLightConstantDefinitionElement).setInherited());
            }
            else if (element instanceof PerlSubDefinitionElement subDefinitionElement) {
              inheritedResult.add(new PerlSubStructureViewElement(subDefinitionElement).setInherited());
            }
            else if (element instanceof PerlSubDeclarationElement subDeclarationElement) {
              inheritedResult.add(new PerlSubStructureViewElement(subDeclarationElement).setInherited());
            }
            else if (element instanceof PerlGlobVariableElement globVariableElement &&
                     globVariableElement.isLeftSideOfAssignment() && globVariableElement.getName() != null) {
              inheritedResult.add(new PerlGlobStructureViewElement(globVariableElement).setInherited());
            }
          }
        }
      }

      if (!inheritedResult.isEmpty()) {
        result.addAll(0, inheritedResult);
      }
    }

    return result;
  }
}
