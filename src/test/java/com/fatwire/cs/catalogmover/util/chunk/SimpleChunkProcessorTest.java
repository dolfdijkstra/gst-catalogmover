/*
 * Copyright 2007 FatWire Corporation. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fatwire.cs.catalogmover.util.chunk;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import junit.framework.TestCase;

public class SimpleChunkProcessorTest extends TestCase {


    protected Set<Integer> getTestData() {
        final Set<Integer> testData = new TreeSet<Integer>();
        for (int i = 0; i < 100; i++) {
            testData.add(i);
        }
        return testData;
    }

    public void testProcess() {
        final AtomicInteger test = new AtomicInteger();
        final AtomicInteger testChunk = new AtomicInteger();
        final IterableProcessor<Integer, ProcessingException> chunkProcessor = new IterableProcessor<Integer, ProcessingException>() {

            public void process(Iterable<Integer> innerIterable)
                    throws ProcessingException {
                testChunk.addAndGet(1);
                for (Integer i : innerIterable) {
                    test.addAndGet(i);
                }

            }

        };
        final ChunkProcessor<Integer, ProcessingException> simple = new SimpleChunkProcessor<Integer, ProcessingException>(
                getTestData(), 10);
        try {
            simple.process(chunkProcessor);
        } catch (final ProcessingException e) {
            Assert.fail("should not happen");

        }
        Assert.assertEquals(10,testChunk.get());
        Assert.assertEquals(4950,test.get());
    }

}
