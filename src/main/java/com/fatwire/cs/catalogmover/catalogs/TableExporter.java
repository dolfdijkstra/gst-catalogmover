package com.fatwire.cs.catalogmover.catalogs;

import static com.fatwire.cs.catalogmover.catalogs.TableData.COLTYPEBINARY;
import static com.fatwire.cs.catalogmover.catalogs.TableData.COLTYPEDATE;
import static com.fatwire.cs.catalogmover.catalogs.TableData.COLTYPENUMBER;
import static com.fatwire.cs.catalogmover.catalogs.TableData.COLTYPETEXT;
import static com.fatwire.cs.catalogmover.catalogs.TableData.COLTYPEUNKNOWN;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TableExporter {
    public static final String falsestr = "false";

    public static final String truestr = "true";

    public static final String brk = "<br>\n";

    public static final String foreign = "fgn";

    public static final String text = "(text)";

    public static final String number = "(integer)";

    public static final String doublestr = "(double)";

    public static final String datetime = "(date/time)";

    public static final String binarydata = "(binary)";

    public static final String unknown = "Unknown argument:";

    final private TableData tableData;

    public TableExporter(final TableData tableData) {
        super();
        this.tableData = tableData;
    }

    /**
     * Export to HTML
     * 
     * @param SelectedRows
     *            vector of row indicess to include or specify null for all rows
     * @throws IOException 
     */
    public StringBuffer exportHTML(final Iterable<Row> selectedRows,
            final String sDBType, final boolean bRowSummary) throws IOException {
        final StringWriter oStringWriter = new StringWriter();
        final PrintWriter oPrintWriter = new PrintWriter(oStringWriter);


            // this entry writes the page wrapping stuff,
            // the one that writes to a stream has that
            // already
            oPrintWriter.print(HTML.startpage(HTML.gencomment()));
            exportHTML(selectedRows, sDBType, bRowSummary, oPrintWriter);
            oPrintWriter.print(HTML.endpage());
            oPrintWriter.flush();
            return new StringBuffer(oStringWriter.toString());
    }

    /**
     * Export to HTML
     * 
     * @param selectedRows
     *            vector of row indicess to include or specify null for all rows
     */
    public void exportHTML(final Iterable<Row> selectedRows,
            final String dbType, final boolean rowSummary,
            final PrintWriter printWriter) throws IOException {
        String type;
        //
        // Create an identifier based on the table name
        //
        final String wrapperUniqueID = Long.toString(tableData.getTableName()
                .hashCode() & 0xFFFF);
        printWriter.println();
        printWriter.print(TableExporter.brk);

        // ---
        // Set the version id of the table format
        printWriter.print(TableParser.m_sStartTableVersionTag);
        printWriter.print(TableData.HTMLTABLEVERSION);
        printWriter.print(TableParser.m_sEndTableVersionTag);
        printWriter.println();

        // Set the unique data wrapper value
        printWriter.print(TableParser.m_sWrapperStartTag);
        printWriter.print(TableParser.m_sWRAPID);
        printWriter.print(wrapperUniqueID);
        printWriter.print(TableParser.m_sWrapperEndTag);
        printWriter.println();
        final String wrapperStart = TableParser.m_sWRAPID + wrapperUniqueID
                + TableParser.m_sStartID;
        final String wrapperEnd = TableParser.m_sWRAPID + wrapperUniqueID
                + TableParser.m_sEndID;

        printWriter.println(HTML.comment(getTableName()));
        printWriter.print(HTML.table(1, 0, 2));
        printWriter.print(HTML.caption(getTableName()));
        printWriter.println();

        type = getTableType();
        if (type == null) {
            type = TableExporter.foreign;
        }
        printWriter.print(TableParser.m_sStartTableTypeTag);
        printWriter.print(type);
        printWriter.print(TableParser.m_sEndTableTypeTag);
        printWriter.println();

        if (isTracked()) {
            printWriter.print(TableParser.m_sStartTableTrackedTag);
            printWriter.print(TableExporter.truestr);
            printWriter.print(TableParser.m_sEndTableTrackedTag);
            printWriter.println();
        }

        printWriter.print(TableParser.m_sStartDBTypeTag);
        printWriter.print(dbType);
        printWriter.print(TableParser.m_sEndDBTypeTag);
        printWriter.println();

        printWriter.print("<tr>");
        printWriter.println();
        final int isz = getColumnCount();
        for (int i = 0; i < isz; i++) {
            switch (getType(i)) {
            case COLTYPETEXT:
                type = TableExporter.text;
                break;

            case COLTYPENUMBER:
                type = TableExporter.number;
                break;

            case COLTYPEDATE:
                type = TableExporter.datetime;
                break;

            case COLTYPEBINARY:
                type = TableExporter.binarydata;
                break;

            case COLTYPEUNKNOWN:
                type = TableExporter.unknown;
                break;

            default:
                type = null;
                break;
            }

            // table info
            String h = getHeader(i);
            h = HTML.underlinethis(HTML.boldthis(h));
            String s = getSchema(i);
            if (type != null) {
                s += (";" + type);
            }

            printWriter.print("\t");
            printWriter.print(HTML.header(h + HTML.comment(s)));
        }
        printWriter.println("</tr>");

        final int numColumns = getColumnCount();
        for (Row row : selectedRows) {

            printWriter.println("<tr>");

            for (int colNum = 0; colNum < numColumns; colNum++) {
                printWriter.print("\t<td>");
                printWriter.print(wrapperStart);
                printWriter.print(row.getData(colNum));
                printWriter.print(wrapperEnd);
                printWriter.println("</td>");
            }
            printWriter.println("</tr>");
        }
        printWriter.print(HTML.endtable());

        if (rowSummary) {
            printWriter.print(TableExporter.brk);
            printWriter.print(getRowCount());
            printWriter.println();
        }
    }

    private boolean isTracked() {
        return tableData.isTracked();
    }

    private int getRowCount() {
        return tableData.getRowCount();
    }

//    private String getCell(final int rowNum, final int colNum) {
//        return tableData.getCell(rowNum, colNum).getCell();
//    }

    private String getSchema(final int i) {
        return tableData.getHeaders().get(i).getSchema();
    }

    private String getHeader(final int i) {
        return tableData.getHeaders().get(i).getName();
    }

    private int getType(final int i) {
        return tableData.getHeaders().get(i).getType();
    }

    private int getColumnCount() {
        return tableData.getHeaders().size();
    }

    private String getTableType() {
        return tableData.getTableType();
    }

    private String getTableName() {
        return tableData.getTableName();
    }

}
