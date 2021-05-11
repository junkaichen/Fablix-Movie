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
        if(genre.equalsIgnoreCase("Susp"))
        {
            return "Thriller";
        }
        if(genre.equalsIgnoreCase("CnR"))
        {
            return "Cops and Robbers";
        }
        if(genre.equalsIgnoreCase("Fant"))
        {
            return "Fantasy";
        }
        if(genre.equalsIgnoreCase("Epic"))
        {
            return "Epic";
        }
        if(genre.equalsIgnoreCase("Crim"))
        {
            return "Crime";
        }
        if(genre.equalsIgnoreCase("Dram"))
        {
            return "Drama";
        }
        if(genre.equalsIgnoreCase("West"))
        {
            return "Western";
        }
        if(genre.equalsIgnoreCase("Myst"))
        {
            return "Mystery";
        }
        if(genre.equalsIgnoreCase("S.F."))
        {
            return "Science Fiction";
        }
        if(genre.equalsIgnoreCase("ScFi"))
        {
            return "Science Fiction";
        }
        if(genre.equalsIgnoreCase("Advt"))
        {
            return "Adventure";
        }
        if(genre.equalsIgnoreCase("Horr"))
        {
            return "Horror";
        }
        if(genre.equalsIgnoreCase("Romt"))
        {
            return "Romantic";
        }
        if(genre.equalsIgnoreCase("Comd"))
        {
            return "Comedy";
        }
        if(genre.equalsIgnoreCase("Musc"))
        {
            return "Musical";
        }
        if(genre.equalsIgnoreCase("Docu"))
        {
            return "Documentary";
        }
        if(genre.equalsIgnoreCase("Porn"))
        {
            return "Porn";
        }
        if(genre.equalsIgnoreCase("Noir"))
        {
            return "Noir";
        }
        if(genre.equalsIgnoreCase("BioP"))
        {
            return "Biographical Picture";
        }
        if(genre.equalsIgnoreCase("Actn"))
        {
            return "Action";
        }
        if(genre.equalsIgnoreCase("TV"))
        {
            return "TV Show";
        }
        if(genre.equalsIgnoreCase("Hist"))
        {
            return "History";
        }
        if(genre.equalsIgnoreCase("Disa"))
        {
            return "Disaster";
        }
        if(genre.equalsIgnoreCase("Cart"))
        {
            return "Cartoon";
        }
        if(genre.equalsIgnoreCase("Faml"))
        {
            return "Family";
        }
        if(genre.equalsIgnoreCase("Surl"))
        {
            return "Surreal";
        }
        if(genre.equalsIgnoreCase("TVs"))
        {
            return "TV Series";
        }
        if(genre.equalsIgnoreCase("TVm"))
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
