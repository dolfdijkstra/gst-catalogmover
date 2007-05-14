package com.fatwire.cs.catalogmover.mover;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import junit.framework.TestCase;

public abstract class AbstractTest extends TestCase {

    public AbstractTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();
        ConsoleAppender appender = new ConsoleAppender();
        appender.setLayout(new PatternLayout("%-5p [%.10t]: %m%n"));
        appender.setName("console");
        appender.activateOptions();
        Logger.getRootLogger().addAppender(appender);
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("com.fatwire").setLevel(Level.INFO);
    
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

}