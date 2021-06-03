package XMLSAX;

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

public class actorParseXML extends DefaultHandler {

    List<Actor> actors;

    private String tempVal;

    private Actor tempAct;

    public actorParseXML()
    {
        actors = new ArrayList<Actor>();
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
            sp.parse("casts124.xml",this);

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
        Iterator<Actor> it = actors.iterator();
        while(it.hasNext())
        {
            System.out.println(it.next().toString());
        }
        System.out.println("Number of Actors '" + actors.size() + "'." );
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
        tempVal = "";
        if(qName.equalsIgnoreCase("m"))
        {
            tempAct = new Actor();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        tempVal = new String(ch,start,length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException{
        if(qName.equalsIgnoreCase("m"))
        {
            Iterator<Actor> it = actors.iterator();
            boolean found = false;
            int count_pos = 0;
            while(it.hasNext() && !found)
            {
                if(it.next().getName().equalsIgnoreCase(tempAct.getName()))
                {
                    found = true;
                }
                else
                {
                    count_pos++;
                }

            }
            if(found)
            {
                actors.get(count_pos).addMovieID(tempAct.getFirstMovieId());
            }
            else
            {
                if(!tempAct.getName().equals(""))
                {
                    actors.add(tempAct);
                }
            }

        }
        else if(qName.equalsIgnoreCase("f"))
        {
            tempAct.addMovieID(tempVal);
        }
        else if(qName.equalsIgnoreCase("a"))
        {
            tempAct.setName(tempVal);
        }

    }

    public List<Actor> ActorInfo()
    {
        return actors;
    }


//    public static void main(String[] args)
//    {
//        XMLSAX.actorParseXML a = new XMLSAX.actorParseXML();
//        a.run();
//    }



}
