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

package org.elasticsearch.common.lucene.uid;

import org.elasticsearch.Version;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

/** Utility class to resolve the Lucene doc ID and version for a given uid. */
public class Versions {

    public static final long MATCH_ANY = -3L; // Version was not specified by the user
    // TODO: can we remove this now?  rolling upgrades only need to handle prev (not older than that) version...?
    // the value for MATCH_ANY before ES 1.2.0 - will be removed
    public static final long MATCH_ANY_PRE_1_2_0 = 0L;
    public static final long NOT_FOUND = -1L;
    public static final long NOT_SET = -2L;

    public static void writeVersionWithVLongForBW(long version, StreamOutput out) throws IOException {
        if (out.getVersion().onOrAfter(Version.V_1_2_0)) {
            out.writeLong(version);
            return;
        }

        if (version == MATCH_ANY) {
            // we have to send out a value the node will understand
            version = MATCH_ANY_PRE_1_2_0;
        }
        out.writeVLong(version);
    }

    public static long readVersionWithVLongForBW(StreamInput in) throws IOException {
        if (in.getVersion().onOrAfter(Version.V_1_2_0)) {
            return in.readLong();
        } else {
            long version = in.readVLong();
            if (version == MATCH_ANY_PRE_1_2_0) {
                return MATCH_ANY;
            }
            return version;
        }
    }
}
