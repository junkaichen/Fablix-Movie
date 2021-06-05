package XMLSAX;


import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Iterator;


public class InsertMovieStarRelation {

//    | starId  | varchar(10) | YES  | MUL | NULL    |       |
//    | movieId | varchar(10) | YES  | MUL | NULL    |       |

    public InsertMovieStarRelation()
    {}

    public void run(List<PairMovieStar> starsInMovies)
    {
        Connection conn = null;

        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String jdbcURL="jdbc:mysql://localhost:3306/moviedb";

        try {
            conn = DriverManager.getConnection(jdbcURL,"mytestuser", "My6$Password");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement psInsertRecord=null;
        String sqlInsertRecord=null;

        int[] iNoRows=null;


        sqlInsertRecord="INSERT IGNORE INTO stars_in_movies(starId,movieId) VALUES(?,?)";
        try {
            conn.setAutoCommit(false);

            psInsertRecord=conn.prepareStatement(sqlInsertRecord);
            int count = 0;
            Iterator<PairMovieStar> it = starsInMovies.iterator();
            while(it.hasNext())
            {
                PairMovieStar tempPair = it.next();
                psInsertRecord.setString(1,tempPair.getFirst());
                psInsertRecord.setString(2,tempPair.getSecond());
                count++;
                psInsertRecord.addBatch();
                if(count >= 100)
                {
                    count = 0;
                    iNoRows=psInsertRecord.executeBatch();
                }
            }
            if(count != 0)
            {
                iNoRows=psInsertRecord.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if(psInsertRecord!=null) psInsertRecord.close();
            if(conn!=null) conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("XMLSAX.Movie and Star relations have been inserted");
    }
}
