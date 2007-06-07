package com.fatwire.cs.catalogmover.mover;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RemoteCatalog {

    private final Log log = LogFactory.getLog(getClass());

    private final String tableName;

    /**
     * path to the export exportFile. This file holds the catalog.html file.
     * 
     */
    private final File exportFile;

    private final File uploadPath;

    /**
     * @param tableName
     * @param exportPath the directory to where the catalog must be exported
     */
    public RemoteCatalog(final String tableName, final File exportPath) {
        super();
        this.tableName = tableName;
        exportFile = new File(exportPath.getAbsoluteFile(), tableName + ".html");
        uploadPath = new File(exportPath.getAbsoluteFile(), tableName);
        log.debug("Catalog: " + exportFile.getAbsolutePath());

    }

    public File getExportFile() {
        return exportFile;
    }

    public File getUploadPath() {
        return uploadPath;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    protected void write(final String string, final File file)
            throws IOException {
        file.getParentFile().mkdirs();
        final FileWriter writer = new FileWriter(file);
        try {
            writer.write(string);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (final IOException e) {
                    //ignore
                }
            }
        }

    }

    protected void write(final byte[] bytes, final File file)
            throws IOException {
        file.getParentFile().mkdirs();
        final OutputStream writer = new FileOutputStream(file);
        try {
            writer.write(bytes);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (final IOException e) {
                    //ignore
                }
            }
        }

    }

    public void writeCatalog(final String content) throws IOException {
        write(content, getExportFile());
    }

    public void writeUrlField(final String content, final String urlField)
            throws IOException {
        write(content, new File(getUploadPath(), urlField));
    }

    public void writeUrlField(final byte[] content, final String urlField)
            throws IOException {
        write(content, new File(getUploadPath(), urlField));
    }

}
