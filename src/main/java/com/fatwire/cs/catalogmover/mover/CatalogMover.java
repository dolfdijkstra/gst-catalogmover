package com.fatwire.cs.catalogmover.mover;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.catalogmover.catalogs.Header;
import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.util.ChunkedIterable;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;
import com.fatwire.cs.core.http.HostConfig;
import com.fatwire.cs.core.http.HttpAccess;
import com.fatwire.cs.core.http.HttpAccessException;
import com.fatwire.cs.core.http.Post;
import com.fatwire.cs.core.http.RequestState;
import com.fatwire.cs.core.http.Response;

public class CatalogMover {
    private final Log log = LogFactory.getLog(this.getClass());

    private static final int rowsPerPost = 20;

    private URI csPath;

    private String username;

    private String password;

    private LocalCatalog catalog;

    private HttpAccess httpAccess;

    private String ttTableName;

    String mirrorprotocolversion = null;

    public void moveCatalog(final IProgressMonitor monitor)
            throws HttpAccessException, IOException {

        move(catalog.getRows(), monitor);

    }

    public void move(final Row element, final IProgressMonitor monitor)
            throws HttpAccessException, IOException {
        final List<Row> l = new ArrayList<Row>();
        l.add(element);
        move(l, monitor);

    }

    /**
     * rows per post hardcoded to 20
     * 
     * @param rows
     * @param monitor
     * @throws HttpAccessException
     * @throws IOException
     */

    public void move(final Iterable<Row> rows, final IProgressMonitor monitor)
            throws HttpAccessException, IOException {
        monitor.beginTask("Moving rows to ContentServer for catalog "
                + catalog.getName(), -1);
        final HostConfig hc = new HostConfig(csPath);
        httpAccess = new HttpAccess(hc);
        final RequestState state = new RequestState();
        httpAccess.setState(state);
        mirrorGetConfig();
        if (this.mirrorprotocolversion == null) {
            throw new RuntimeException(
                    "Could not find the protocol version. Is the configuration correct?");

        }
        createTempTable();
        int errorCount = 0;
        try {
            for (final Iterable<Row> list : new ChunkedIterable<Row>(rows,
                    CatalogMover.rowsPerPost)) {
                final Post post = new Post();
                post.setUrl(csPath.getPath());
                post.addMultipartData("authusername", username);
                post.addMultipartData("authpassword", password);
                post.addMultipartData("tablename", ttTableName);
                post.addMultipartData("ftcmd", "replacerows");
                post.addMultipartData("tablekey", catalog.getTableKey());
                int i = 0;
                for (final Row element : list) {
                    addToPost(post, element, i);
                    i++;
                    if (monitor.isCanceled()) {
                        break;
                    }
                }
                log.trace(post.toString());

                monitor.subTask("Sending " + i + " rows to ContentServer.");

                final ResponseStatusCode status = execute(post);
                monitor.worked(i);

                log.trace(status.toString());
                do {
                    final int nCode = status.getErrorID();
                    if ((nCode != 0) && (nCode != 5))// ftErrors.replaceRowsID))
                    {
                        errorCount++;
                        log.error(status.toString());
                    }
                } while (status.setNextError());

            }
            commit();
        } finally {
            httpAccess.close();
        }

    }

    void addToPost(final Post post, final Row element, final int rowNum) {
        final int cols = element.getNumberOfColumns();
        for (int i = 0; i < cols; i++) {
            final Header header = element.getHeader(i);
            final String name = header.getHeader();
            String val = element.getData(i);
            if (val.length() == 0) {
                continue;
            }
            if (name.toLowerCase().startsWith("url")) {
                val = val.replace('\\', '/');
                final String folder = getFolder(val);
                if (folder != null) {
                    post.addMultipartData(name + "_folder" + rowNum, folder);
                }
                post.addMultipartData(name + rowNum, val, getAbsolutePath(val));
            } else {
                post.addMultipartData(name + rowNum, val);

            }

        }

    }

    private String getAbsolutePath(final String url) {
        return new File(catalog.getUploadPath(), url).toString();
    }

    private String getFolder(final String url) {
        final int index = url.lastIndexOf('/');
        if (index >= 0) {
            return url.substring(0, index);
        }
        return null;
    }

    public void setCatalog(final LocalCatalog catalog) {

        this.catalog = catalog;

    }

    public void setCsPath(final URI csPath) {
        this.csPath = csPath;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    protected void mirrorGetConfig() throws HttpAccessException, IOException {

        final Post post = new Post();
        post.setUrl(csPath.getPath());
        post.addMultipartData("authusername", username);
        post.addMultipartData("authpassword", password);
        post.addMultipartData("ftcmd", "mirrorgetconfig");

        final ResponseStatusCode status = execute(post);
        if (status.getResult()) {
            mirrorprotocolversion = status.getParams().get(
                    "mirrorprotocolversion");
        }
        log.debug(mirrorprotocolversion);

    }

    private ResponseStatusCode execute(final Post post)
            throws HttpAccessException, IOException {
        final ResponseStatusCode status = new ResponseStatusCode();
        final long t = System.currentTimeMillis();
        Response response = null;
        try {
            response = httpAccess.execute(post);

            log.trace("request took "
                    + Long.toString(System.currentTimeMillis() - t) + " ms.");
            log.trace(response);
            if (response.getStatusCode() == 200) {
                final String charset = response.getResponseEncoding();

                final InputStreamReader in = new InputStreamReader(response
                        .getResponseBodyAsStream(), charset);
                final char[] b = new char[1024];
                int c = 0;

                final StringBuilder out = new StringBuilder();

                while ((c = in.read(b)) != -1) {
                    out.append(b, 0, c);
                }

                final boolean foundStatus = status.setFromData(out.toString());

            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return status;

    }

    protected void createTempTable() throws HttpAccessException, IOException {
        final Post post = new Post();
        post.setUrl(csPath.getPath());
        post.addMultipartData("authusername", username);
        post.addMultipartData("authpassword", password);
        post.addMultipartData("ftcmd", "createtemptable");
        post.addMultipartData("tablekey", catalog.getTableKey());
        post.addMultipartData("aclList", "Browser,SiteGod");
        post.addMultipartData("parenttablename", catalog.getName());
        //tablename
        final ResponseStatusCode status = execute(post);
        if (status.getResult()) {
            ttTableName = status.getParams().get("tablename");
        }

    }

    protected void commit() throws HttpAccessException, IOException {
        final Post post = new Post();
        post.setUrl(csPath.getPath());
        post.addMultipartData("authusername", username);
        post.addMultipartData("authpassword", password);
        post.addMultipartData("ftcmd", "committables");
        post.addMultipartData("tablekey0", catalog.getTableKey());
        post.addMultipartData("aclList", "Browser,SiteGod");
        post.addMultipartData("childtablename0", ttTableName);
        post.addMultipartData("tablename0", catalog.getName());
        final ResponseStatusCode status = execute(post);
        if (status.getResult()) {
            log.info(status.toString());
        }

    }
}
