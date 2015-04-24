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

package org.elasticsearch.threadpool;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.unit.SizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ThreadPool {

    public static class Names {
        public static final String SEARCH = "search";
    }

    public static final String THREADPOOL_GROUP = "threadpool.";

    private volatile ImmutableMap<String, ExecutorHolder> executors;

    private final Queue<ExecutorHolder> retiredExecutors = new ConcurrentLinkedQueue<ExecutorHolder>();


    public Info info(String name) {
        ExecutorHolder holder = executors.get(name);
        if (holder == null) {
            return null;
        }
        return holder.info;
    }

    public Executor executor(String name) {
        Executor executor = executors.get(name).executor;
        if (executor == null) {
            throw new ElasticsearchIllegalArgumentException("No executor found for [" + name + "]");
        }
        return executor;
    }


    static class ExecutorHolder {
        public final Executor executor;
        public final Info info;

        ExecutorHolder(Executor executor, Info info) {
            this.executor = executor;
            this.info = info;
        }
    }

    public static class Info implements Streamable, ToXContent {

        private String name;
        private String type;
        private int min;
        private int max;
        private TimeValue keepAlive;
        private SizeValue queueSize;

        Info() {

        }

        public Info(String name, String type) {
            this(name, type, -1);
        }

        public Info(String name, String type, int size) {
            this(name, type, size, size, null, null);
        }

        public Info(String name, String type, int min, int max, @Nullable TimeValue keepAlive, @Nullable SizeValue queueSize) {
            this.name = name;
            this.type = type;
            this.min = min;
            this.max = max;
            this.keepAlive = keepAlive;
            this.queueSize = queueSize;
        }

        public String getName() {
            return this.name;
        }

        public String getType() {
            return this.type;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        @Nullable
        public TimeValue getKeepAlive() {
            return this.keepAlive;
        }

        @Nullable
        public SizeValue getQueueSize() {
            return this.queueSize;
        }

        public void readFrom(StreamInput in) throws IOException {
            name = in.readString();
            type = in.readString();
            min = in.readInt();
            max = in.readInt();
            if (in.readBoolean()) {
                keepAlive = TimeValue.readTimeValue(in);
            }
            if (in.readBoolean()) {
                queueSize = SizeValue.readSizeValue(in);
            }
            in.readBoolean(); // here to conform with removed waitTime
            in.readBoolean(); // here to conform with removed rejected setting
            in.readBoolean(); // here to conform with queue type
        }

        public void writeTo(StreamOutput out) throws IOException {
            out.writeString(name);
            out.writeString(type);
            out.writeInt(min);
            out.writeInt(max);
            if (keepAlive == null) {
                out.writeBoolean(false);
            } else {
                out.writeBoolean(true);
                keepAlive.writeTo(out);
            }
            if (queueSize == null) {
                out.writeBoolean(false);
            } else {
                out.writeBoolean(true);
                queueSize.writeTo(out);
            }
            out.writeBoolean(false); // here to conform with removed waitTime
            out.writeBoolean(false); // here to conform with removed rejected setting
            out.writeBoolean(false); // here to conform with queue type
        }

        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject(name, XContentBuilder.FieldCaseConversion.NONE);
            builder.field(Fields.TYPE, type);
            if (min != -1) {
                builder.field(Fields.MIN, min);
            }
            if (max != -1) {
                builder.field(Fields.MAX, max);
            }
            if (keepAlive != null) {
                builder.field(Fields.KEEP_ALIVE, keepAlive.toString());
            }
            if (queueSize == null) {
                builder.field(Fields.QUEUE_SIZE, -1);
            } else {
                builder.field(Fields.QUEUE_SIZE, queueSize.toString());
            }
            builder.endObject();
            return builder;
        }

        static final class Fields {
            static final XContentBuilderString TYPE = new XContentBuilderString("type");
            static final XContentBuilderString MIN = new XContentBuilderString("min");
            static final XContentBuilderString MAX = new XContentBuilderString("max");
            static final XContentBuilderString KEEP_ALIVE = new XContentBuilderString("keep_alive");
            static final XContentBuilderString QUEUE_SIZE = new XContentBuilderString("queue_size");
        }

    }

    /**
     * Returns <code>true</code> if the given service was terminated successfully. If the termination timed out,
     * the service is <code>null</code> this method will return <code>false</code>.
     */
    public static boolean terminate(ExecutorService service, long timeout, TimeUnit timeUnit) {
        if (service != null) {
            service.shutdown();
            try {
                if (service.awaitTermination(timeout, timeUnit)) {
                    return true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            service.shutdownNow();
        }
        return false;
    }
}
