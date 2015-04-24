package org.apache.lucene.search;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.util.automaton.LevenshteinAutomata;

/** Implements the fuzzy search query. The similarity measurement
 * is based on the Damerau-Levenshtein (optimal string alignment) algorithm,
 * though you can explicitly choose classic Levenshtein by passing <code>false</code>
 * to the <code>transpositions</code> parameter.
 * 
 * <p>This query uses {@link MultiTermQuery.TopTermsScoringBooleanQueryRewrite}
 * as default. So terms will be collected and scored according to their
 * edit distance. Only the top terms are used for building the {@link BooleanQuery}.
 * It is not recommended to change the rewrite mode for fuzzy queries.
 * 
 * <p>At most, this query will match terms up to 
 * {@value org.apache.lucene.util.automaton.LevenshteinAutomata#MAXIMUM_SUPPORTED_DISTANCE} edits. 
 * Higher distances (especially with transpositions enabled), are generally not useful and 
 * will match a significant amount of the term dictionary. If you really want this, consider
 * using an n-gram indexing technique (such as the SpellChecker in the 
 * <a href="{@docRoot}/../suggest/overview-summary.html">suggest module</a>) instead.
 *
 * <p>NOTE: terms of length 1 or 2 will sometimes not match because of how the scaled
 * distance between two terms is computed.  For a term to match, the edit distance between
 * the terms must be less than the minimum length term (either the input term, or
 * the candidate term).  For example, FuzzyQuery on term "abcd" with maxEdits=2 will
 * not match an indexed term "ab", and FuzzyQuery on term "a" with maxEdits=2 will not
 * match an indexed term "abc".
 */
public class FuzzyQuery {
  /**
   * Helper function to convert from deprecated "minimumSimilarity" fractions
   * to raw edit distances.
   * 
   * @param minimumSimilarity scaled similarity
   * @param termLen length (in unicode codepoints) of the term.
   * @return equivalent number of maxEdits
   * @deprecated pass integer edit distances instead.
   */
  @Deprecated
  public static int floatToEdits(float minimumSimilarity, int termLen) {
    if (minimumSimilarity >= 1f) {
      return (int) Math.min(minimumSimilarity, LevenshteinAutomata.MAXIMUM_SUPPORTED_DISTANCE);
    } else if (minimumSimilarity == 0.0f) {
      return 0; // 0 means exact, not infinite # of edits!
    } else {
      return Math.min((int) ((1D-minimumSimilarity) * termLen), 
        LevenshteinAutomata.MAXIMUM_SUPPORTED_DISTANCE);
    }
  }
}
