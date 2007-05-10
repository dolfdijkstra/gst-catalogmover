package com.fatwire.cs.catalogmover.mover;

public class NullProgressMonitor implements IProgressMonitor {

    public void beginTask(final String string, final int i) {

    }

    public boolean isCanceled() {
        return false;
    }

    public void subTask(final String string) {

    }

    public void worked(final int i) {

    }

}
