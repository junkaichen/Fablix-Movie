import java.util.ArrayList;

public class Movie {
    private String title;
    private ArrayList<String> genres;
    private String director;
    private int year;
    private String id;


    public Movie()
    {
        this.title = "";
        this.id = "";
        this.genres = new ArrayList<String>();
        this.director = "";
        this.year = -1;
    }

    public boolean validMovie()
    {
        if(!title.equals("") && !id.equals("") && !director.equals("") && year != -1 && !genres.isEmpty())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void addGenre(String genre)
    {
        if(!genre.isEmpty())
        {
            String actualGenre = translateGenre(genre);
            if(!genres.contains(actualGenre))
            {
                genres.add(actualGenre);
            }
        }
    }

    public ArrayList<String> getGenres()
    {
        return this.genres;
    }

    public void setYear(String year)
    {
        if(numbersOnly(year))
        {
            this.year = Integer.parseInt(year);
        }

    }

    public int getYear()
    {
        return this.year;
    }

    public void setDirector(String director)
    {
        if(this.director.equals(""))
        {
            this.director = director;
        }

    }

    public String getDirector()
    {
        return this.director;
    }

    private String translateGenre(String genre)
    {
        if(genre.equals("Susp"))
        {
            return "Thriller";
        }
        if(genre.equals("CnR"))
        {
            return "Cops and Robbers";
        }
        if(genre.equals("Dram"))
        {
            return "Drama";
        }
        if(genre.equals("West"))
        {
            return "Western";
        }
        if(genre.equals("Myst"))
        {
            return "Mystery";
        }
        if(genre.equals("S.F."))
        {
            return "Science Fiction";
        }
        if(genre.equals("Advt"))
        {
            return "Adventure";
        }
        if(genre.equals("Horr"))
        {
            return "Horror";
        }
        if(genre.equals("Romt"))
        {
            return "Romantic";
        }
        if(genre.equals("Comd"))
        {
            return "Comedy";
        }
        if(genre.equals("Musc"))
        {
            return "Musical";
        }
        if(genre.equals("Docu"))
        {
            return "Documentary";
        }
        if(genre.equals("Porn"))
        {
            return "Porn";
        }
        if(genre.equals("Noir"))
        {
            return "Noir";
        }
        if(genre.equals("BioP"))
        {
            return "Biographical Picture";
        }
        if(genre.equals("TV"))
        {
            return "TV Show";
        }
        if(genre.equals("TVs"))
        {
            return "TV Series";
        }
        if(genre.equals("TVm"))
        {
            return "TV Miniseries";
        }
        else
        {
            return "Unknown";
        }
    }
    private boolean numbersOnly(String year)
    {
        if(year.matches("[0-9]+"))
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
        sb.append("ID: " + this.id);
        sb.append(",");
        sb.append("Title: " + this.title);
        sb.append(",");
        sb.append("Director: " + this.director);
        sb.append(",");
        sb.append("Year: " + this.year);
        sb.append(",");
        sb.append("Genres: " + genres.toString().substring(1,genres.toString().length()-1));
        return sb.toString();
    }
}
