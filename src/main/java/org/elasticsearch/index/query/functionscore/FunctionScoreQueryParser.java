/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.query.functionscore;

import org.elasticsearch.common.ParseField;

/**
 *
 */
public class FunctionScoreQueryParser {

    public static final String NAME = "function_score";
    // For better readability of error message
    static final String MISPLACED_FUNCTION_MESSAGE_PREFIX = "You can either define \"functions\":[...] or a single function, not both. ";
    static final String MISPLACED_BOOST_FUNCTION_MESSAGE_SUFFIX = " Did you mean \"boost\" instead?";

    public static final ParseField WEIGHT_FIELD = new ParseField("weight");

}