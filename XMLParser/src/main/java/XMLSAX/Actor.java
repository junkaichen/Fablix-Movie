package XMLSAX;

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

    public void setName(String Actorname)
    {
        this.name = Actorname;
        if(!name.equals(""))
        {
            setId(Actorname);
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
        if(dob.equals(""))
        {
            this.dob = 0;
        }
        else if(numbersOnly(dob))
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

    public String getFirstMovieId()
    {
        return movies_played_in.get(0);
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
        sb.append("XMLSAX.Actor Details - ");
        sb.append("Name: " + this.name);
        sb.append(",");
        sb.append("MovieIDs of movies played in: " + movies_played_in.toString());
        sb.append(",");
        sb.append("DOB:" + dob);

        return sb.toString();
    }

    private void setId(String name)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("A");
        sb.append(name.substring(0,1).toLowerCase());
        int value = 0;
        for(int i = 0; i < name.length(); i++)
        {
            value += name.charAt(i);
        }
        if( value % 10000 == 0) {
            sb.append(sb.append(name.substring(0, name.length() / 2)));
            if (sb.length() > 10) {
                String newId = sb.toString().substring(0, 9);
                this.id = newId;
            }
            else
            {
                this.id = sb.toString();
            }
        }
        else
        {
            sb.append(value % 10000);
            if (sb.length() > 10) {
                String newId = sb.toString().substring(0, 9);
                this.id = newId;
            }
            else
            {
                this.id = sb.toString();
            }
        }
    }

}
