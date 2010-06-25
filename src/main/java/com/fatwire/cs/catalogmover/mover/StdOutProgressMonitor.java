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
