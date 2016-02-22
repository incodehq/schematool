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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import com.google.common.collect.ComparisonChain;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;
import schemacrawler.schemacrawler.DatabaseConnectionOptions;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.utility.SchemaCrawlerUtility;

public class Database {

    private String connectionUrl;
    private String userName;
    private String password;
    private String database;

    private Set<Table> tables;

    public Database(String connectionUrl, String userName, String password, String database) {
        this.connectionUrl = connectionUrl;
        this.userName = userName;
        this.password = password;
        this.database = database;
    }

    public Set<Table> getTables() {
        return tables;
    }

    public void doCrawl()  {
        // Create a database connection
        final DataSource dataSource;
        try {
            dataSource = new DatabaseConnectionOptions(connectionUrl);
        } catch (SchemaCrawlerException e) {
            e.printStackTrace();
            return;
        }
        final Connection connection;
        try {
            connection = dataSource.getConnection(userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Create the options
        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        // Set what details are required in the schema - this affects the
        // time taken to crawl the schema
        options.setSchemaInfoLevel(SchemaInfoLevel.standard());
        options.setRoutineInclusionRule(new ExcludeAll());
        options.setSchemaInclusionRule(new RegularExpressionInclusionRule("{database}\\.dbo||{database}\\.isis.*".replace("{database}", database)));

        // Get the schema definition
        final Catalog catalog;
        try {
            catalog = SchemaCrawlerUtility.getCatalog(connection, options);
        } catch (SchemaCrawlerException e) {
            e.printStackTrace();
            return;
        }

        tables = new TreeSet<Table>(){
            @Override public Comparator<? super Table> comparator() {
                return new Comparator<Table>() {
                    public int compare(final Table o1, final Table o2) {
                        return ComparisonChain.start()
                                .compare(o1.getSchema().getName(), o2.getSchema().getName())
                                .compare(o1.getName(), o2.getName())
                                .result();
                    }
                };
            }
        };

        for (final Schema schema: catalog.getSchemas())
        {
            for (final Table table: catalog.getTables(schema))
            {
                if (table instanceof View)
                {
                }
                else
                {
                    tables.add(table);
                }
            }
        }

    }

}
