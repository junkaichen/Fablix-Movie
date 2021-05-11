import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class movieParseXML extends DefaultHandler {
    List<Movie> movies;

    private String tempVal;

    private Movie tempMov;

    public movieParseXML()
    {
        movies = new ArrayList<Movie>();
    }

    public void run()
    {
        parseDocument();
        //printData();
    }

    private void parseDocument()
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try{
            SAXParser sp = spf.newSAXParser();
            sp.parse("mains243.xml",this);

        }
        catch(SAXException se)
        {
            se.printStackTrace();
        }
        catch(ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch(IOException ie)
        {
            ie.printStackTrace();
        }
    }

    private void printData()
    {
        System.out.println("Number of Movies '" + movies.size() + "'." );
        int countValid = 0;
        Iterator<Movie> it = movies.iterator();
        while(it.hasNext())
        {
            Movie it2 = it.next();
            if(it2.validMovie()){
                System.out.println(it2.toString());
                countValid++;
            }

        }
        System.out.println("Valid movies is :" + countValid);
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
        tempVal = "";
        if(qName.equalsIgnoreCase("film"))
        {
            tempMov = new Movie();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        tempVal = new String(ch,start,length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException{
        if(qName.equalsIgnoreCase("film"))
        {

            if(tempMov.getGenres().isEmpty())
            {
                tempMov.addGenre("unknown");
            }
            Iterator<Movie> it = movies.iterator();
            boolean found = false;
            while(it.hasNext() && !found)
            {
                Movie temper = it.next();
                if(temper.getTitle().equalsIgnoreCase(tempMov.getTitle()))
                {
                    found = true;
                }
                if(temper.getId().equalsIgnoreCase(tempMov.getId()))
                {
                    found = true;
                }
            }
            if(!found)
            {
                if(tempMov.validMovie())
                {
                    movies.add(tempMov);
                }

            }

        }
        else if(qName.equalsIgnoreCase("fid"))
        {
            tempMov.setId(tempVal);
        }
        else if(qName.equalsIgnoreCase("t"))
        {
            tempMov.setTitle(tempVal);
        }
        else if(qName.equalsIgnoreCase("year"))
        {
            tempMov.setYear(tempVal);
        }
        else if(qName.equalsIgnoreCase("cat"))
        {
            tempMov.addGenre(tempVal);
        }
        else if(qName.equalsIgnoreCase("cats"))
        {
            tempMov.addGenre(tempVal);
        }
        else if(qName.equalsIgnoreCase("dirn")) {
            tempMov.setDirector(tempVal);
        }

    }

    public List<Movie> getMovies() {
        return movies;
    }

    public static void main(String[] args)
    {
        movieParseXML a = new movieParseXML();
        a.run();
    }

}
