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

package org.elasticsearch.index.query;

import org.elasticsearch.common.ParseField;

/**
 *
 */
public class MoreLikeThisQueryParser {

    public static final String NAME = "mlt";

    public static class Fields {
        public static final ParseField LIKE_TEXT = new ParseField("like_text");
        public static final ParseField MIN_TERM_FREQ = new ParseField("min_term_freq");
        public static final ParseField MAX_QUERY_TERMS = new ParseField("max_query_terms");
        public static final ParseField MIN_WORD_LENGTH = new ParseField("min_word_length", "min_word_len");
        public static final ParseField MAX_WORD_LENGTH = new ParseField("max_word_length", "max_word_len");
        public static final ParseField MIN_DOC_FREQ = new ParseField("min_doc_freq");
        public static final ParseField MAX_DOC_FREQ = new ParseField("max_doc_freq");
        public static final ParseField BOOST_TERMS = new ParseField("boost_terms");
        public static final ParseField PERCENT_TERMS_TO_MATCH = new ParseField("percent_terms_to_match");
        public static final ParseField FAIL_ON_UNSUPPORTED_FIELD = new ParseField("fail_on_unsupported_field");
        public static final ParseField STOP_WORDS = new ParseField("stop_words");
        public static final ParseField DOCUMENT_IDS = new ParseField("ids");
        public static final ParseField DOCUMENTS = new ParseField("docs");
        public static final ParseField INCLUDE = new ParseField("include");
        public static final ParseField EXCLUDE = new ParseField("exclude");
    }

    public MoreLikeThisQueryParser() {

    }
}