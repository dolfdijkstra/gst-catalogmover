package com.fatwire.cs.catalogmover.mover;

/**
 * ProgressMonitor that prints a dot to standard out on each subtask
 * 
 * @author Dolf Dijkstra
 * 
 */
public class StdOutProgressMonitor implements IProgressMonitor {

    public void beginTask(final String string, final int i) {

        System.out.println(string);
        if (string == null) {
            new Exception().printStackTrace();
        }

    }

    public boolean isCanceled() {
        return false;
    }

    public void subTask(final String string) {
        // System.out.println("sub task " + string);
        System.out.print(".");
    }

    public void worked(final int i) {
        // System.out.println(" task " + task + " has worked " + i);
    }

}
