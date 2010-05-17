package com.fatwire.cs.catalogmover.mover;

public class StdOutProgressMonitor implements IProgressMonitor {
    private String task;

    public void beginTask(final String string, final int i) {
        task = string;
        System.out.println(string);
        if (string ==null){
            new Exception().printStackTrace();
        }

    }

    public boolean isCanceled() {
        return false;
    }

    public void subTask(final String string) {
        //System.out.println("sub task " + string);
        System.out.print(".");
    }

    public void worked(final int i) {
        //System.out.println(" task " + task + " has worked " + i);
    }

}
