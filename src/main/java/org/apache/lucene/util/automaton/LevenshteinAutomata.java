package org.apache.lucene.util.automaton;

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

/**
 * Class to construct DFAs that match a word within some edit distance.
 * <p>
 * Implements the algorithm described in:
 * Schulz and Mihov: Fast String Correction with Levenshtein Automata
 * <p>
 * @lucene.experimental
 */
public class LevenshteinAutomata {
  /** Maximum edit distance this class can generate an automaton for.
   *  @lucene.internal */
  public static final int MAXIMUM_SUPPORTED_DISTANCE = 2;
}
