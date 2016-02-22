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

public class StringBuilder2 {

    private final String string;

    StringBuilder2(String str){
        string = str;
    }

    StringBuilder2(){
        string = "|";
    }

    public StringBuilder2 add(String str, int length) {
        String fill = fill(str, length, " ");
        return new StringBuilder2(string + fill +"|");
    }

    public static String fill(String str, int length, String with) {
        while (str.length() < length) {
            str = str + with;
        }
        return str;
    }

    public String result() {
        return string;
    }

}
