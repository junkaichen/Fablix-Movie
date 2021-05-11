import java.util.ArrayList;

public class Actor {
    private String name;
    private ArrayList<String> movies_played_in;
    private int dob;
    private String id;

    public Actor()
    {
        this.name = "";
        this.movies_played_in = new ArrayList<String>();
        this.dob = 0;
        this.id = "";
    }

    public void setName(String name)
    {
        this.name = name;
        if(!name.equals(""))
        {
            this.id = this.name.substring(0,1) + this.name.hashCode();
            this.id = "a" + this.id;
            if(this.id.length() > 10)
            {
                this.id = this.id.substring(0,9);
            }
        }
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public void addMovieID(String movie)
    {
        if(!movies_played_in.contains(movie))
        {
            movies_played_in.add(movie);
        }
    }

    public ArrayList<String> getMovies()
    {
        return this.movies_played_in;
    }

    public void setDob(String dob)
    {
        if(numbersOnly(dob))
        {
            this.dob = Integer.parseInt(dob);
        }
    }

    public int getDob()
    {
        return this.dob;
    }

    private boolean numbersOnly(String dob)
    {
        if(dob.matches("[0-9]+"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void removeMovieID(String id)
    {
        if(movies_played_in.contains(id))
        {
            movies_played_in.remove(movies_played_in.indexOf(id));
        }
    }

    public boolean playedInMovie(String id)
    {
        if(movies_played_in.contains(id))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Actor Details - ");
        sb.append("Name: " + this.name);
        sb.append(",");
        sb.append("MovieIDs of movies played in: " + movies_played_in.toString());
        sb.append(",");
        sb.append("DOB:" + dob);

        return sb.toString();
    }

}
