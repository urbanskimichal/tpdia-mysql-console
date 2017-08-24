package pl.polsl.tpdia.sqlConsole.database

import groovy.sql.Sql

import java.sql.SQLException

class DatabaseManager {

    def dbConfig = [
            url: 'jdbc:mysql://localhost:3306/tpdia',
            user: 'root',
            password: 'root',
            driver: 'com.mysql.jdbc.Driver'
    ]

    private Sql sql
    private SqlPreprocessor sqlPreprocessor

    DatabaseManager() {
        sqlPreprocessor = new SqlPreprocessor()
    }

    void connect() {
        sql = Sql.newInstance(
                dbConfig.url,
                dbConfig.user,
                dbConfig.password,
                dbConfig.driver
        )
    }

    void disconnect() {
        sql.close()
    }

    def processQuery(String query) {
        def result
        try {
            connect()
            String validQuery = sqlPreprocessor.process(query)
            result = queryDb(validQuery)
        }
        catch (SQLException e) {
            e.printStackTrace()
            result = null
        }
        disconnect()
        return result
    }

    private def queryDb(String query) {
        return sql.rows(query)
    }
}
