package XMLSAX;



import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;




public class MergeInfo{

    private List<Movie> movies;
    private List<Actor> actors;
    private List<String> movieIds;
    private int countRemoved;
    private int countRemovedMovies;
    private List<String> allGenres;
    private List<PairMovieStar> starsInMovie;



    public MergeInfo()
    {
        countRemoved = 0;
        countRemovedMovies = 0;
        actorParseDOBXML a = new actorParseDOBXML();
        movieParseXML m = new movieParseXML();
        a.run();
        m.run();
        movies = new ArrayList<Movie>();
        actors  = new ArrayList<Actor>();
        movies = m.getMovies();
        actors = a.getActors();
        movieIds = new ArrayList<String>();
        allGenres = new ArrayList<String>();
        starsInMovie = new ArrayList<>();
    }

    private void findMoviesWithNoActors()
    {
        int i = 0;
        while(i < movies.size())
        {
            Movie tempMovie = movies.get(i);
            if(movies.get(i).hasNoActors()) {
                countRemovedMovies++;
                movies.remove(i);
                if (i > 1) {
                    i -= 2;
                }
                else
                {
                    i = 0;
                }
            }
            else
            {
                i++;
            }
        }
        System.out.println("Movies removed:" + countRemovedMovies);
    }

    public void allGenresFromMovies()
    {
        Iterator<Movie> it = movies.iterator();
        while(it.hasNext())
        {
            ArrayList<String> g = it.next().getGenres();
            for(String gen : g)
            {
                if(!allGenres.contains(gen))
                {
                    allGenres.add(gen);
                }
            }
        }
    }

    public void run(){
        System.out.println("The size of Parse XML Movies size is :" + movies.size());
        System.out.println("The size of Parse XML Actors size is : " + actors.size());
        confirmActors();
        findMoviesWithNoActors();
        System.out.println("Total number of movies remaining is: " + movies.size());
        allGenresFromMovies();
        System.out.println("Total number of found Genres: " + allGenres.size());
    }

    public void confirmActors()
    {
        int i = 0;
        while (i < movies.size())
        {
            Iterator<Actor> ia = actors.iterator();
            while(ia.hasNext())
            {
                Actor tempAct = ia.next();
                if(tempAct.playedInMovie(movies.get(i).getId()))
                {
                    movies.get(i).confirmActors();
                    starsInMovie.add(new PairMovieStar(tempAct.getId(),movies.get(i).getId()));
                }
            }
            i++;
        }
    }





    public List<String> getAllGenres()
    {
        return allGenres;
    }

    public List<Actor> getActors()
    {
        return actors;
    }

    public List<PairMovieStar> getStarsInMovie()
    {
        return starsInMovie;
    }

    public List<Movie> getMovies()
    {
        return this.movies;
    }


    public void printNumberOfStarswithMovies()
    {
        int count = 0;
        Iterator<Actor> it = actors.iterator();
        while(it.hasNext())
        {
            if(!it.next().getMovies().isEmpty())
            {
                count++;
            }
        }
        System.out.println("Out of " + actors.size() + " actors, " + count + " have movies");
    }




    public static void main(String[] args)
    {
        MergeInfo m = new MergeInfo();
        m.run();
        InsertGenres ig = new InsertGenres();
        InsertStars is = new InsertStars();
        InsertMovies im = new InsertMovies();
        InsertRatings ir = new InsertRatings();
        InsertMovieGenreRelation mgr = new InsertMovieGenreRelation();
        InsertMovieStarRelation msr = new InsertMovieStarRelation();

        try {
            ig.run(m.getAllGenres());
            is.run(m.getActors());
            im.run(m.getMovies());
            ir.run(m.getMovies());
            mgr.run(m.getMovies());
            msr.run(m.getStarsInMovie());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        m.printNumberOfStarswithMovies();


    }


}