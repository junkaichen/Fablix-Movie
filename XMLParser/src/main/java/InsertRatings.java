import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class InsertRatings {

    /*
    | Field    | Type        | Null | Key | Default | Extra |
    +----------+-------------+------+-----+---------+-------+
    | movieId  | varchar(10) | YES  | MUL | NULL    |       |
    | rating   | float       | NO   |     | NULL    |       |
    | numVotes | int         | NO   |     | NULL    |       |
     */

    public InsertRatings(){}



    public void run(List<Movie> movies)throws InstantiationException, IllegalAccessException, ClassNotFoundException
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


        sqlInsertRecord="INSERT IGNORE INTO ratings(movieId,rating,numVotes) VALUES(?,?,?)";
        try {
            conn.setAutoCommit(false);

            psInsertRecord=conn.prepareStatement(sqlInsertRecord);
            Iterator<Movie> it = movies.iterator();
            int count = 0;
            while(it.hasNext())
            {
                Movie m = it.next();
                psInsertRecord.setString(1,m.getId());
                psInsertRecord.setFloat(2,(float)7.5);
                psInsertRecord.setInt(3,341);

                count++;
                psInsertRecord.addBatch();
                if(count == 100)
                {
                    count = 0;
                    iNoRows=psInsertRecord.executeBatch();
                    psInsertRecord.clearBatch();
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
        System.out.println("Movie ratings have been inserted");
    }
}
