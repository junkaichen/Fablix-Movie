import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class InsertGenres {

    private List<String> genres;

    public InsertGenres(List<String> genres)
    {
        this.genres = genres;
    }

    public void run()throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        MergeInfo in = new MergeInfo();
        in.run();

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


        sqlInsertRecord="INSERT IGNORE INTO genres(name) VALUES(?)";
        try {
            conn.setAutoCommit(false);

            psInsertRecord=conn.prepareStatement(sqlInsertRecord);

            for(String gen : in.getAllGenres())
            {
                psInsertRecord.setString(1,gen);
                psInsertRecord.addBatch();
            }

            iNoRows=psInsertRecord.executeBatch();
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
    }

}
