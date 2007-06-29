package com.fatwire.cs.catalogmover.util.chunk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class ExceptionHandlingTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private final IExceptionHandler<IOException> handler = new IExceptionHandler<IOException>() {

        public void handleException(final IOException e) {
            assertTrue(e instanceof IOException);

        }

        public boolean canHandle(Exception e) {

            return IOException.class.isAssignableFrom(e.getClass());
        }

    };

    public void testHandling() {

        Worker<IOException> worker = new Worker<IOException>(handler);
        List<ICommand> list = new ArrayList<ICommand>();
        list.add(new Command());
        worker.doWork(list);
    }

    public void testRTEHandling() {

        Worker<IOException> worker = new Worker<IOException>(handler);
        List<ICommand> list = new ArrayList<ICommand>();
        list.add(new RuntimeThrowingCommand());
        try {
            worker.doWork(list);
        } catch (ExceptionAdapter e) {
            assertTrue(e instanceof ExceptionAdapter);
        }
    }

    private interface IExceptionHandler<E extends Exception> {
        boolean canHandle(Exception e);

        void handleException(E e);

    }

    private class Command implements ICommand<IOException> {

        public void execute() throws IOException {
            throw new IOException("foo");
        }
    }

    private class RuntimeThrowingCommand implements ICommand<IOException> {

        public void execute() throws IOException {
            throw new RuntimeException("foo");
        }
    }

    private interface ICommand<E extends Exception> {
        void execute() throws E;
    }

    private class Worker<E extends Exception> {
        private final IExceptionHandler<E> handler;

        Worker(final IExceptionHandler<E> handler) {
            this.handler = handler;
        }

        void doWork(Iterable<ICommand> iterable) {
            for (ICommand i : iterable) {

                try {
                    i.execute();
                } catch (Exception e) { // catch(E e) does not compile
                    if (handler.canHandle(e))
                        handler.handleException((E) e);
                    else
                        throw new ExceptionAdapter(e);
                }
            }
        }
    }

}
