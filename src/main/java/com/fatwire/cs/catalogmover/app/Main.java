package com.fatwire.cs.catalogmover.app;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.catalogs.filter.Filter;
import com.fatwire.cs.catalogmover.catalogs.filter.PatternBasedIdColumnRowFilter;
import com.fatwire.cs.catalogmover.mover.CatalogExporter;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.mover.PoolableHttpAccessTransporter;
import com.fatwire.cs.catalogmover.mover.RemoteCatalog;
import com.fatwire.cs.catalogmover.mover.StdOutProgressMonitor;
import com.fatwire.cs.catalogmover.mover.commands.ExportAllCatalogsCommand;
import com.fatwire.cs.catalogmover.mover.commands.FilteringExportCatalogCommand;
import com.fatwire.cs.catalogmover.util.HttpClientUtil;

public class Main {

    static void initLog4j() {
        ConsoleAppender appender = new ConsoleAppender();
        appender.setLayout(new PatternLayout("%-5p [%.10t]: %m%n"));
        appender.setName("console");
        appender.activateOptions();
        Logger.getRootLogger().addAppender(appender);
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("com.fatwire").setLevel(Level.INFO);
    }

    /**
     * @param args
     * @throws CatalogMoverException 
     */
    public static void main(String[] args) throws CatalogMoverException {
        initLog4j();
        //        URI uri = URI.create("http://localhost:8080/cs/CatalogManager");
        //        String username = "ContentServer";
        //        String password = "password";
        //        File populateDirectory = new File("C:\\TEMP\\support-tools-3.7");
        //        String pattern ="Support/.*";
        if (args.length !=5) throw new IllegalArgumentException("arguments should be 5, uri,username, password,populateDirectory,pattern ");
        URI uri = URI.create(args[0]);
        String username = args[1];
        String password = args[2];
        File populateDirectory = new File(args[3]);
        String pattern = args[4];
        doMain(uri, username, password, populateDirectory, pattern);

    }

    public static void doMain(URI uri, String username, String password,
            File populateDirectory, String pattern)
            throws CatalogMoverException {

        Set<String> catalogs = new HashSet<String>();
        catalogs.add("ElementCatalog");
        catalogs.add("SiteCatalog");
        catalogs.add("SystemSQL");
        //catalogs.add("SystemInfo");

        int size = 10;
        MultiThreadedHttpConnectionManager conManager = HttpClientUtil
                .getConnectionManager(size);
        final PoolableHttpAccessTransporter transporter = new PoolableHttpAccessTransporter(
                conManager);

        transporter.setCsPath(uri);
        transporter.setUsername(username);
        transporter.setPassword(password);
        //transporter.init();
        ExecutorService executor = Executors.newFixedThreadPool(size);
        final CatalogExporter cm = new CatalogExporter(transporter, executor);
        try {
            final IProgressMonitor monitor = new StdOutProgressMonitor();
            if ((catalogs.size() == 1) && catalogs.contains("SystemInfo")) {

                monitor.beginTask("Exporting all catalogs", -1);

                final ExportAllCatalogsCommand command = new ExportAllCatalogsCommand(
                        cm, populateDirectory, monitor);
                command.execute();

            } else {
                final List<Filter<Row>> includeFilters = new ArrayList<Filter<Row>>();
                includeFilters.add(new PatternBasedIdColumnRowFilter(Pattern
                        .compile(pattern)));
                for (String name : catalogs) {
                    monitor.beginTask("Exporting " + name, -1);
                    RemoteCatalog rc = new RemoteCatalog(name,
                            populateDirectory);
                    FilteringExportCatalogCommand command = new FilteringExportCatalogCommand(
                            cm, rc, includeFilters, null, monitor);

                    command.execute();
                    monitor.worked(1);
                }

            }

        } finally {
            cm.close();
            executor.shutdown();
            conManager.shutdown();

        }

    }

}
