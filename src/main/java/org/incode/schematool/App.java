/*
 * Copyright 2016 Jeroen van der Wal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.incode.schematool;

import java.util.List;
import java.util.Set;

import schemacrawler.schema.Column;
import schemacrawler.schema.Table;

public class App {
    public static void main(String[] args) {
        Database db1 = new Database("jdbc:sqlserver://ams-s-sql08:1433;instance=.;databaseName=estatio_blank", "estatio", "estatio", "estatio_blank");
        db1.doCrawl();

        Database db2 = new Database("jdbc:sqlserver://ams-s-sql08:1433;instance=.;databaseName=estatio_dev", "estatio", "estatio", "estatio_dev");
        db2.doCrawl();

        compare(db1, db2);
    }

    public static void compare(Database db1, Database db2) {
        Set<Table> l1 = db1.getTables();
        Set<Table> l2 = db2.getTables();

        System.out.println(StringBuilder2.fill("Tables not found on other ",75,"-"));
        for (Table table : TableHelper.symmetricDifference(l1, l2)) {
            System.out.println(TableHelper.title(table));
        }

        System.out.println(StringBuilder2.fill("Column differences ",75,"-"));
        for (Table table : TableHelper.union(l1, l2)) {
            //System.out.println(TableUtil.title(table));
            compare(TableHelper.find(table, l1), TableHelper.find(table, l2));
        }
    }

    public static void compare(Table t1, Table t2){
        List<Column> l1 = t1.getColumns();
        List<Column> l2 = t2.getColumns();

        for (Column column : ColumnHelper.symmetricDifference(l1, l2)) {
            System.out.println(ColumnHelper.title(column));
        }

    }


}
