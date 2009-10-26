package com.fatwire.cs.catalogmover.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
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
     * @throws IOException 
     */
    public static void main(String[] a) throws CatalogMoverException,
            IOException {
        initLog4j();

        Properties p = new Properties();
        FileInputStream in = new FileInputStream(a[0]);
        p.load(in);
        in.close();

        URI uri = URI.create(p.getProperty("host"));
        String username = p.getProperty("user");
        String password = p.getProperty("password");
        File populateDirectory = new File(p.getProperty("target"));
        System.out.println(populateDirectory);
        String pattern = p.getProperty("pattern");

        String proxyHost = null;
        int proxyPort = 8080;

        proxyHost = p.getProperty("proxy_host");
        proxyPort = Integer.parseInt(p.getProperty("proxy_port", "8080"));

        String proxyUsername = null;
        String proxyPassword = null;
        proxyUsername = p.getProperty("proxy_user");
        proxyPassword = p.getProperty("proxy_password");
        Set<String> catalogs = new HashSet<String>();
        catalogs.add("ElementCatalog");
        catalogs.add("SiteCatalog");
        catalogs.add("SystemSQL");
        //catalogs.add("SystemInfo");

        try {
            doMain(uri, username, password, proxyHost, proxyPort,
                    proxyUsername, proxyPassword, populateDirectory, pattern,
                    catalogs);
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }
 

    public static void doMain(URI uri, String username, String password,
            String proxyHost, int proxyPort, String proxyUsername,
            String proxyPassword, File populateDirectory, String pattern,
            Set<String> catalogs) throws CatalogMoverException {

        int size = 3;
        MultiThreadedHttpConnectionManager conManager = HttpClientUtil
                .getConnectionManager(size);
        final PoolableHttpAccessTransporter transporter = new PoolableHttpAccessTransporter(
                conManager);

        transporter.setCsPath(uri);
        transporter.setUsername(username);
        transporter.setPassword(password);
        transporter.setProxyHost(proxyHost);
        transporter.setProxyPort(proxyPort);

        transporter.setProxyUsername(proxyUsername);
        transporter.setProxyPassword(proxyPassword);
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
