package pl.polsl.tpdia.sqlConsole.database

import java.sql.SQLException
import java.util.regex.Pattern

class GroupByTimeWindowSqlCommand {

    //Command GBTW(CHAR tableName, CHAR timestampColumn, CHAR toSumColumn, INT timeWindow)

    private final Pattern commandPattern = ~/(GBTW\([a-zA-Z\d\s]*,[a-zA-Z\d\s]*,[a-zA-Z\d\s]*,[a-zA-Z\d\s]*\))/
    private final Pattern argsPattern = ~/([a-zA-Z\d\s]*,[a-zA-Z\d\s]*,[a-zA-Z\d\s]*,[a-zA-Z\d\s]*)/

    private String createCommandCodeWithParams(String tableName, String timestampColumn,
                                       String toSumColumn, Integer timeWindow) {
        String part1 = "select from_unixtime(round(groupedByTime.timekey * ($timeWindow*60))) as timestamp, groupedByTime.sum "
        String part2 = "from ( "
        String part3 = "select round(unix_timestamp($timestampColumn) / ($timeWindow * 60)) as timekey, sum($toSumColumn) as sum "
        String part4 = "from $tableName "
        String part5 = "group by timekey ) groupedByTime "
        return "($part1 $part2 $part3 $part4 $part5)"
    }

    private String replaceCommandWithCode(String query, String command) {
        List args = command.find(argsPattern).split(',')*.trim()
        if (args.size() != 4) {
            throw new SQLException('Command GBTW must have 4 arguments')
        }
        return query.replace(command, createCommandCodeWithParams(args[0], args[1], args[2], args[3] as Integer))
    }

    String processQuery(String query) {
        def commandOccurances = query.findAll(commandPattern)
        commandOccurances.each {
            query = replaceCommandWithCode(query, it)
        }
        return query
    }
}
