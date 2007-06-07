package com.fatwire.cs.catalogmover.catalogs;

public class SampleTableData {

    TableData create() {
        TableData table = new TableData();
        table.setDatabaseType("sample");
        table.setTableName("TestCatalog");
        table.setTableType("no");
        

        return table;
    }

}
