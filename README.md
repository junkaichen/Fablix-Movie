IMPORTANT: ONLY WAY TO ACCESS THIS REPOSISTORY IS TO BE PART OF TEAM 54 OR STAFF OF CS122B SPRING 2021. 
THIS IS A CLASSROOM PROJECT AND NO CODE MAY BE USED OUTSIDE OF THIS PROJECT IN ANY FORMAT.
ALL CODE THAT WAS NOT PROVIDED BY Professor Chen Li WAS CREATED BY TEAM 54.

LATEST UPDATE OF README: SATURDAY APRIL 10, 2021

LINK FOR VIDEO = https://drive.google.com/file/d/1me4rMcDOXmS8_YY7YRj39A-yiUCrfckq/view?usp=sharing

REQUIREMENTS:
Java 11
Apache-Tomcat9
MySql
Maven

WHEN setting up mysql

create a user 

USERNAME MUST BE : mytestuser
PASSWORD MUST BE : My6$Password

mysql -u mytestuser -p -e "DROP DATABASE IF EXISTS moviedb; SHOW DATABASES;"

After it is confirmed that moviedb database does not exist, then we load the
new database schema.

mysql -u mytestuser -p < createMovieTables.sql

This will create the database moviedb
Then we will need to populate the database moviedb.
When loading the movie-data.sql from Professor Chen Li

mysql -u mytestuser -p --database=moviedb < /home/ubuntu/movie-data.sql

mysql -u mytestuser -p -e  "USE moviedb; ALTER TABLE stars CHANGE name starname VARCHAR(100);"

When loading the movie-data.sql from Professor Chen Li,
Log into mytestuser and alter  the column name of stars to starname with this line

mysql -u mytestuser -p -e "USE moviedb; ALTER TABLE stars CHANGE name starname VARCHAR(100);"


This will match the data up with the java code provided to run the website.


After Mysql is set up, it's time to move to deploying the web application.

open the project directory and build the project using maven with this line.

mvn package

now it is time to add the war file to tomcat server.

cp ./target/CS122APROJECT1-1.0-SNAPSHOT.war /var/lib/tomcat9/webapps/

now confirm the war file is available for tomcat web apps

ls -lah /var/lib/tomcat9/webapps/

The war file will appear 

Finally, type in the url http://3.129.149.109:8080/CS122APROJECT1-1.0-SNAPSHOT/

to get to the landing page of the web application. The landing page will be the MovieList Page.

Once on the landing page, there will be options to search and browse within the home page. 
Most data will render on the main page with 
the exception of browsingby a genre when clicking a hyperlink from movie list table results.

The option to search is done with writing all 
possible parameters out the user wants to check from i.e. title,year,director and/or actor.

Substring matches designed use was %user_input% This subsstring match is used 
for title, director and stars. Year must be an exact match. 



Contributions

SQL_Schema -- Jacob (Editted by: Alex)
1 Row 1 Moive Result Search Query Jacob
SQL query for Actors by Movies played in Jacob
SearchServlet.java Alex (Editted by: Jacob)
Browsing Servlets Jacob
Browsing Gernre html/js Jacob
Logging in functionality Alex
Shopping Cart functionality Alex
Jump functionality between pages Jacob
User Servlet Alex
SingleStarServlet.java Jacob (Editted by: Alex)
MovieListServlet.java Alex (Editted by: Jacob)
SingleMovie.java Alex (Editted by: Jacob) 
single-movie.html Alex (Editted by: Jacob)
single-movie.js Alex (Editted by: Jacob)
UPDATED single-movie.hmtl/js Jacob (Editted by: Alex)
UPDATED single-star.html/js Jacob (Editted by: Alex)
single-star.html Jacob 
index.js Alex (Editted by: Jacob)
index.html Alex (Editted by: Jacob)
jumping between single movies and movielist by movie title Alex 
Jumping between single star and movielist by movie star Jacob
Going back to the home page from Single movie page and Single Star page Jacob
Payment pages Alex
Confirmation pages Alex
git repo organization Jacob
README Jacob (Editted by: Alex)
