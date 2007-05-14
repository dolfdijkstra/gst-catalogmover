package com.fatwire.cs.catalogmover.mover;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    private File uploadPath;

    /**
     * @param tableName
     * @param exportPath the directory to where the catalog must be exported
     */
    public RemoteCatalog(final String tableName, final File exportPath) {
        super();
        this.tableName = tableName;
        exportFile = new File(exportPath.getAbsoluteFile(), tableName + ".html");
        this.uploadPath = new File(exportPath.getAbsoluteFile(), tableName);
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

    protected void write(String string, File file) throws IOException {
        file.getParentFile().mkdirs();
        final FileWriter writer = new FileWriter(file);
        try {
            writer.write(string);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }

    }

    public void writeCatalog(String content) throws IOException {
        write(content, getExportFile());
    }

    public void writeUrlField(String content, String urlField)
            throws IOException {
        write(content, new File(this.getUploadPath(), urlField));
    }

}
