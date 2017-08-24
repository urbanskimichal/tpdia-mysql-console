package pl.polsl.tpdia.sqlConsole.database

import gudusoft.gsqlparser.EDbVendor
import gudusoft.gsqlparser.TGSqlParser

import java.sql.SQLException

class SqlPreprocessor {

    TGSqlParser sqlParser
    GroupByTimeWindowSqlCommand groupByTimeWindowSqlCommand

    SqlPreprocessor() {
        sqlParser = new TGSqlParser(EDbVendor.dbvmysql)
        groupByTimeWindowSqlCommand = new GroupByTimeWindowSqlCommand()
    }

    private void vaidate(String query) throws SQLException {
        sqlParser.sqltext = query
        Integer result = sqlParser.parse()
        if (result == 0){
            println "Query ok"
        }else{
            throw new SQLException(sqlParser.getErrormessage())
        }
    }

    String process(String query) throws SQLException {
        String processedQuery = groupByTimeWindowSqlCommand.processQuery(query)
        println "Query: $processedQuery"
        vaidate(processedQuery)
        return processedQuery
    }
}
