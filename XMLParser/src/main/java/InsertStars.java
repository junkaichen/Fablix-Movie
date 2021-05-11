import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class InsertStars {

    /*
    | Field     | Type         | Null | Key | Default | Extra |
    +-----------+--------------+------+-----+---------+-------+
    | id        | varchar(10)  | NO   | PRI | NULL    |       |
    | starname  | varchar(100) | YES  |     | NULL    |       |
    | birthYear | int          | YES  |     | NULL    |       |
     */

    public InsertStars(){}

    public void run(List<Actor> actors)throws InstantiationException, IllegalAccessException, ClassNotFoundException
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


        sqlInsertRecord="INSERT IGNORE INTO stars(id,starname,birthYear) VALUES(?,?,?)";
        try {
            conn.setAutoCommit(false);

            psInsertRecord=conn.prepareStatement(sqlInsertRecord);
            Iterator<Actor> it = actors.iterator();
            int count = 0;
            while(it.hasNext())
            {
                Actor a = it.next();
                psInsertRecord.setString(1,a.getId());
                psInsertRecord.setString(2,a.getName());
                if(a.getDob() == 0)
                {
                    psInsertRecord.setInt(3,java.sql.Types.INTEGER);
                }
                else
                {
                    psInsertRecord.setInt(3,a.getDob());
                }
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
        System.out.println("New stars have been inserted");
    }
}
