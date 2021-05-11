import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class InsertMovies {

    /*
        | Field    | Type         | Null | Key | Default | Extra |
        +----------+--------------+------+-----+---------+-------+
        | id       | varchar(10)  | NO   | PRI | NULL    |       |
        | title    | varchar(100) | NO   |     | NULL    |       |
        | year     | int          | NO   |     | NULL    |       |
        | director | varchar(100) | NO   |     | NULL    |       |
     */

    public InsertMovies(){}


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


        sqlInsertRecord="INSERT IGNORE INTO movies(id,title,year,director) VALUES(?,?,?,?)";
        try {
            conn.setAutoCommit(false);

            psInsertRecord=conn.prepareStatement(sqlInsertRecord);
            Iterator<Movie> it = movies.iterator();
            int count = 0;
            while(it.hasNext())
            {
                Movie m = it.next();
                psInsertRecord.setString(1,m.getId());
                psInsertRecord.setString(2,m.getTitle());
                psInsertRecord.setInt(3,m.getYear());
                psInsertRecord.setString(4,m.getDirector());

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
        System.out.println("New movies has been inserted");
    }
}
