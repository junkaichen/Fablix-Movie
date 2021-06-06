- # General
    - #### Team#: 54
    
    - #### Names: Jacob Martinez, JunkaiChen
    
    - #### Project 5 Video Demo Link: https://drive.google.com/file/d/1I2HhraUoDmSMR1TCPUfomjtWv42t9hj1/view?usp=sharing

    - #### Project 5 URL Links for Fabflix:
        aws-lb : http://18.216.72.64:80/CS122APROJECT1-1.0-SNAPSHOT/login.html
        master: http://18.117.150.200:8080/CS122APROJECT1-1.0-SNAPSHOT/login.html
        slave: http://3.143.7.196:8080/CS122APROJECT1-1.0-SNAPSHOT/login.html
        single: http://52.14.192.44:8080/CS122APROJECT1-1.0-SNAPSHOT/login.html
        gcp-lb : http://34.66.239.163/CS122APROJECT1-1.0-SNAPSHOT/login.html

    - #### Instruction of deployment: 

    REQUIREMENTS:
    Java 11
    Apache-Tomcat9
    MySql
    Maven
    Android development Studio for emulation

    WHEN setting up mysql

    create a user 

    USERNAME MUST BE : mytestuser
    PASSWORD MUST BE : My6$Password

    mysql -u mytestuser -p -e "DROP DATABASE IF EXISTS moviedb; SHOW DATABASES;"

    After it is confirmed that moviedb database does not exist, then we load the
    new database schema.

        mysql -u mytestuser -p < createMovieTables.sql

    IF USING movie-data.sql provided by Jacob Martinez or Junkai Chen, the following command in MYSQL is needed afterwards,

        mysql -u mytestuser -p < movie-data.sql

    IF USING movie-data.sql provided by Professor Chen Li, The following command in MYSQL is needed afterwards,

    mysql -u mytestuser -p --database=moviedb < /home/ubuntu/movie-data.sql
    mysql -u mytestuser -p < stored-procedure.sql
    mysql -u mytestuser -p -e  "USE moviedb; ALTER TABLE stars CHANGE name starname VARCHAR(100);"
    mysql -u mytestuser -p -e "USE moviedb; INSERT IGNORE INTO ft (movieID, title) SELECT id, title FROM movies;"

    To add the additional Movies provided by the InfoLab of Stanford,
    The following Files must be added to the XMLParser Folder,
    actors63.xml, casts124.xml and mains243.xml.

    After the following Information is added to the XMLParser Folder,
    Open an existing project using Maven import and as a separate Project (within the XMLParser Directory)
    Enter the following lines into the terminal:

        mvn clean package
        java -cp target/XMLParser-1.0-SNAPSHOT.jar MergeInfo
        mysql -u mytestuser -p -e "use moviedb; INSERT IGNORE INTO ft (movieID, title) SELECT id, title FROM movies;"


    These commands are all that are needed to insert the new movies from the InfoLab of Stanford

    BEFORE LAUNCHING FABFLIX:
        The passwords for Employees and Customers must be encrypted by entering the following commands into the terminal
            NOTE: the referenced terminal above is in regards to the home directory of Fabflix (cs122b-spring21-team-54)

        mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="UpdateSecurePassword"
        mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="EmployeeSecurePassword"

    Now the passwords for customers and employees are secure and the application can be launched. 
    When launching from IntelliJ, make sure the configuration for the project is a local tomcat server.
    For deployment on a linux vm, make sure the following requirements are installed and the war file is compiled and
    moved to the webapp folder.

    STEPS FOR LINUX
   
        After Mysql is set up, it's time to move to deploying the web application.

        open the project directory and build the project using maven with this line.

        mvn package

        now it is time to add the war file to tomcat server.

        sudo cp ./target/CS122APROJECT1-1.0-SNAPSHOT.war /var/lib/tomcat9/webapps/

        now confirm the war file is available for tomcat web apps

        ls -lah /var/lib/tomcat9/webapps/

        The war file will appear 




    - #### Collaborations and Work Distribution: 

    SQL_Schema -- Jacob (Editted by: Junkai)
    1 Row 1 Moive Result Search Query Jacob
    SQL query for Actors by Movies played in Jacob
    stored-procedure.sql Jacob
    SearchServlet.java Junkai (Editted by: Jacob)
    UPDATED SearchServlet Junkai & Jacob
    Browsing Servlets Jacob
    Browsing Gernre html/js Jacob
    Logging in functionality Junkai
    Shopping Cart functionality Junkai
    Jump functionality between pages Jacob
    User Servlet Junkai
    SingleStarServlet.java Jacob (Editted by: Junkai)
    MovieListServlet.java Junkai (Editted by: Jacob)
    SingleMovie.java Junkai (Editted by: Jacob) 
    single-movie.html Junkai (Editted by: Jacob)
    single-movie.js Junkai (Editted by: Jacob)
    UPDATED single-movie.hmtl/js Jacob (Editted by: Junkai)
    UPDATED single-star.html/js Jacob (Editted by: Junkai)
    single-star.html Jacob 
    index.js Junkai (Editted by: Jacob)
    UPDATED index.js Junkai and Jacob
    index.html Junkai (Editted by: Jacob)
    UPDATED index.html Junkai and Jacob
    jumping between single movies and movielist by movie title Junkai 
    Jumping between single star and movielist by movie star Jacob
    Going back to the home page from Single movie page and Single Star page Jacob
    Payment pages Junkai
    Confirmation pages Junkai
    git repo organization Jacob
    XML Parser Jacob
    UPDATED XML Parser Jacob
    DashBoard and webpages associated with it Junkai
    Encryption Junkai
    Recaptcha Junkai
    Android Login development Jacob 
    Android Search Development Junkai & Jacob 
    Android Single movie page Junkai 
    Android ListViewActivity Jacob & Junkai
    Full search implementation Junkai
    Auto Complete Junkai
    README Jacob & Junkai



- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
        Single Star Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/SingleStarServlet.java
        Single Movie Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/SingleMovieServlet.java
        Search Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/SearchServlet.java
        AddMovie Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/AddMovieServlet.java
        Browse Genre Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/BrowseGenreServlet.java
        Browse Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/BrowseServlet.java
        Browsing Genre Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/BrowsingGenreServlet.java
        Employee Login Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/EmployeeLoginServlet.java
        Login Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/LoginFilter.java
        Login Mobile Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/LoginMobileServlet.java
        MovieList Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/MovieListServlet.java
        MovieSuggestion Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/MovieSuggestion.java
        Payment Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/PaymentServlet.java
        ShopCart Servlet: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/ShopCartServlet.java


    
    - #### Explain how Connection Pooling is utilized in the Fabflix code.
    Connection pooling is utilized in our code by maintaining the number of available threads to users.
    There are currently 100 avaialable threads to be used by new users which allows multiple users access to 
    Fabflix. This will help decrease the response time since our prepared statements are cached allowing for a faster retrieval when a 
    user searches the same exact query.
    
    - #### Explain how Connection Pooling works with two backend SQL.
    Connection pooling for two backend SQL servers allows for reading to happen from servers but when there is a write request needed for
    the Fabflix database, only the master can write into the SQL database which will update the information on its respective slave SQL servers.
    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
        Single Star Servlet: routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/SingleStarServlet.java
        Single Movie Servlet: routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/SingleMovieServlet.java
        Search Servlet: rooutes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/SearchServlet.java
        AddMovie Servlet: routes to Master (writing purpsoses of a new movie) => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/AddMovieServlet.java
        Browse Genre Servlet: routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/BrowseGenreServlet.java
        Browse Servlet: routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/BrowseServlet.java
        Browsing Genre Servlet: routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/BrowsingGenreServlet.java
        Employee Login Servlet: routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/EmployeeLoginServlet.java
        Login Servlet: routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/LoginServlet.java
        Login Mobile Servlet: routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/LoginMobileServlet.java
        MovieList Servlet: routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/MovieListServlet.java
        MovieSuggestion Servlet:  routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/MovieSuggestion.java
        Payment Servlet: routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/PaymentServlet.java
        ShopCart Servlet: routes to both => https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/java/com/example/CS122APROJECT1/ShopCartServlet.java
        context.xml : https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-54/blob/main/src/main/webapp/META-INF/context.xml

    - #### How read/write requests were routed to Master/Slave SQL?
    read requests are handled locally based on which server the load balancers decision to send to the master server or the slave server. 
    Write requests are the special exception which will only be handled by the Master SQL server in which there is an extra resource dedicated to writing in
    new information to the Fabflix database.

- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
    under the assumption that the timeInfo file is in the /tmp folder of the aws machine,
        the following commands are needed:

        // make sure you are in the base directory of the aws machine
        cd cs122b-spring21-team-54/WritingFile
        mvn clean package
        java -cp target/WritingFile-1.0-SNAPSHOT.jar logProcessor

    if the timeInfo file is not in the /tmp folder of the aws machine, then the following things are needed:

        cd /tmp
        sudo chmod 777 <tomcat service folder> 
        cd ./<tomcat service folder>/tmp
        sudo chmod 777 timeInfo
        sudo mv timeInfo /tmp
        cd /home/ubuntu/cs122b-spring21-team-54/WritingFile
        mvn clean package
        java -cp target/WritingFile-1.0-SNAPSHOT.jar logProcessor

    since there are different requests being sent to both slave and master for the scaled version instances, the following commands above need to be executed on both master and slave to read
    the times for query and servlet times. After the averages are displayed by the logProcessor.java, then the only thing needed to be done is the both averages and divide by 2 to get the average
    time of both slave and master queries together.

    NOTE: logs folder has a label for each log of servlet and query time based on the stress testing done to them. folders in the logs folder represent the times for both slave and master needed
    for the tj,ts calculations.


    This will show in the terminal the averages of the query time, search servlet time, and jdbc time.
    Note there are two different versions of the time shown for this and the data presented below represents 
    all queries with empty results. The purpose of showing times with and without empty results was to see how the time is affected on
    a smaller scale such as only a few requests done at once which makes the difference between and empty result set very drastic.



- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | 371.068ms                  | 373.313ms                           | 2.245ms                   | expcected results because of empty result set from movies that werent in database   |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | 2321.189ms                 | 3644.788ms                          | 1323.598ms                | expected results to be faster than no connection pooling           |
| Case 3: HTTPS/10 threads                       | ![](path to image in img/)   | 2343.246ms                 | 3661.955ms                          | 1318.708ms                | expected results to be slightly more time than HTTP but was not expecting it to be faster than no connection pooling           |
| Case 4: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 2380.562ms                 | 3695.849ms                          | 1315.286ms                | was expecting worse because of no connection pooling           |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | 347.032ms                  | 349.281ms                           | 2.249ms                   | expcected results because of empty result set from movies that werent in database           |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | 1736.263ms                 | 1740.097ms                          | 3.834ms                   | expected results to be faster than no connection pooling           |
| Case 3: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 1587.385ms                 | 1729.969ms                          | 142.583m                  | was expecting worse because of no connection pooling           |
