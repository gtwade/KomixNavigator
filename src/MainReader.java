// Program: MainReader 
// Programmer: Guy Wade
// Date:

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainReader extends JFrame implements ActionListener
{
	// class scope variables
	JButton addFeedButton, closeButton;
	Vector library, entry;
	// JList<String[]> libraryList, entryList;
	JTextField addFeedText;
	ImageIcon comicImage;
	JLabel comicLabel;
	FileService libraryFile;
	ComicFeed feed;
	JPanel libraryListPanel, entryListPanel, listPanel;
	JScrollPane comicPanel = new JScrollPane();
	DefaultListModel libraryModel = new DefaultListModel();
	DefaultListModel entryModel = new DefaultListModel();
	
	JList libraryList;
	JList entryList = null;
	// ImagePanel imagePanel;
	
	int selectedLibrary, selectedEntry = 0;
	
	// two arrays for testing the JLists
	String[] libraryNames = null;
	String[][] entries = null;
    String[][] libraryItems = null;
	
	// main method
	public static void main(String args[])
	{ MainReader reader = new MainReader(); }
	
	public MainReader()
	{
		// constructor
		libraryFile = new FileService();
		libraryFile.setType("library");
		libraryItems = libraryFile.getLibraryList();
		
		listPanel = new JPanel(new GridLayout(3,1));
        listPanel.setMaximumSize(new Dimension(450, 200));
		
		// parts of the listPanel
		libraryListPanel = new JPanel(); // goes inside listPanel
		entryListPanel = new JPanel(); // goes inside listPanel
		//JPanel addTextPanel = new JPanel();
		//addFeedText = new JTextField(20);
		//addFeedText.setFont(new Font("Default", Font.PLAIN, 20));
		//addFeedText.setSize(800, 20);
		
		// System.out.println("libraryItems.length: " + libraryItems.length);
		this.getLibrary(libraryItems.length);
		libraryList = new JList(libraryModel);
		libraryList.setSelectedIndex(0);
		libraryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		libraryList.setFixedCellWidth(415);
		libraryList.addListSelectionListener(new ListListener());
		
		try {
		    // System.out.println("Before getting entrylist: " + libraryList.getSelectedIndex(0));
		    this.getEntryList(0);
		    } catch (Exception f) {System.out.println("MainReader: " + f);}
		
		// imagePanel.setPreferredSize(new Dimension(420, 200));
		entryList = new JList(entryModel);
		// entryList.setSelectedIndex(0);
		entryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		entryList.setFixedCellWidth(415);
		entryList.addListSelectionListener(new ListListener());
		
		comicImage = new ImageIcon("KomixNavigator.png");
		comicLabel = new JLabel(comicImage);
		comicPanel = new JScrollPane(comicLabel);
		comicPanel.setPreferredSize(new Dimension(420,300));
		
		addFeedButton = new JButton("Add Feed");
		closeButton = new JButton("Close");
		
		libraryListPanel.setPreferredSize(new Dimension(420, 200));
		libraryListPanel.add(libraryList);
		entryListPanel.setPreferredSize(new Dimension(420, 200));
		entryListPanel.add(entryList);
		
		// adding a feed will be for a future version 
		//addTextPanel.setPreferredSize(new Dimension(50, 50));
		//addTextPanel.add(addFeedButton);
		//addTextPanel.add(addFeedText);
		
		listPanel.add(new JScrollPane(libraryListPanel), BorderLayout.NORTH);
		listPanel.add(new JScrollPane(entryListPanel), BorderLayout.SOUTH);
		//listPanel.add(addTextPanel, BorderLayout.SOUTH);
		//listPanel.add(addFeedText);
		
		// comicPanel.add(imagePanel);
		
		this.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent event) 
  	 				{shutDown();}
			}	  
		 );
		
		Container c = this.getContentPane();                      
		c.setLayout(new GridLayout(1,2));
		c.add(listPanel);
		c.add(comicPanel);
		this.setSize(900,425);
		this.setVisible(true);
	}
	
	// methods to get library list and library entry list
	public void getLibrary(int length)
	{
        for (int i = 0; i < length; i++) {
                    libraryModel.addElement(libraryItems[i][0]);
                    // System.out.println(libraryItems[i][0]);
                }
	}
	
	public void getEntryList(int c) throws Exception
	{
	    // System.out.println("Library index: " + c);
	    entries = null;
	    // System.out.println("This is the selected comic: " + libraryItems[c][1]);
	    // System.out.println("Size of model after clearing: " + entryModel.size());
	    // System.out.println(comic);

	    feed = new ComicFeed(libraryItems[c][1]);
	    try {
	        // System.out.println("Size of ENTRIES before getting comics: " + entries.length);
            entries = feed.getComicEntries();
            entryModel.setSize(entries.length);
            // System.out.println("New size of entryModel: " + entryModel.getSize());
            // System.out.println("Size of ENTRIES after getting comics: " + entries.length);
        } catch (Exception e) {
            System.out.println("getEntriesList" + e);}
        // System.out.println("Entries entries length: " + entries.length);
        if (entries.length != 0) {
            //entryModel.clear();
            // System.out.println("Cleared EntryModel length: " + entryModel.getSize());
            int index = 0;
            for(int i = (entries.length - 1); i > -1; i--) {
                // System.out.println("This is i: " + i);
                // System.out.println("Entries length: " + entries.length);
                // System.out.println("Item here: " + entries[i][0]);
                entryModel.addElement(entries[i][0]);
                // System.out.println("Item at entryModel[" + index +"]: " + entryModel.size());
                index++;
                // System.out.println(items[i]);
            }
            entryModel.trimToSize();
        }
        entryList.setModel(entryModel);
        //entryList.setSelectedIndex(0);
       entryList.updateUI();
       entryList.repaint();
       entryListPanel.updateUI();
       entryListPanel.repaint();
       //entryListPanel.revalidate();
       getComic(0);
	}
	
	public void getComic(int c){
	    FileService comicFile = new FileService();
	    String downloadedPath = null;
	    String[] tryImage = new String[2];
	    // System.out.println("c = " + c);
	    tryImage[0] = entries[c][1];
	    tryImage[1] = entries[c][2];
	    try {
	        downloadedPath = comicFile.downloadComic(tryImage);
	        //System.out.println("DownloadPath: " + downloadedPath);
	    } catch (Exception e) {System.out.println("getComic" + e);}
	    
	    comicImage = new ImageIcon(downloadedPath);
	    comicLabel.setIcon(comicImage);

	    comicPanel.updateUI();
	    comicPanel.repaint();
	    comicPanel.revalidate();
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
	}
	
    public void changeImage(String newImageFileName) {
		//This will change the image.  We can call this function and the image shown by the scroll
		//pane will change after the function returns.
		comicImage = new ImageIcon(newImageFileName);
		return;
	}
	
	private class ListListener implements ListSelectionListener {
	    public void valueChanged(ListSelectionEvent e) {
	        // System.out.println("valueChanged");
	        if (e.getSource() == libraryList) {
	            // get entries for the list, set selectedEntry to 0
	            // get comic of selectedEntry
	            // System.out.println("It's the library list.");
	            try {
	                // System.out.println(libraryItems[x][1]);
	                getEntryList(libraryList.getSelectedIndex());
	            } catch (Exception f) {System.out.println("changeImage" + f);}
	        }
	        else
	            // System.out.println("It's the comic");
	        // System.out.println("Selected index: " + entryList.getSelectedIndex());
	            getComic(entryList.getSelectedIndex());
	    }
	    
	}
	
	public void shutDown()
	{
		this.dispose();
	   System.exit(0);  // terminate
	}
}

