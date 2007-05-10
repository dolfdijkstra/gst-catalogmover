package com.fatwire.cs.catalogmover.mover;

public class StdOutProgressMonitor implements IProgressMonitor {
    private String task;

    public void beginTask(final String string, final int i) {
        task = string;
        System.out.println("begin task " + string);

    }

    public boolean isCanceled() {
        return false;
    }

    public void subTask(final String string) {
        System.out.println("begin sub task " + string);
    }

    public void worked(final int i) {
        System.out.println(" task " + task + " has worked " + i);
    }

}
