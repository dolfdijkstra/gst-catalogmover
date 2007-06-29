package com.fatwire.cs.catalogmover.util.chunk;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import junit.framework.TestCase;

public class BaseChunkProcessorTest extends TestCase {

    protected Set<Integer> getTestData() {
        final Set<Integer> testData = new TreeSet<Integer>();
        for (int i = 0; i < 100; i++) {
            testData.add(i);
        }
        return testData;
    }

    public void testProcess() throws Exception {
        final BaseChunkProcessor<Integer, Exception> chunkProcessor = new BaseChunkProcessor<Integer, Exception>(
                getTestData(), 10);
        final AtomicInteger result = new AtomicInteger(0);
        final AtomicInteger chunks = new AtomicInteger(0);
        chunkProcessor.process(new Processor<Integer, Exception>() {

            public boolean beginChunk() {
                chunks.addAndGet(1);
                return true;

            }

            public boolean endChunk() {
                chunks.addAndGet(-2);
                return true;
            }

            public boolean process(final Integer element) {
                result.addAndGet(element);
                return true;
            }
        });

        Assert.assertEquals(-10, chunks.get());
        Assert.assertEquals(4950, result.get());
    }

    public void testExceptionHandling() {
        final BaseChunkProcessor<Integer, ProcessingException> chunkProcessor = new BaseChunkProcessor<Integer, ProcessingException>(
                getTestData(), 10);
        final AtomicInteger result = new AtomicInteger(0);
        final AtomicInteger chunks = new AtomicInteger(0);
        try {
            chunkProcessor
                    .process(new Processor<Integer, ProcessingException>() {

                        public boolean beginChunk() {
                            chunks.addAndGet(1);
                            return true;

                        }

                        public boolean endChunk() {
                            chunks.addAndGet(-2);
                            return true;
                        }

                        public boolean process(final Integer element)
                                throws ProcessingException {
                            if (element.intValue() == 10) {
                                throw new ProcessingException("Can't handle 10");
                            }
                            result.addAndGet(1);
                            return true;
                        }

                    });
        } catch (ProcessingException e) {
            Assert.assertEquals("Can't handle 10", e.getMessage());
        }

        Assert.assertEquals(-2, chunks.get());
        Assert.assertEquals(10, result.get());

    }

    public void testProcessWithTrans() throws ProcessingException {
        final BaseChunkProcessor<Integer, ProcessingException> chunkProcessor = new BaseChunkProcessor<Integer, ProcessingException>(
                getTestData(), 10);
        MockTrans trans = new MockTrans();
        chunkProcessor.process(trans);

        Assert.assertEquals(false, trans.open);
        Assert.assertEquals(4950, trans.total);
    }

    class MockTrans implements Processor<Integer, ProcessingException> {
        boolean open;

        int total;

        public boolean beginChunk() {
            open = true;
            return true;

        }

        public boolean endChunk() {
            open = false;
            return true;
        }

        public boolean process(final Integer element) {
            if (open)
                total += element;
            return true;

        }

    }
}
