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


    public InsertGenres()
    {
    }

    public void run(List<String> genres)throws InstantiationException, IllegalAccessException, ClassNotFoundException
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


        sqlInsertRecord="INSERT IGNORE INTO genres(name) VALUES(?)";
        try {
            psInsertRecord=conn.prepareStatement(sqlInsertRecord);

            Iterator<String> gen = genres.iterator();
            while(gen.hasNext())
            {
                String ge = gen.next();
                psInsertRecord.setString(1,ge);
                psInsertRecord.execute();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if(psInsertRecord!=null) psInsertRecord.close();
            if(conn!=null) conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("New Genres have been inserted");
    }

}
