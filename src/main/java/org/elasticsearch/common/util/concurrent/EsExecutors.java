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

package org.elasticsearch.common.util.concurrent;

import org.elasticsearch.common.settings.Settings;

/**
 *
 */
public class EsExecutors {

    /**
     * Settings key to manually set the number of available processors.
     * This is used to adjust thread pools sizes etc. per node.
     */
    public static final String PROCESSORS = "processors";

    /**
     * Returns the number of processors available but at most <tt>32</tt>.
     */
    public static int boundedNumberOfProcessors(Settings settings) {
        /* This relates to issues where machines with large number of cores
         * ie. >= 48 create too many threads and run into OOM see #3478
         * We just use an 32 core upper-bound here to not stress the system
         * too much with too many created threads */
        return settings.getAsInt(PROCESSORS, Math.min(32, Runtime.getRuntime().availableProcessors()));
    }

}
