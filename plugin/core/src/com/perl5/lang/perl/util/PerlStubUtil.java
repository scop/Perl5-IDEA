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

package com.perl5.lang.perl.util;

import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processors;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public final class PerlStubUtil {
  private PerlStubUtil() {
  }

  /**
   * @return all names from the index with {@code indexKey} limited with {@code scope}
   */
  public static @NotNull Collection<String> getAllKeys(@NotNull StubIndexKey<String, ?> indexKey, @NotNull GlobalSearchScope scope) {
    Set<String> allKeys = new THashSet<>();
    StubIndex.getInstance().processAllKeys(indexKey, Processors.cancelableCollectProcessor(allKeys), scope, null);
    return allKeys;
  }
}
