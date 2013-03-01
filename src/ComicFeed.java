/*
 * constructor: public ComicFeed(String u)
 * methods:
 *  public void getFeedInfo()
 *  public String[][] getComicEntries()
 *  public String parseComicAddress(String line)
 *  public String buildLibraryLine()
 *  public void downloadComic(String x)
 */
 
// package komixnavigator;

import java.net.URL;    
import java.util.AbstractCollection;
import com.sun.syndication.feed.*;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.feed.synd.SyndImage;
import com.sun.syndication.io.XmlReader;
import java.util.*;
import java.util.List;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jdom.*;

public class ComicFeed {
    
    // attributes
    String urlString;
    Date channelPubDate;
    String comicName;
    String itemLink;
    String itemTitle;
    String itemDescription;
    String itemEnclosure;
    List itemList;
    SyndFeed feed;
    
    String[][] comicEntryArray = null;

    // constructor
    public ComicFeed(String u) {
        urlString = u;
    }
    
    // methods
    public void getFeedInfo() throws Exception {
        // this method should either get the info for a new feed
        // or get the url of the comic image file.
        XmlReader reader = null;
        URL url = new URL(urlString);
        
        try {
            reader = new XmlReader(url);
            feed = new SyndFeedInput().build(reader);
            
            channelPubDate = feed.getPublishedDate();
            comicName = feed.getTitle();
            // itemList = feed.getEntries(); // for testing
        } finally {
            if (reader != null)
                reader.close();
        }
    }
    
    public String[][] getComicEntries() throws Exception {
        // returns an array of comic entries
        /* [0] = date
         * [1] = full address
         * [2] = image name (name.jpg, etc)
         * [3] = path without name
         */
        XmlReader reader = null;
        URL url = new URL(urlString);

        String[][] comicEntryArray = null;
        
        try {
            reader = new XmlReader(url);
            feed = new SyndFeedInput().build(reader);
        } catch (Exception e) { System.out.println("trying to get feed:"); }
        
            List<SyndEntry> entries = new ArrayList<SyndEntry>(feed.getEntries());
            //entries.addAll(0, feed.getEntries());
            // System.out.println("NUMBER OF ENTRIES: " + entries.size());
            //System.out.println("Entry 3: " + entries.get(3));
            comicEntryArray = new String[entries.size()][4];
            // System.out.println("Size of comicEntryArray: " + comicEntryArray.length);
            //Iterator itr = feed.getEntries.interator();
            for (int x = 0; x < entries.size(); x++)
            {
                SyndEntry entry = (SyndEntry) entries.get(x);
                String[] details = new String[3];
                //System.out.println("Trying to get next.");
                //SyndEntry entry = (SyndEntry) entries.get(x);
                //System.out.println("Does itr have next? " + itr.hasNext());
                //System.out.println("getDescription: " + entries.get(x));
                String tryThis = entry.toString();
                // System.out.println("TRYING TO MAKE A STRING: " + tryThis);
                //entry.getDescription().getValue());
                details = parseComicAddress(tryThis);
                comicEntryArray[x][0] = entry.getPublishedDate().toString();
                // System.out.println("Date: " + comicEntryArray[x][0]);
                for (int i = 1; i < 4; i++) {
                        comicEntryArray[x][i] = details[i - 1];
                        // System.out.println("x = " + x);
                        //System.out.println("details[i]: " + details[i]);
                        // System.out.println("details[i-1]: " + details[i-1]);
                    }
            }
            /*
            System.out.println("Item 1 in entries: " + entries.get(0));
            // System.out.println("Entries: " + entries);
            if (entries != null) {
                comicEntryArray = new String[entries.size()][4];
                System.out.println("Number of entries (ComicFeed origin): " + entries.size());
                int itr = -1;
                
                for (Iterator entryIterator = feed.getEntries().iterator(); entryIterator.hasNext(); ) {
                    itr++;
                    SyndEntry entry = (SyndEntry) entryIterator.next();
                    
                    System.out.println("entries has Next? " + entryIterator.hasNext());
                    System.out.println("Entry #" + itr + ": " + entry);
                    System.out.println("Entry getDescription: " + entry.getDescription().getValue());
                    details = parseComicAddress(entry.getDescription().getValue());
                    comicEntryArray[itr][0] = entry.getPublishedDate().toString();
                    for (int x = 1; x < 4; x++) {
                        comicEntryArray[itr][x] = details[x - 1];
                        System.out.println("details[x]: " + details[x]);
                        System.out.println("details[x-1]: " + details[x-1]);
                    }
                }
            } */
            //} catch (Exception e) {System.out.println("getComic: " + e);}
        
        // finally, return the array of comic entries
        /*
        for (int i = 0; i < comicEntryArray.length; i++) {
            System.out.println("0: " + comicEntryArray[i][0] + " 1: " +
                comicEntryArray[i][1] + " 2: " + comicEntryArray[i][2] + " 3: " + 
                comicEntryArray[i][3]);
        } */
         if (reader != null)
                reader.close();
        return comicEntryArray;
    }
    
    public String[] parseComicAddress(String line) {
        // it seems like the address for the comic exists in the description field
        // it also seems like the Image class doesn't extract that info
        // [0] = whole string
        // [1] = comic.jpg
        // [2] = stem address before the comic.jpg
        
        String searchKey = "img src=\"\\S+\"";
        String[] returnedAddress = new String[3];
        try {
            Pattern pattern = Pattern.compile(searchKey);
            Matcher matcher = pattern.matcher(line);
            String matchedString = null;
            if (matcher.find()) {
                matchedString = matcher.group();
                String[] imageKeys = {"jpg", "jpeg", "gif", "png"}; 
                matchedString = matchedString.substring(9, (matchedString.length() - 1));
                returnedAddress[0] = matchedString;
                // System.out.println("MatchedString: " + returnedAddress[0]);
                    for (int i = 0;i < imageKeys.length; i++) { 
                        searchKey = "[a-zA-Z0-9\\_\\-\\.]+." + imageKeys[i];
                        Pattern namePattern = Pattern.compile(searchKey);
                        Matcher nameMatcher = namePattern.matcher(matchedString);
                        if (nameMatcher.find()) {
                            String comicName = nameMatcher.group();
                            returnedAddress[1] = comicName;
                            // System.out.println(returnedAddress[1]);
                            returnedAddress[2] = matchedString.substring(0, 
                                matchedString.length() - comicName.length());
                            // System.out.println(returnedAddress[2]);
                        }
                    }
            }
            
            // may have to strip off the extra text
        } catch (Exception e) {
             System.out.println(searchKey);
        }
        finally {
            return returnedAddress;
        }
    }
    
    public String buildLibraryLine() {
        String libraryLine;
        String delimiter = ",";
        
        libraryLine = comicName + delimiter + urlString;
        return libraryLine;
    }       
}
