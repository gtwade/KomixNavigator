// Program: FileService
// Programmer: Guy Wade
// Date:

import java.nio.file.*;
import java.io.*;
import static java.nio.file.StandardOpenOption.*;
// import java.nio.ByteBuffer;
// import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.*;
import java.util.regex.*;
import java.net.URL;
import java.util.*;
import java.util.regex.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileService
{
    // the library list file
    final String LIBRARY = "library.txt";
    final String FAVORITES = "favorites.txt";
    String delimiter = ",";
    String filePath;
    Scanner scanner;
    int arrayLength;
    String type;
    
    // constructor - determines which file to use
    public FileService()
    {
    }
    
    // accessor methods
    public void setType(String t) { 
        type = t;
        if (type.contentEquals("library")) {
            arrayLength = 2; // number of elements in the library file
            setFilePath(LIBRARY);
        }
        else {
            arrayLength = 4;
            setFilePath(FAVORITES);
        }
    }
    public String getType()
    { return type; }
    
    public void setArrayLength(int l)
    { arrayLength = l; }
    public int getArrayLength()
    { return arrayLength; }
    
    public void setFilePath(String f)
    { filePath = f; }
    public String getFilePath()
    { return filePath; }
    /*
    public void writeToFile(String textLine)
    {
        // should only write a file if it doesn't already exist
        Path file = Paths.get(getFilePath());
        OutputStream output = 
            new BufferedOutputStream(Files.newOutputStream(file, CREATE));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
    }
    */
    public Vector findLibraryLine(String searchKey)
    {
        int x = getArrayLength(); // for size of array
        String line;
        
        String[] returnedLine = new String[x];
        Vector linesFound = new Vector();
        File file = new File(getFilePath());
        
        try
		{
		    scanner = new Scanner(new FileReader(file));
			Pattern pattern = Pattern.compile(searchKey);

			Boolean lineFound = false;
			
			while(scanner.hasNextLine())
			{
			    line = scanner.nextLine();
                Matcher matcher = pattern.matcher(line);			    
			    // look for a match, then parse the line into the array
			    if(matcher.find()) {
                    // if match found, parse line and add to array
                    returnedLine = line.split(delimiter);
                    linesFound.add(returnedLine);
			    }
			}
		} catch(Exception e) {
		    
		} finally {
		    scanner.close();
		    return linesFound;
		}
    }
    
    public String[][] getLibraryList() {
        // return whole library as a string array to populate the list box
        int x = getArrayLength(); // for size of array
        int i = 0;
        String line;
        String[] splitLine = new String[2];
        String[][] returnedArray = null;
        File file = new File(getFilePath());
        
        try
		{
		    scanner = new Scanner(new FileReader(file));
		    while (scanner.hasNextLine()) { // get the number of lines in library
		        i++;
		        line = scanner.nextLine();
		    }
		    scanner.close();
		    
		    scanner = new Scanner(new FileReader(file));
		    returnedArray = new String[i][x];
		    i = -1;
		    while (scanner.hasNextLine()) {
		        i++;
                line = scanner.nextLine();
                splitLine = line.split(delimiter);
                returnedArray[i][0] = splitLine[0];
                returnedArray[i][1] = splitLine[1];
            }
            scanner.close();
		        
		        
        } catch (Exception e) {
        }
		  
		return returnedArray;
    }
    
    public String downloadComic(String[] c) throws Exception {
        URL url = new URL(c[0]);
        String destinationFile = c[1];
        String savePath = "img/";
        String finalDestination = savePath + destinationFile;
        InputStream in = url.openStream();
        OutputStream out = 
            new BufferedOutputStream(new FileOutputStream(finalDestination));
        byte[] b = new byte[2048];
		int length;

		while ((length = in.read(b)) != -1) {
			out.write(b, 0, length);
		}
        out.close();
        in.close();
        return (finalDestination);
    }
    
}
