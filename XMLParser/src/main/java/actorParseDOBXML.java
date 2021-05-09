import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class actorParseDOBXML extends DefaultHandler {

    List<Actor> actors;

    private String tempVal;

    private Actor tempAct;

    public actorParseDOBXML()
    {
        actorParseXML actorMovieInfo = new actorParseXML();
        actorMovieInfo.run();
        actors = actorMovieInfo.ActorInfo();
    }

    public void run()
    {
        parseDocument();
        printData();
    }

    private void parseDocument()
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try{
            SAXParser sp = spf.newSAXParser();
            sp.parse("actors63.xml",this);

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
        if(qName.equalsIgnoreCase("actor"))
        {
            tempAct = new Actor();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        tempVal = new String(ch,start,length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException{
        if(qName.equalsIgnoreCase("actor"))
        {

            Iterator<Actor> it = actors.iterator();
            boolean found = false;
            int count_pos = 0;
            while(it.hasNext() && !found)
            {
                if(it.next().getName().equals(tempAct.getName()))
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
                actors.get(count_pos).setDob(Integer.toString(tempAct.getDob()));
            }
            else
            {
                actors.add(tempAct);
            }

        }
        else if(qName.equalsIgnoreCase("stagename"))
        {
            tempAct.setName(tempVal);
        }
        else if(qName.equalsIgnoreCase("dob"))
        {
            tempAct.setDob(tempVal);
        }

    }

    public static void main(String[] args)
    {
        actorParseDOBXML a = new actorParseDOBXML();
        a.run();
    }
}
