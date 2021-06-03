package XMLSAX;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class InsertMovieGenreRelation {
    private HashMap<String,Integer> genreInfo;

    /*
        | Field   | Type        | Null | Key | Default | Extra |
        +---------+-------------+------+-----+---------+-------+
        | genreId | int         | YES  | MUL | NULL    |       |
        | movieId | varchar(10) | YES  | MUL | NULL    |       |
         */
    public InsertMovieGenreRelation()
    {
        this.genreInfo = new HashMap<String,Integer>();
    }

    public void run(List<Movie> movies) throws InstantiationException, IllegalAccessException, ClassNotFoundException
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
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM genres;");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                String name = rs.getString("name");
                int id = Integer.parseInt(rs.getString("id"));
                genreInfo.put(name,id);
            }
            ps.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        PreparedStatement psInsertRecord=null;
        String sqlInsertRecord=null;
        int[] iNoRows=null;

        sqlInsertRecord="INSERT IGNORE INTO genres_in_movies(genreId,movieId) VALUES(?,?)";
        try {
            conn.setAutoCommit(false);

            psInsertRecord=conn.prepareStatement(sqlInsertRecord);
            Iterator<Movie> it = movies.iterator();
            int count = 0;
            while(it.hasNext())
            {
                Movie m = it.next();
                for(String gen : m.getGenres())
                {
                    psInsertRecord.setInt(1,genreInfo.get(gen));
                    psInsertRecord.setString(2,m.getId());
                    count++;
                    psInsertRecord.addBatch();
                }
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
        System.out.println("XMLSAX.Movie and Genre relations have been inserted");
    }

}
