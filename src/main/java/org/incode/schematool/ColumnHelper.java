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

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ComparisonChain;

import schemacrawler.schema.Column;

public class ColumnHelper {
    public static List<Column> symmetricDifference(List<Column> l1, List<Column> l2) {
        List<Column> result = new ArrayList<Column>();
        result.addAll(difference(l1, l2));
        result.addAll(difference(l2, l1));
        return result;
    }

    public static List<Column> difference(List<Column> l1, List<Column> l2) {
        List<Column> result = new ArrayList<Column>();
        for (Column t1 : l1) {
            boolean found = false;
            for (Column t2 : l2) {
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

    public static int compareTo(Column c1, Column c2){
        return ComparisonChain.start()
                .compare(c1.getName(), c2.getName())
                .compare(c1.getColumnDataType(), c2.getColumnDataType())
                .compare(c1.getSize(), c2.getSize())
                .compare(c1.getDecimalDigits(), c2.getDecimalDigits())
                .compare(c1.isNullable(), c2.isNullable())
                .result();
    }

    public static String title(Column c){
        return new StringBuilder2()
                .add(c.getParent().toString(), 75)
                .add(c.getName(), 50)
                .add(c.getColumnDataType().toString(), 10)
                .add(c.getWidth(), 10)
                .add(c.isNullable()?"":"No null", 10)
                .result();
    }

}
