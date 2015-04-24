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

package org.elasticsearch.script;

import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.ElasticsearchIllegalStateException;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

public class ScriptService {

    public static enum ScriptType {

        INLINE,
        INDEXED,
        FILE;

        private static final int INLINE_VAL = 0;
        private static final int INDEXED_VAL = 1;
        private static final int FILE_VAL = 2;

        public static ScriptType readFrom(StreamInput in) throws IOException {
            int scriptTypeVal = in.readVInt();
            switch (scriptTypeVal) {
                case INDEXED_VAL:
                    return INDEXED;
                case INLINE_VAL:
                    return INLINE;
                case FILE_VAL:
                    return FILE;
                default:
                    throw new ElasticsearchIllegalArgumentException("Unexpected value read for ScriptType got [" + scriptTypeVal +
                            "] expected one of ["+INLINE_VAL +"," + INDEXED_VAL + "," + FILE_VAL+"]");
            }
        }

        public static void writeTo(ScriptType scriptType, StreamOutput out) throws IOException {
            if (scriptType != null) {
                switch (scriptType){
                    case INDEXED:
                        out.writeVInt(INDEXED_VAL);
                        return;
                    case INLINE:
                        out.writeVInt(INLINE_VAL);
                        return;
                    case FILE:
                        out.writeVInt(FILE_VAL);
                        return;
                    default:
                        throw new ElasticsearchIllegalStateException("Unknown ScriptType " + scriptType);
                }
            } else {
                out.writeVInt(INLINE_VAL); //Default to inline
            }
        }
    }

    static class IndexedScript {
        private final String lang;
        private final String id;

        IndexedScript(String lang, String script) {
            this.lang = lang;
            final String[] parts = script.split("/");
            if (parts.length == 1) {
                this.id = script;
            } else {
                if (parts.length != 3) {
                    throw new ElasticsearchIllegalArgumentException("Illegal index script format [" + script + "]" +
                            " should be /lang/id");
                } else {
                    if (!parts[1].equals(this.lang)) {
                        throw new ElasticsearchIllegalStateException("Conflicting script language, found [" + parts[1] + "] expected + ["+ this.lang + "]");
                    }
                    this.id = parts[2];
                }
            }
        }
    }

}
