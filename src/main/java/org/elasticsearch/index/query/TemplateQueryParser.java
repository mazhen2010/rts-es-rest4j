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

import org.elasticsearch.script.ScriptService;

import java.util.HashMap;
import java.util.Map;

/**
 * In the simplest case, parse template string and variables from the request, compile the template and
 * execute the template against the given variables.
 * */
public class TemplateQueryParser {

    /** Name to reference this type of query. */
    public static final String NAME = "template";
    /** Name of query parameter containing the template string. */
    public static final String QUERY = "query";
    /** Name of query parameter containing the template parameters. */
    public static final String PARAMS = "params";

    private final static Map<String,ScriptService.ScriptType> parametersToTypes =
            new HashMap<String,ScriptService.ScriptType>();
    static {
        parametersToTypes.put("query", ScriptService.ScriptType.INLINE);
        parametersToTypes.put("file", ScriptService.ScriptType.FILE);
        parametersToTypes.put("id", ScriptService.ScriptType.INDEXED);
    }

}
