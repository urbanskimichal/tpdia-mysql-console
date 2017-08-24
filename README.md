# tpdia-mysql-console
Console that adds grouping in time window operation to SQL.

##Usage

### Run code
    
```./gradlew runApp'''```

### Build executable jar
    
```./gradlew shaddowjar```
    
    
# About

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
Michał Urbański,
Roksana Struczewska
2017