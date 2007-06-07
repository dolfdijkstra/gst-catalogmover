package com.fatwire.cs.catalogmover.catalogs;

// This just provides a responsitory
// for html bits

public final class HTML {
    /**
     * bold a string
     *
     * @param s input
     * @return <b>input</b>
     */
    public static final String boldthis(final String s) {
        return ("<b>" + s + "</b>");
    }

    /**
     * table caption used by CS tools
     * DO NOT CHANGE THIS CODE
     *
     * @param data
     * @return string
     */
    public static final String caption(final String data) {
        return ("\n<CAPTION ALIGN=LEFT>" + data + "</CAPTION>\n");
    }

    /**
     * comment a string
     *
     * @param s
     * @return string
     */
    public static final String comment(final String s) {
        return ("<!--" + s + "-->");
    }

    /**
     * endpage
     *
     * @return String with end html tags
     */
    public static final String endpage() {
        return "</body>\n</html>";
    }

    /**
     * end a table def
     *
     * @return string
     */
    public static final String endtable() {
        return ("</TABLE>\n");
    }

    /**
     * make a product comment
     *
     * @return string
     */
    public static final String gencomment() {
        return ("\n<!-- FatWire's Headless CatalogManager -->\n");
    }

    /**
     * make a column heading
     *
     * @param data header data
     * @return string
     */
    public static final String header(final String data) {
        return ("<th>" + data + "</th>\n");
    }

    /**
     * make a row
     *
     * @param coldata header data
     * @return string
     */
    public static final String row(final String coldata) {
        return ("<tr>\n" + coldata + "</tr>\n");
    }

    /**
     * startpage
     *
     * @return String with start html tags
     */
    public static final String startpage(final String t) {
        return "<html>\n<head><title>" + t + "</title></head>\n<body>\n";
    }

    /**
     * make a table (begin)
     *
     * @param border
     * @param spacing
     * @param padding
     * @return string
     */
    public static final String table(final int border, final int spacing,
            final int padding) {
        final String s = Integer.toString(border);
        final String s2 = Integer.toString(padding);
        final String s3 = Integer.toString(spacing);

        return ("\n<TABLE BORDER=" + s + " CELLSPACING=" + s3 + " CELLPADDING="
                + s2 + ">\n");
    }

    /**
     * underline a string
     *
     * @param s input
     * @return <u>input</u>
     */
    public static final String underlinethis(final String s) {
        return ("<u>" + s + "</u>");
    }
}