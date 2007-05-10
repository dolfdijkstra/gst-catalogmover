/*
 * $Logfile: /branches/CSv610/ContentServer/COM/FutureTense/Util/TableParser.java $ $Revision: 16 $ $Date: 8/03/04 5:15p $
 *
 * Copyright (c) 2003, 2004 FatWire Corporation, All Rights Reserved.
 * Copyright (c) 2002, 2003 divine, inc., All Rights Reserved.
 * Copyright (c) 1999, 2000, 2001 Open Market, Inc., All Rights Reserved.
 * Copyright (c) 1999 FutureTense, Inc., All Rights Reserved.
 */
package com.fatwire.cs.catalogmover.catalogs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TableParser {
    final Log log = LogFactory.getLog(TableParser.class);

    // static initialization
    // !!! NOTE - all tags must be lower case, only string being compared to
    // is made tolower !!!
    protected static final String m_sBeginTag = "<";

    protected static final String m_sEndTag = ">";

    protected static final String m_sBeginTable = "<table";

    protected static final String m_sEndTable = "</table>";

    protected static final String m_sStartRowTag = "<tr>";

    protected static final String m_sEndRowTag = "</tr>";

    protected static final String m_sStartColumnTag = "<td>";

    protected static final String m_sEndColumnTag = "</td>";

    protected static final String m_sStartHeaderColumnTag = "<th>";

    protected static final String m_sEndHeaderColumnTag = "</th>";

    protected static final String m_sStartBoldTag = "<b>";

    protected static final String m_sEndBoldTag = "</b>";

    protected static final String m_sStartTableNameTag = "<caption align=left>";

    protected static final String m_sEndTableNameTag = "</caption>";

    protected static final String m_sStartSchemaTag = "<!--";

    protected static final String m_sEndSchemaTag = "-->";

    protected static final String m_sStartTableTypeTag = "<!--tt:";

    protected static final String m_sEndTableTypeTag = ":-->";

    protected static final String m_sStartTableTrackedTag = "<!--track:";

    protected static final String m_sEndTableTrackedTag = ":-->";

    protected static final String m_sStartDBTypeTag = "<!--dbt:";

    protected static final String m_sEndDBTypeTag = ":-->";

    protected static final String m_sTDID = " ftcs=\"ftcs\">";

    protected static String m_sStartColumnTag2; // note NOT final

    protected static String m_sEndColumnTag2; // note NOT final

    protected static final String m_sStartTableVersionTag = "<!--ver:";

    protected static final String m_sEndTableVersionTag = ":-->";

    protected static final String m_sWRAPID = "<ft";

    protected static final String m_sStartID = "b/>";

    protected static final String m_sEndID = "e/>";

    protected static final String m_sWrapperStartTag = "<!--wrapper:";

    protected static final String m_sWrapperEndTag = ":-->";

    protected TableData m_TableData;

    protected int m_nTableVersion;

    private static final String text = "(text)";

    private static final String number = "(integer)";

    private static final String datetime = "(date/time)";

    private static final String binarydata = "(binary)";

    public TableParser() {
        m_TableData = null;
        m_nTableVersion = 0;
    }

    /**
     * Parse an HTML table from input
     */
    public TableData parseHTML(final String sBuffer) {
        int nTableStart;
        int nTableEnd;
        // int nBufferPos;
        String sTable;
        IntStatus ibrBufferPos;
        String sRow;
        String sTableName;
        String sTableType;
        String sDBType;
        String sWrapper;
        int nRow = 0;

        ibrBufferPos = new IntStatus(0);
        // ---
        // Fetch the version of the table'
        final String sVersion = extractBetweenTokens(sBuffer,
                TableParser.m_sStartTableVersionTag,
                TableParser.m_sEndTableVersionTag, ibrBufferPos);

        // ---
        // Save the version of the table being parsed. May be useful
        // someday to ease parsing.
        try {
            m_nTableVersion = Integer.parseInt(sVersion);
        } catch (final Exception e) {
        } // if not found, oh well

        // ---
        // If the version is recent enough, try to find the
        // wrapper string that was used
        if (m_nTableVersion >= TableData.BASETMLVERSION) {
            sWrapper = extractBetweenTokens(sBuffer,
                    TableParser.m_sWrapperStartTag,
                    TableParser.m_sWrapperEndTag, ibrBufferPos);
            // if we found a wrapper, redefine the table
            // column strings we will search for
            // NOTE that if no wrapper is found, these will be
            // left as standard <td> and </td> tags
            if (sWrapper.length() > 0) {
                TableParser.m_sStartColumnTag2 = sWrapper
                        + TableParser.m_sStartID;
                TableParser.m_sEndColumnTag2 = sWrapper + TableParser.m_sEndID;
            } else {
                log
                        .error("TableParser version/wrapper mismatch - required wrapper not found");
                TableParser.m_sStartColumnTag2 = TableParser.m_sStartColumnTag;
                TableParser.m_sEndColumnTag2 = TableParser.m_sEndColumnTag;
            }

        } else {
            TableParser.m_sStartColumnTag2 = TableParser.m_sStartColumnTag;
            TableParser.m_sEndColumnTag2 = TableParser.m_sEndColumnTag;
        }

        nTableStart = findTable(sBuffer);
        if (nTableStart == -1) {
            return null;
        }

        m_TableData = new TableData();
        ibrBufferPos.value = nTableStart;

        nTableEnd = findTokenReverse(sBuffer, TableParser.m_sEndTable);

        if (nTableEnd == -1) {
            m_TableData = null;
            return null;
        }

        // ---
        // make a string that is just contents of table (without its
        // start/end tags
        sTable = sBuffer.substring(nTableStart, nTableEnd);
        ibrBufferPos.value = 0;

        // ---
        // Fetch the table name
        sTableName = extractBetweenTokens(sTable,
                TableParser.m_sStartTableNameTag,
                TableParser.m_sEndTableNameTag, ibrBufferPos);
        if (sTableName == null) {
            log.error("Table Name not found in table");
            m_TableData = null;
            return null;
        } else {
            m_TableData.setTableName(sTableName.trim());
        }

        // fetch the table type
        sTableType = extractBetweenTokens(sTable,
                TableParser.m_sStartTableTypeTag,
                TableParser.m_sEndTableTypeTag, ibrBufferPos);
        if (sTableType != null) {
            m_TableData.setTableType(sTableType);
        }

        sDBType = extractBetweenTokens(sTable, TableParser.m_sStartDBTypeTag,
                TableParser.m_sEndDBTypeTag, ibrBufferPos);
        if (sDBType != null) {
            m_TableData.setDatabaseType(sDBType);
        }

        // ---
        // get the first row out...
        sRow = extractBetweenTokens(sTable, TableParser.m_sStartRowTag,
                TableParser.m_sEndRowTag, ibrBufferPos);

        // ---
        // if not even a first row, nothing we can do here...
        if (sRow == null) {
            m_TableData = null;
            return null;
        }

        if (parseHeaderRow(sRow)) {
        } else {
            // ---
            // if no headers, we don't want the buffer position updated
            // past the first row
            ibrBufferPos.value = 0;
        }

        // ---
        // process each table row
        while (true) {
            // this must be the safe version for special
            // wrapper tags, so that </tr> inside a
            // column is not picked up. It may or
            // not make any difference parsing a
            // regular table, in most cases not.
            sRow = extractBetweenTokensSafe(sTable, TableParser.m_sStartRowTag,
                    TableParser.m_sEndRowTag, ibrBufferPos);

            if (sRow != null) {
                parseRow(sRow, nRow);
                nRow++;
            } else {
                break;
            }
        }

        return m_TableData;
    }

    protected int findToken(final String sBuffer, final String sToken) {
        int nFound;

        // ---
        // look for token
        nFound = caselessFindStr(sBuffer, sToken);

        return nFound;
    }

    protected int findTokenReverse(final String sBuffer, final String sToken) {
        int nFound;

        // ---
        // look for token starting from end
        nFound = caselessFindStrReverse(sBuffer, sToken);

        return nFound;
    }

    protected int findTable(final String sBuffer) {
        int nFound;

        // ---
        // look for the table start
        nFound = caselessFindStr(sBuffer, TableParser.m_sBeginTable);

        if (nFound == -1) {
            return -1;
        }

        // ---
        // look for table end
        nFound = caselessFindStr(sBuffer, TableParser.m_sEndTag,
                (nFound + TableParser.m_sBeginTable.length()));
        if (nFound == -1) {
            return -1;
        }

        // ---
        // return index into buffer past end of table tag
        return (nFound + TableParser.m_sEndTag.length());
    }

    protected void parseRow(final String sBuffer, final int nRow) {
        int nColumn = -1;
        // int nEnd = 0;
        String sCell;
        String sData;
        IntStatus ibrEnd;

        // ---
        // So that data can be modified...
        sData = new String(sBuffer);
        ibrEnd = new IntStatus(0);

        sCell = extractBetweenTokens(sData, TableParser.m_sStartColumnTag2,
                TableParser.m_sEndColumnTag2, ibrEnd);

        // log the warning tell sCell has wrong wrapper
        if (sCell == null) {
            log
                    .warn("TableParser : Error Processing the record  "
                            + sData
                            + ". \nIt may be this row has wrong wrapper\n"
                            + "All the record after this will be ignored. Please correct the wrapper and import back the catalog");
        }

        while (sCell != null) {
            nColumn++;

            // ---
            // now add the row data to the TableData (remember that?
            // that's what this is all for...
            m_TableData.addCell(nRow, nColumn, sCell);

            sCell = extractBetweenTokens(sData, TableParser.m_sStartColumnTag2,
                    TableParser.m_sEndColumnTag2, ibrEnd);
        }
    }

    protected String parseSchema(final String sSchema,
            final IntStatus ibrColType) {
        int nColType = TableData.COLTYPEUNKNOWN;
        int nSep;
        String sSchemaName;

        nSep = sSchema.indexOf(';');
        if (nSep != -1) {
            sSchemaName = sSchema.substring(0, nSep);

            // ---
            // Is there any more to the string after the schema
            // name?
            try {
                final String sType = sSchema.substring(nSep + 1).trim();
                if (sType.equalsIgnoreCase(TableParser.text)) {
                    nColType = TableData.COLTYPETEXT;
                } else if (sType.equalsIgnoreCase(TableParser.number)) {
                    nColType = TableData.COLTYPENUMBER;
                } else if (sType.equalsIgnoreCase(TableParser.datetime)) {
                    nColType = TableData.COLTYPEDATE;
                } else if (sType.equalsIgnoreCase(TableParser.binarydata)) {
                    nColType = TableData.COLTYPEBINARY;
                }
            } catch (final Exception e) {
            }
        } else {
            sSchemaName = sSchema;
        }

        // ---
        // Return the column type in the IntStatus
        ibrColType.value = (nColType);

        return sSchemaName;

    }

    protected boolean parseHeaderRow(final String sRow) {
        int nColumn = -1;
        // int nEnd = 0;
        String sHeader;
        String sSchema;
        IntStatus ibrBogus;
        IntStatus ibrEnd;

        // ---
        // see if there are any real headers here
        ibrEnd = new IntStatus(0);
        sHeader = extractBetweenTokens(sRow,
                TableParser.m_sStartHeaderColumnTag,
                TableParser.m_sEndHeaderColumnTag, ibrEnd);

        while (sHeader != null) {
            nColumn++;

            // ---
            // Extract the schema from the header record.
            ibrBogus = new IntStatus(0);
            sSchema = extractBetweenTokens(sHeader,
                    TableParser.m_sStartSchemaTag, TableParser.m_sEndSchemaTag,
                    ibrBogus);

            if (sSchema == null) {
                sSchema = "";
            }

            final IntStatus ibrColType = new IntStatus(TableData.COLTYPEUNKNOWN);
            sSchema = parseSchema(sSchema, ibrColType);

            // ---
            // header includes all the goop between tokens
            // get rid of any tokens found, leaving just the text.
            sHeader = getRealText(sHeader);

            // ---
            // now add the header data to the TableData
            m_TableData.addHeader(nColumn, sHeader, sSchema, ibrColType.value);

            sHeader = extractBetweenTokens(sRow,
                    TableParser.m_sStartHeaderColumnTag,
                    TableParser.m_sEndHeaderColumnTag, ibrEnd);

        } // while

        // ---
        // if no real headers, use the convention of any bold columns in first
        // row for headers.
        if (nColumn == -1) {
            if (caselessFindStr(sRow, TableParser.m_sStartBoldTag) != -1) {
                sHeader = extractBetweenTokens(sRow,
                        TableParser.m_sStartColumnTag,
                        TableParser.m_sEndColumnTag, ibrEnd);
                while (sHeader != null) {
                    nColumn++;

                    // ---
                    // Extract the schema from the header record.
                    ibrBogus = new IntStatus(0);
                    sSchema = extractBetweenTokens(sHeader,
                            TableParser.m_sStartColumnTag,
                            TableParser.m_sEndHeaderColumnTag, ibrBogus);

                    if (sSchema == null) {
                        sSchema = "";
                    }

                    final IntStatus ibrColType = new IntStatus(
                            TableData.COLTYPEUNKNOWN);
                    sSchema = parseSchema(sSchema, ibrColType);

                    // ---
                    // header includes all the goop between tokens
                    // get rid of any tokens found, leaving just the text.
                    sHeader = getRealText(sHeader);

                    // ---
                    // now add the header data to the CTableData
                    m_TableData.addHeader(nColumn, sHeader, sSchema,
                            ibrColType.value);

                    sHeader = extractBetweenTokens(sRow,
                            TableParser.m_sStartColumnTag,
                            TableParser.m_sEndColumnTag, ibrEnd);
                } // while
            }
        }

        return (nColumn > -1);
    }

    // ///////////////////////////////////////////////////////////////////////////
    // this assumes find string is ALL LOWER CASE (since presumably
    // it is predefined token)
    protected int caselessFindStr(final String sString, final String sFind) {
        return caselessFindStr(sString, sFind, 0);
    }

    protected int caselessFindStr(final String sString, final String sFind,
            final int nBegin) {
        int index = nBegin;
        int len = sString.length();
        int j;
        final int len2 = sFind.length();

        int foundit = -1;

        // subtract off the length of the find string
        // since once index reaches beyond that point,
        // sFind can't be found; prevents k from falling
        // off the back
        try {
            len -= len2;
            while (index <= len) {
                int k = index;
                boolean found = true;
                for (j = 0; j < len2; j++, k++) {
                    final char c = Character.toLowerCase(sString.charAt(k));
                    if (c != sFind.charAt(j)) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    foundit = index;
                    break;
                }
                index = k + 1; // is this always safe? step over failed
            }
        } catch (final Exception e) {
        }
        return foundit;
    }

    // ///////////////////////////////////////////////////////////////////////////
    // this assumes find string is ALL LOWER CASE (since presumably
    // it is predefined token)
    protected int caselessFindStrReverse(final String sString,
            final String sFind) {
        return caselessFindStrReverse(sString, sFind, -1);
    }

    protected int caselessFindStrReverse(final String sString,
            final String sFind, int nBegin) {
        final int findLen = sFind.length();
        int i, j;

        if (nBegin == -1) {
            nBegin = sString.length() - 1;
        }

        try {
            j = findLen - 1;
            for (i = nBegin; i >= 0; i--) {
                final char c = Character.toLowerCase(sString.charAt(i));
                if (c == sFind.charAt(j)) {
                    if (j == 0) {
                        return i;
                    }
                    j--;
                } else {
                    j = findLen - 1;
                }
            }
        } catch (final Exception e) {
        }

        return -1;
    }

    protected String extractBetweenTokens(final String sBuffer,
            final String sHeadToken, final String sTailToken,
            final IntStatus ibrStart) {
        String sExtracted = null;
        int head, tail;

        // ---
        // look for head tag
        head = caselessFindStr(sBuffer, sHeadToken, ibrStart.value);

        if (head == -1) {
            return sExtracted;
        }

        // ---
        // look for end
        tail = caselessFindStr(sBuffer, sTailToken,
                (head + sHeadToken.length()));

        if (tail == -1) {
            return sExtracted;
        }

        ibrStart.value = (tail + sTailToken.length());

        final int nFirstChar = head + sHeadToken.length();
        final int nLastChar = nFirstChar + tail - head - sHeadToken.length();

        sExtracted = sBuffer.substring(nFirstChar, nLastChar);

        return sExtracted;
    }

    // make sure the tokens we find are not inside the
    // currently defined column tokens
    protected String extractBetweenTokensSafe(final String sBuffer,
            final String sHeadToken, final String sTailToken,
            final IntStatus ibrStart) {
        String sExtracted = null;
        int head, tail, lastStartTag, lastEndTag;

        while (true) {
            // ---
            // look for head tag
            head = caselessFindStr(sBuffer, sHeadToken, ibrStart.value);

            if (head == -1) {
                return sExtracted;
            }

            // look back for column end tag
            lastEndTag = caselessFindStrReverse(sBuffer,
                    TableParser.m_sEndColumnTag2, head);

            // look back for column start tag
            lastStartTag = caselessFindStrReverse(sBuffer,
                    TableParser.m_sStartColumnTag2, head);

            // make sure that the end tag is closer than the start tag,
            // otherwise we are in a column
            if (((lastEndTag > -1) && (lastStartTag > -1))
                    && (lastStartTag > lastEndTag)) {
                ibrStart.value = caselessFindStr(sBuffer,
                        TableParser.m_sEndColumnTag2, head);
                if (ibrStart.value == -1) {
                    return sExtracted;
                }
                continue;
            } else {
                break;
            }
        }

        // ---
        // look for end
        ibrStart.value = head + sHeadToken.length();
        while (true) {
            tail = caselessFindStr(sBuffer, sTailToken, ibrStart.value);

            if (tail == -1) {
                return sExtracted;
            }

            // look back for column end tag
            lastEndTag = caselessFindStrReverse(sBuffer,
                    TableParser.m_sEndColumnTag2, tail);

            // look back for column start tag
            lastStartTag = caselessFindStrReverse(sBuffer,
                    TableParser.m_sStartColumnTag2, tail);

            // make sure that the end tag is closer than the start tag,
            // otherwise we are in a column
            if (((lastEndTag > -1) && (lastStartTag > -1))
                    && (lastStartTag > lastEndTag)
                    && (lastStartTag > ibrStart.value)) {
                ibrStart.value = caselessFindStr(sBuffer,
                        TableParser.m_sEndColumnTag2, tail);
                if (ibrStart.value == -1) {
                    return sExtracted;
                }
                continue;
            } else {
                break;
            }
        }

        ibrStart.value = (tail + sTailToken.length());

        final int nFirstChar = head + sHeadToken.length();
        final int nLastChar = nFirstChar + tail - head - sHeadToken.length();

        sExtracted = sBuffer.substring(nFirstChar, nLastChar);

        return sExtracted;
    }

    // ///////////////////////////////////////////////////////////////////////////
    // remove any tokens found within this buffer
    // meant for removing markup in and around real text
    protected String getRealText(final String sBuffer) {
        int inToken = 0;
        int i;
        final StringBuffer sbOutput = new StringBuffer();

        for (i = 0; i < sBuffer.length(); i++) {
            final char cTemp = sBuffer.charAt(i);

            if (cTemp == '<') {
                inToken++;
            } else if (cTemp == '>') {
                inToken--;
            } else if (inToken <= 0) {
                sbOutput.append(cTemp);
            }
        }

        return sbOutput.toString();
    }
}