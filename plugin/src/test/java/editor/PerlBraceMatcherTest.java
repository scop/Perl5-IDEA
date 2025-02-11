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

package editor;


import base.PerlLightTestCase;
import org.junit.Test;
public class PerlBraceMatcherTest extends PerlLightTestCase {

  @Override
  protected String getBaseDataPath() {
    return "braceMatcher/perl";
  }

  @Test
  public void testOctSubstitutionCorrect() {doTest();}

  @Test
  public void testHexSubstitutionsCorrect() {doTest();}

  @Test
  public void testUnicodeSubstitutionsCorrect() {doTest();}

  @Test
  public void testRegex() {doTest();}

  @Test
  public void testQuotes() {doTest();}

  @Test
  public void testParensAll() {doTest();}

  private void doTest() {
    doTestBraceMatcher();
  }
}
