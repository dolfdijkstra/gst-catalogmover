package com.fatwire.cs.catalogmover.mover;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import junit.framework.TestCase;

import com.fatwire.cs.catalogmover.mover.CatalogMover;
import com.fatwire.cs.catalogmover.mover.LocalCatalog;
import com.fatwire.cs.catalogmover.mover.StdOutProgressMonitor;
import com.fatwire.cs.core.http.HttpAccessException;

public class CatalogMoverTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ConsoleAppender appender = new ConsoleAppender();
        appender.setLayout(new PatternLayout("%-5p [%-10t]: %m%n"));
        appender.setName("console");
        appender.activateOptions();
        Logger.getRootLogger().addAppender(appender);
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("com.fatwire").setLevel(Level.INFO);

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMoveCatalog() throws URISyntaxException,
            HttpAccessException, IOException {
        if (true)
            return;
        final CatalogMover cm = new CatalogMover();

        cm.setCsPath(new URI(
                "http://radium.nl.fatwire.com:8080/cs/CatalogManager"));
        cm.setUsername("fwadmin");
        cm.setPassword("xceladmin");

        final File f = new File(
                "C:\\tmp\\support-tools\\trunk\\cs\\src\\main\\Populate\\ElementCatalog.html");
        final LocalCatalog ec = new LocalCatalog(f);
        ec.refresh();
        cm.setCatalog(ec);
        cm.moveCatalog(new StdOutProgressMonitor());
        final File f2 = new File(
                "C:\\tmp\\support-tools\\trunk\\cs\\src\\main\\Populate\\SiteCatalog.html");
        final LocalCatalog sc = new LocalCatalog(f);
        sc.refresh();
        cm.setCatalog(sc);
        cm.moveCatalog(new StdOutProgressMonitor());

    }

}
