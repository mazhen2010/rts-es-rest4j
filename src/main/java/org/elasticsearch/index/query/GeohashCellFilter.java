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

import org.elasticsearch.common.geo.GeoHashUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

/**
 * A geohash cell filter that filters {@link org.elasticsearch.common.geo.GeoPoint}s by their geohashes. Basically the a
 * Geohash prefix is defined by the filter and all geohashes that are matching this
 * prefix will be returned. The <code>neighbors</code> flag allows to filter
 * geohashes that surround the given geohash. In general the neighborhood of a
 * geohash is defined by its eight adjacent cells.<br />
 * The structure of the {@link GeohashCellFilter} is defined as:
 * <pre>
 * &quot;geohash_bbox&quot; {
 *     &quot;field&quot;:&quot;location&quot;,
 *     &quot;geohash&quot;:&quot;u33d8u5dkx8k&quot;,
 *     &quot;neighbors&quot;:false
 * }
 * </pre>
 */
public class GeohashCellFilter {

    public static final String NAME = "geohash_cell";
    public static final String NEIGHBORS = "neighbors";
    public static final String PRECISION = "precision";
    public static final String CACHE = "_cache";
    public static final String CACHE_KEY = "_cache_key";

    /**
     * Builder for a geohashfilter. It needs the fields <code>fieldname</code> and
     * <code>geohash</code> to be set. the default for a neighbor filteing is
     * <code>false</code>.
     */
    public static class Builder extends BaseFilterBuilder {
        // we need to store the geohash rather than the corresponding point,
        // because a transformation from a geohash to a point an back to the
        // geohash will extend the accuracy of the hash to max precision
        // i.e. by filing up with z's.
        private String field;
        private String geohash;
        private int levels = -1;
        private boolean neighbors;
        private Boolean cache;
        private String cacheKey;


        public Builder(String field) {
            this(field, null, false);
        }

        public Builder(String field, GeoPoint point) {
            this(field, point.geohash(), false);
        }

        public Builder(String field, String geohash) {
            this(field, geohash, false);
        }

        public Builder(String field, String geohash, boolean neighbors) {
            super();
            this.field = field;
            this.geohash = geohash;
            this.neighbors = neighbors;
        }

        public Builder point(GeoPoint point) {
            this.geohash = point.getGeohash();
            return this;
        }

        public Builder point(double lat, double lon) {
            this.geohash = GeoHashUtils.encode(lat, lon);
            return this;
        }

        public Builder geohash(String geohash) {
            this.geohash = geohash;
            return this;
        }

        public Builder precision(int levels) {
            this.levels = levels;
            return this;
        }

        public Builder neighbors(boolean neighbors) {
            this.neighbors = neighbors;
            return this;
        }

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        /**
         * Should the filter be cached or not. Defaults to <tt>false</tt>.
         */
        public Builder cache(boolean cache) {
            this.cache = cache;
            return this;
        }

        public Builder cacheKey(String cacheKey) {
            this.cacheKey = cacheKey;
            return this;
        }

        @Override
        protected void doXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
            builder.startObject(NAME);
            if (neighbors) {
                builder.field(NEIGHBORS, neighbors);
            }
            if(levels > 0) {
                builder.field(PRECISION, levels);
            }
            if (cache != null) {
                builder.field(CACHE, cache);
            }
            if (cacheKey != null) {
                builder.field(CACHE_KEY, cacheKey);
            }
            builder.field(field, geohash);

            builder.endObject();
        }
    }
}
