package ca.sandytran.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Sandy on 2016-06-02.
 */
public class ParseApplications {
    private String xmlData; //xml file contents we're downloading, we will pass to this class for parsing
    private ArrayList<Application> applications; //one for each of the top ten apps

    public ParseApplications(String xmlData) {
        this.xmlData = xmlData;
        applications = new ArrayList<>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    //extracts info we want from xml file
    public boolean process(){
        boolean status = true; //identify if process has succeeded or failed
        Application currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            // built into android to parse xml for us
            // set up and initialize xml parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData)); //setInput only excepts StringReader so convert
            int eventType = xpp.getEventType();

            //keep going til we get to the end of the document
            while (eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch(eventType) {
                    case XmlPullParser.START_TAG:
//                        Log.d("ParseApplications", "Starting tag for " + tagName);
                        if (tagName.equalsIgnoreCase("entry")) {
                            inEntry = true;
                            currentRecord = new Application();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
//                        Log.d("ParseApplications", "ending tag for " + tagName);
                        if (inEntry){
                            if (tagName.equalsIgnoreCase("entry")){
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if (tagName.equalsIgnoreCase("name")){
                                currentRecord.setName(textValue);
                            } else if (tagName.equalsIgnoreCase("artist")){
                                currentRecord.setArtist(textValue);
                            } else if (tagName.equalsIgnoreCase("releaseDate")){
                                currentRecord.setReleaseDate(textValue);
                            }
                        }
                        break;

                    default:
                        //nothing else to do
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        for (Application app : applications) {
            Log.d("ParseApplications", "Name: " + app.getName());
        }
        return true;
    }

}
