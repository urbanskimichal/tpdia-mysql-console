package pl.polsl.tpdia.sqlConsole.controller

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import pl.polsl.tpdia.sqlConsole.view.View
import pl.polsl.tpdia.sqlConsole.database.DatabaseManager

class Controller {

    DatabaseManager databaseManager
    View view

    Controller() {
        databaseManager = new DatabaseManager()
        view = new View(this)
    }

    String runQuery(String query) {
        def results = databaseManager.processQuery(query)
        return formatResponse(query, results)
    }

    private String formatResponse(String query, def results) {
        String niceResults = "Query:\n${query}\n\nResults:\n"
        if (results) {
            List<Map> rows = results as List<Map>
            niceResults += "Returned ${rows.size()} rows\n\n"
            results.each { row ->
                row.each { k, v ->
                    niceResults += "$v | "
                }
                niceResults += '\n'
            }
        }
        else {
            niceResults +=  'Returned 0 rows'
        }
        return niceResults
    }

    String getSettings() {
        return new JsonBuilder(databaseManager.dbConfig).toPrettyString()
    }

    String saveSettings(String settings) {
        try {
            def dbConfig = new JsonSlurper().parseText((settings))
            databaseManager.dbConfig = dbConfig
            databaseManager.connect()
            databaseManager.disconnect()
            return "New database configuration saved:\n${getSettings()}\n" +
                    "Connection test:\nSuccessful"
        }
        catch (Exception e) {
            return "Database config error:\n${e.message}"
        }
    }

    String getAboutMessage() {
        return '''
TPDiA MySql Console

The Console works with MySql database server.
 Database settings are located in File->Properties,
 but are not saved permanently. 

Grouping by time window:
 GBTW - Additional operator that enables grouping 
        rows by time window in minutes. 
                        
        GBTW(CHAR tableName, CHAR timestampColumn, CHAR toSumColumn, INT timeWindow)
        
        -tableName - name of a table to perform operation on
        -timestampColumn - name of a column with timestamp
        -toSumColumn - name of a column with value to be summed
        -timeWindow - a time window in minutes
        
        returns columns: 
        -timestamp - timestep with group beginning
        -sum - sum of grouped values from toSumColumn

        GBTW be used as regular table, example:
            Query:
            select * from GBTW(refuel , timestampCol, volume, 30) groupedRefuel;
            
            Results:
            2014-01-02 11:00:00.0 | 7518.37742164287 | 
            2014-01-03 13:30:00.0 | 7966.1468494107 | 
            ... 
                        
            
Silesian University of Technology        
Michał Urbański
Roksana Struczewska
2017
'''
    }
}
