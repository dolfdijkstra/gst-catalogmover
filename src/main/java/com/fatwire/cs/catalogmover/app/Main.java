package com.fatwire.cs.catalogmover.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.catalogs.filter.Filter;
import com.fatwire.cs.catalogmover.catalogs.filter.PatternBasedIdColumnRowFilter;
import com.fatwire.cs.catalogmover.http.HttpClientTransporter;
import com.fatwire.cs.catalogmover.mover.CatalogExporter;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.mover.RemoteCatalog;
import com.fatwire.cs.catalogmover.mover.StdOutProgressMonitor;
import com.fatwire.cs.catalogmover.mover.commands.ExportAllCatalogsCommand;
import com.fatwire.cs.catalogmover.mover.commands.FilteringExportCatalogCommand;
import com.fatwire.cs.catalogmover.util.HttpClientUtil;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

public class Main {

    static void initLog4j() {
        URL p = Thread.currentThread().getContextClassLoader().getResource("log4j.properties");
        if (p != null) {
            PropertyConfigurator.configure(p);
        } else {
            p = Thread.currentThread().getContextClassLoader().getResource("log4j.xml");
            if (p != null) {
                org.apache.log4j.xml.DOMConfigurator.configure(p);
            }
        }

        if (p == null) {

            ConsoleAppender appender = new ConsoleAppender();
            appender.setLayout(new PatternLayout("%-5p %c [%.10t]: %m%n"));
            appender.setName("console");
            appender.activateOptions();
            Logger.getRootLogger().addAppender(appender);
            Logger.getRootLogger().setLevel(Level.INFO);
            Logger.getLogger("com.fatwire").setLevel(Level.INFO);
            Logger.getLogger("httpclient.wire.header").setLevel(Level.INFO);
            // Logger.getLogger(org.apache.commons.httpclient.HttpConnection.class).setLevel(Level.TRACE);
        }
    }

    /**
     * @param args
     * @throws CatalogMoverException
     * @throws IOException
     */
    public static void main(String[] a) throws CatalogMoverException, IOException {
        if (a.length < 1) {
            System.err.println("You need to add a properties file name as the first argument");
            System.exit(-1);
        }
        initLog4j();

        Properties p = new Properties();
        if (a.length == 1) {
            FileInputStream in = new FileInputStream(a[0]);
            p.load(in);
            in.close();
        } else {
            for (String s : a) {
                if (s.startsWith("--")) {
                    int t = s.indexOf('=');
                    if (t > 2) {
                        String key = s.substring(2, t);
                        String value = s.substring(t + 1);
                        p.setProperty(key, value.trim());
                    }

                }

            }
        }
        if (StringUtils.isBlank(p.getProperty("host")))
            throw new IllegalArgumentException("The parameter 'host' can not be blank");
        if (StringUtils.isBlank(p.getProperty("user")))
            throw new IllegalArgumentException("The parameter 'user' can not be blank");
        if (StringUtils.isBlank(p.getProperty("password"))) {
            p.setProperty("password", readPassword());
        }
        if (StringUtils.isBlank(p.getProperty("password")))
            throw new IllegalArgumentException("The parameter 'password' can not be blank");

        URI uri = URI.create(p.getProperty("host"));
        String username = p.getProperty("user");
        String password = p.getProperty("password");
        File populateDirectory = new File(p.getProperty("target", "./target/" + uri.getHost() + "_"
                + (uri.getPort() == -1 ? "80" : uri.getPort())));
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
        String cats = p.getProperty("catalogs");
        if (cats == null || cats.length() == 0) {
            catalogs.add("ElementCatalog");
            catalogs.add("SiteCatalog");
            catalogs.add("SystemSQL");
        } else {
            for (String s : cats.split(",")) {
                if (s.trim().length() > 0) {
                    catalogs.add(s.trim());
                }
            }
        }
        // catalogs.add("SystemInfo");

        try {
            new Main().export(uri, username, password, proxyHost, proxyPort, proxyUsername, proxyPassword,
                    populateDirectory, pattern, catalogs);
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public static String readPassword() {
        System.out.print("Enter password: ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String pw = null;

        try {
            pw = br.readLine();
        } catch (IOException ioe) {
            System.out.println("IO error trying to read your password!");
            System.exit(1);
        }
        return pw;
    }

    public void export(URI uri, String username, String password, String proxyHost, int proxyPort,
            String proxyUsername, String proxyPassword, File populateDirectory, String pattern, Set<String> catalogs)
            throws CatalogMoverException {

        int size = 3;
        MultiThreadedHttpConnectionManager conManager = HttpClientUtil.getConnectionManager(size);

        HttpState state = new HttpState();
        if (!StringUtils.isBlank(proxyUsername) && !StringUtils.isBlank(proxyPassword)) {
            Credentials credentials = new UsernamePasswordCredentials(proxyUsername, proxyPassword);

            state.setProxyCredentials(AuthScope.ANY, credentials);
        }
        ProxyHost proxyHost1 = StringUtils.isNotBlank(proxyHost) ? new ProxyHost(proxyHost, proxyPort) : null;
        HttpClientTransporter transporter = new HttpClientTransporter(conManager, state, proxyHost1);

        transporter.setCsPath(uri);
        transporter.setUsername(username);
        transporter.setPassword(password);
        ExecutorService executor = Executors.newFixedThreadPool(size);
        final CatalogExporter cm = new CatalogExporter(transporter, executor);
        try {
            final IProgressMonitor monitor = new StdOutProgressMonitor();
            if ((catalogs.size() == 1) && catalogs.contains("SystemInfo")) {

                monitor.beginTask("Exporting all catalogs", -1);

                final ExportAllCatalogsCommand command = new ExportAllCatalogsCommand(cm, populateDirectory, monitor);
                command.execute();

            } else {
                List<Filter<Row>> includeFilters = null;
                if (StringUtils.isNotBlank(pattern)) {
                    includeFilters = new ArrayList<Filter<Row>>();

                    includeFilters.add(new PatternBasedIdColumnRowFilter(Pattern.compile(pattern)));
                }

                for (String name : catalogs) {
                    monitor.beginTask("Exporting " + name, -1);
                    RemoteCatalog rc = new RemoteCatalog(name, populateDirectory);
                    FilteringExportCatalogCommand command = new FilteringExportCatalogCommand(cm, rc, includeFilters,
                            null, monitor);

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
