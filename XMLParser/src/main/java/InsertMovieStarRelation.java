import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InsertMovieStarRelation {

//    | starId  | varchar(10) | YES  | MUL | NULL    |       |
//    | movieId | varchar(10) | YES  | MUL | NULL    |       |

    public InsertMovieStarRelation()
    {}

    public void run(HashMap<String,String> starsInMovie)
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
            for(Map.Entry<String,String> entry : starsInMovie.entrySet())
            {
                psInsertRecord.setString(1,entry.getKey());
                psInsertRecord.setString(2,entry.getValue());
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
        System.out.println("Movie and Star relations have been inserted");
    }
}
