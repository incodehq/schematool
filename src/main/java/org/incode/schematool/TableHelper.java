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

import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.ComparisonChain;

import schemacrawler.schema.Table;

public class TableHelper {

    public static Set<Table> symmetricDifference(Set<Table> l1, Set<Table> l2) {
        Set<Table> result = new TreeSet<Table>();
        result.addAll(difference(l1, l2));
        result.addAll(difference(l2, l1));
        return result;
    }

    public static Set<Table> union(Set<Table> l1, Set<Table> l2) {
        Set<Table> result = new TreeSet<Table>();
        for (Table t1 : l1) {
            for (Table t2 : l2) {
                if (compareTo(t1, t2) == 0) {
                    result.add(t1);
                }
            }
        }
        return result;
    }

    public static Table find(Table table, Set<Table> list) {
        Set<Table> result = new TreeSet<Table>();
        for (Table t2 : list) {
            if (compareTo(table, t2) == 0) {
                return t2;
            }
        }
        return null;
    }

    public static Set<Table> difference(Set<Table> l1, Set<Table> l2) {
        Set<Table> result = new TreeSet<Table>();
        for (Table t1 : l1) {
            boolean found = false;
            for (Table t2 : l2) {
                if (compareTo(t1, t2) == 0) {
                    found = true;
                }
            }
            if (!found) {
                result.add(t1);
            }
        }
        return result;
    }

    public static int compareTo(Table table1, Table table2) {
        return ComparisonChain.start()
                .compare(table1.getSchema().getName(), table2.getSchema().getName())
                .compare(table1.getName(), table2.getName())
                .result();
    }

    public static String title(Table table){
        return new StringBuilder2()
                .add(table.toString(), 75)
                .result();
    }

}
