package com.fatwire.cs.catalogmover.mover;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import junit.framework.TestCase;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.fatwire.cs.catalogmover.mover.commands.MoveCatalogCommand;

public class CatalogMoverTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ConsoleAppender appender = new ConsoleAppender();
        appender.setLayout(new PatternLayout("%-5p [%-10t]: %m%n"));
        appender.setName("console");
        appender.activateOptions();
        Logger.getRootLogger().addAppender(appender);
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("com.fatwire").setLevel(Level.TRACE);

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMoveCatalog() throws IOException, CatalogMoverException {
        if (false)
            return;
        final BaseCatalogMover cm = new CatalogMover();

        cm.setCsPath(URI
                .create("http://radium.nl.fatwire.com:8080/cs/CatalogManager"));
        cm.setUsername("fwadmin");
        cm.setPassword("xceladmin");
        cm.init();

        final File f = new File(
                "C:\\tmp\\support-tools\\trunk\\cs\\src\\main\\Populate\\ElementCatalog.html");
        final LocalCatalog ec = new LocalCatalog(f);
        ec.refresh();
        MoveCatalogCommand command = new MoveCatalogCommand(cm, ec,
                new StdOutProgressMonitor());
        command.execute();
        final File f2 = new File(
                "C:\\tmp\\support-tools\\trunk\\cs\\src\\main\\Populate\\SiteCatalog.html");
        final LocalCatalog sc = new LocalCatalog(f2);
        sc.refresh();
        command = new MoveCatalogCommand(cm, sc, new StdOutProgressMonitor());
        command.execute();
        cm.close();

    }

}
