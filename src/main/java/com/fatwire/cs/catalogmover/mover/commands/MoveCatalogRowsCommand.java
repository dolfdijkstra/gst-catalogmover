package com.fatwire.cs.catalogmover.mover.commands;

import java.io.File;

import com.fatwire.cs.catalogmover.catalogs.Header;
import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.util.ChunkedIterable;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;
import com.fatwire.cs.catalogmover.util.StringUtils;
import com.fatwire.cs.core.http.Post;

public class MoveCatalogRowsCommand extends AbstractCatalogMoverCommand
        implements CatalogMoverCommand {
    private static final int ROWSPERPOST = 20;

    private String tableName;

    private String tableKey;

    private final Iterable<Row> rows;

    private final File uploadPath;

    private final IProgressMonitor monitor;

    public MoveCatalogRowsCommand(BaseCatalogMover cm, String tableName,
            String tableKey, final File uploadPath, final Iterable<Row> rows,
            IProgressMonitor monitor) {
        super(cm);
        if (StringUtils.emptyString(tableName)) {
            throw new NullPointerException("tableName is null or blank");
        }
        if (StringUtils.emptyString(tableKey)) {
            throw new NullPointerException("tableKey is null or blank");
        }
        if (rows == null) {
            throw new NullPointerException("rows is null");
        }

        this.tableName = tableName;
        this.tableKey = tableKey;
        this.uploadPath = uploadPath;
        this.rows = rows;
        this.monitor = monitor;
    }

    public void execute() throws CatalogMoverException {
        move();
    }

    /**
     * rows per post hardcoded to 20
     * 
     * @param rows
     * @param monitor
     * @throws CatalogMoverException 
     */
    protected void move() throws CatalogMoverException {
        int errorCount = 0;

        for (final Iterable<Row> list : new ChunkedIterable<Row>(rows,
                ROWSPERPOST)) {
            final Post post = this.prepareNewPost();
            post.addMultipartData("ftcmd", "replacerows");
            post.addMultipartData("tablename", tableName);
            post.addMultipartData("tablekey", tableKey);
            int i = 0;
            for (final Row row : list) {
                addToPost(post, row, i);
                i++;
                if (monitor.isCanceled()) {
                    break;
                }
            }
            log.trace(post.toString());

            monitor.subTask("Sending " + i + " rows to ContentServer.");

            final ResponseStatusCode status = cm.execute(post);
            monitor.worked(i);
            if (log.isTraceEnabled())
                log.trace(status.toString());
            if (status.getResult()) {
                do {
                    final int nCode = status.getErrorID();
                    if ((nCode != 0) && (nCode != 5))// ftErrors.replaceRowsID))
                    {
                        errorCount++;
                        log.error(status.toString());
                        throw new ReplaceRowException(status.toString());
                    }
                } while (status.setNextError());
            }else {
                throw new ReplaceRowException(status.toString());
            }

        }

    }

    /**
     * @param post
     * @param row
     * @param rowNum
     */
    void addToPost(final Post post, final Row row, final int rowNum) {
        final int cols = row.getNumberOfColumns();
        for (int i = 0; i < cols; i++) {
            final Header header = row.getHeader(i);
            final String name = header.getHeader();
            String val = row.getData(i);
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
        //        if (cm.hasListeners()) {
        //            cm.fireEvent(new RowAddedEvent(this, row));
        //        }

    }

    private String getAbsolutePath(final String url) {
        return new File(uploadPath, url).toString();
    }

    private String getFolder(final String url) {
        final int index = url.lastIndexOf('/');
        if (index >= 0) {
            return url.substring(0, index);
        }
        return null;
    }

}
