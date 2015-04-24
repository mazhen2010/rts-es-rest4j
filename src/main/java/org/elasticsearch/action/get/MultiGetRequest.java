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

package org.elasticsearch.action.get;

import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.lucene.uid.Versions;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.search.fetch.source.FetchSourceContext;

import java.io.IOException;
import java.util.Arrays;

public class MultiGetRequest {

    public static final IndicesOptions INDICES_OPTIONS = IndicesOptions.strictSingleIndexNoExpandForbidClosed();

    /**
     * A single get item.
     */
    public static class Item implements Streamable, IndicesRequest {
        private String index;
        private String type;
        private String id;
        private String routing;
        private String[] fields;
        private long version = Versions.MATCH_ANY;
        private VersionType versionType = VersionType.INTERNAL;
        private FetchSourceContext fetchSourceContext;

        public Item() {

        }

        /**
         * Constructs a single get item.
         *
         * @param index The index name
         * @param type  The type (can be null)
         * @param id    The id
         */
        public Item(String index, @Nullable String type, String id) {
            this.index = index;
            this.type = type;
            this.id = id;
        }

        public String index() {
            return this.index;
        }

        @Override
        public String[] indices() {
            return new String[]{index};
        }

        @Override
        public IndicesOptions indicesOptions() {
            return INDICES_OPTIONS;
        }

        public Item index(String index) {
            this.index = index;
            return this;
        }

        public String type() {
            return this.type;
        }

        public Item type(String type) {
            this.type = type;
            return this;
        }

        public String id() {
            return this.id;
        }

        /**
         * The routing associated with this document.
         */
        public Item routing(String routing) {
            this.routing = routing;
            return this;
        }

        public String routing() {
            return this.routing;
        }

        public Item parent(String parent) {
            if (routing == null) {
                this.routing = parent;
            }
            return this;
        }

        public Item fields(String... fields) {
            this.fields = fields;
            return this;
        }

        public String[] fields() {
            return this.fields;
        }

        public long version() {
            return version;
        }

        public Item version(long version) {
            this.version = version;
            return this;
        }

        public VersionType versionType() {
            return versionType;
        }

        public Item versionType(VersionType versionType) {
            this.versionType = versionType;
            return this;
        }

        public FetchSourceContext fetchSourceContext() {
            return this.fetchSourceContext;
        }

        /**
         * Allows setting the {@link org.elasticsearch.search.fetch.source.FetchSourceContext} for this request, controlling if and how _source should be returned.
         */
        public Item fetchSourceContext(FetchSourceContext fetchSourceContext) {
            this.fetchSourceContext = fetchSourceContext;
            return this;
        }

        public static Item readItem(StreamInput in) throws IOException {
            Item item = new Item();
            item.readFrom(in);
            return item;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            index = in.readSharedString();
            type = in.readOptionalSharedString();
            id = in.readString();
            routing = in.readOptionalString();
            int size = in.readVInt();
            if (size > 0) {
                fields = new String[size];
                for (int i = 0; i < size; i++) {
                    fields[i] = in.readString();
                }
            }
            version = Versions.readVersionWithVLongForBW(in);
            versionType = VersionType.fromValue(in.readByte());

            fetchSourceContext = FetchSourceContext.optionalReadFromStream(in);
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeSharedString(index);
            out.writeOptionalSharedString(type);
            out.writeString(id);
            out.writeOptionalString(routing);
            if (fields == null) {
                out.writeVInt(0);
            } else {
                out.writeVInt(fields.length);
                for (String field : fields) {
                    out.writeString(field);
                }
            }

            Versions.writeVersionWithVLongForBW(version, out);
            out.writeByte(versionType.getValue());

            FetchSourceContext.optionalWriteToStream(fetchSourceContext, out);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Item)) return false;

            Item item = (Item) o;

            if (version != item.version) return false;
            if (fetchSourceContext != null ? !fetchSourceContext.equals(item.fetchSourceContext) : item.fetchSourceContext != null)
                return false;
            if (!Arrays.equals(fields, item.fields)) return false;
            if (!id.equals(item.id)) return false;
            if (!index.equals(item.index)) return false;
            if (routing != null ? !routing.equals(item.routing) : item.routing != null) return false;
            if (type != null ? !type.equals(item.type) : item.type != null) return false;
            if (versionType != item.versionType) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = index.hashCode();
            result = 31 * result + (type != null ? type.hashCode() : 0);
            result = 31 * result + id.hashCode();
            result = 31 * result + (routing != null ? routing.hashCode() : 0);
            result = 31 * result + (fields != null ? Arrays.hashCode(fields) : 0);
            result = 31 * result + (int) (version ^ (version >>> 32));
            result = 31 * result + versionType.hashCode();
            result = 31 * result + (fetchSourceContext != null ? fetchSourceContext.hashCode() : 0);
            return result;
        }
    }
}
