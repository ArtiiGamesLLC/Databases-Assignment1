_**To use this script to regenerate the SQL Database I have created, please follow these steps:**_

____________________________________________________________________________________________________________________________    
_1. Open the Windows (Or other OS) command line, and change the directory with a "cd" command to the location of your machine's MySQL.exe file. In my case, this command was:_ 
 ```cd C:\Program Files\MySQL\MySQL Server 8.0\bin```

_2. In the Windows command line, create the database you would like to run my SQL commands on. If you were calling this database "Assignment", you would run:_
```mysql -u<user> -p<password> -h127.0.0.1 -e "CREATE DATABASE Assignment"```

_3. In the Windows command line, copy the data in my .SQL file into your new database by typing the following command, but replacing the filepath at the end with the location of the .SQL file you downloaded from here. In my case, this was:_
```mysql -u<user> -p<password> -h127.0.0.1 --database=Assignment < C:\Users\Ryan\Desktop\Assignment1.sql```
                                                      
____________________________________________________________________________________________________________________________                                                     
**Having followed these steps, you should now have a fully complete example of the Paper Review database described in the posted assignment guidelines page.** Open your MySQL terminal and change the active database to the one you copied this script into. There should be multiple sample entries and 5 tables imported by this operation.

The database implements the Paper Review system described in question 3.34 in the class book. The diagram of this database which was created for a previous assignment was a great help in understanding the necessary commands to build the database. The diagram I created, as well as the question this is based on, can be found here: https://imgur.com/a/sCIc8Np
