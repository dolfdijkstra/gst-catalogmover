package com.fatwire.cs.catalogmover.mover;

public interface IProgressMonitor {

    void beginTask(String name, int size);

    void worked(int i);

    boolean isCanceled();

    void subTask(String task);

}
