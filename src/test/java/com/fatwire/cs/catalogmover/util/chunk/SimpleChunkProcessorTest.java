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
