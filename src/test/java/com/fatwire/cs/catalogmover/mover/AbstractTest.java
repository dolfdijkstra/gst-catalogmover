package com.fatwire.cs.catalogmover.mover;

import java.io.File;
import java.net.URI;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.TestCase;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public abstract class AbstractTest extends TestCase {
    private static String tmpdir;

    public AbstractTest() {
        super();
    }

    protected boolean isRemoteDisabled() {
        return !"false".equals(System.getProperty("it.disable.test"));
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
        Logger.getRootLogger().removeAllAppenders();
        super.tearDown();
    }

    protected BaseCatalogMover prepare() {

        String url = System.getProperty("it.url");
        String username = System.getProperty("it.username");
        String password = System.getProperty("it.password");
        HttpAccessTransporter transporter = new HttpAccessTransporter();
        transporter.setCsPath(URI.create(url));
        transporter.setUsername(username);
        transporter.setPassword(password);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final BaseCatalogMover cm = new CatalogExporter(transporter,executor);
        return cm;

    }

    protected File getExportPath() {
        return new File(getTempDir(), "junit" + File.separator
                + getClass().getName() + File.separator + getName());

    }

    private static String getTempDir() {
        if (tmpdir == null) {
            final PrivilegedAction<String> a = new PrivilegedAction<String>() {

                public String run() {
                    return System.getProperty("java.io.tmpdir");
                }
            };
            tmpdir = AccessController.doPrivileged(a);

        }
        return tmpdir;
    }

}