package library;
import java.io.FileReader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author John Butler
 *
 */
public class driver {

	static JFrame frame;
	static JPanel panel;
	static JTable table; 
	static DefaultTableModel tableModel;
	
	protected static int lastAuthorId;
	protected static int lastPublisherId;
	protected static int lastBookId;
	
	static HashMap<Integer, Item> authorsMap = new HashMap<Integer, Item>(); // empty HashMap for authors
	static HashMap<Integer, Item> publishersMap = new HashMap<Integer, Item>(); // empty HashMap for publishers
	static HashMap<Integer, Item> booksMap = new HashMap<Integer, Item>(); // empty HashMap for books
	
	static int row = -1; // current row that is selected in the table
	
	public static void writeCSV(String filePath, HashMap<Integer, Item> map) {	
		// writes the data in map into the CSV located at filePath
		try {
			FileWriter fw = new FileWriter(filePath, false); // overwrite the file already existing
			map.forEach((id, item) -> { // lambda expression to iterate through items in map
				try { // add the line to the CSV file
					fw.append(item.getLine()); 
					fw.append('\n');
				} 
				catch (IOException e) { // if append fails
					e.printStackTrace();
				}
			});
			fw.close(); // close the CSV file
		}
		catch(Exception e) { // if creating or closing the FileWriter fails
			System.out.println(e);
		}
	}

	public static Object[][] getData(HashMap<Integer, Item> itemsMap){
		// returns an Object[][] from the input HashMap<Integer, Item> 
		Object[][] data = new Object[itemsMap.size()][];
		int index = 0;
		for(Item item : itemsMap.values()) {
			data[index] = (item.getLine()).split(",");
			index++;
		}
		return data;
	}
	public static String[] getArrayOfIdNames(HashMap<Integer, Item> itemsMap) {
		String[] arr = new String[itemsMap.size()];
		int index = 0;
		for(Item i : itemsMap.values()) {
			arr[index] = Integer.toString(i.getId()) + " - " + i.getName();
			index++;
		}
		return arr;
	}
	
	public static void createAuthor() {
		JTextField name = new JTextField();
		Object[] message = {"Name:", name };
		String[] options = {"Create", "Cancel"};
		
		int ok = JOptionPane.showOptionDialog(frame, message, "Add a new author",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (ok == JOptionPane.OK_OPTION) {
		    if ("".equals(name.getText()) || name.getText().matches(".*\\d.*")) { // name cannot be empty or have numbers
		    	JOptionPane.showMessageDialog(frame, "Name cannot be empty or contain numbers",
		    		    "Bad Name Entered", JOptionPane.ERROR_MESSAGE);
		    	createAuthor(); // keep trying
		    }
		    else { // a good name was entered
		    	lastAuthorId++;
		    	Author a = new Author(lastAuthorId, name.getText());
		    	authorsMap.put(lastAuthorId, a); // the new author to authorsMap
		    	String[] newAuthor = {String.valueOf(a.getId()), a.getName()};
		    	tableModel.addRow(newAuthor);
		    }
		}
	}
	public static void createPublisher() {
		JTextField name = new JTextField();
		JTextField address = new JTextField();
		Object[] message = {"Name:", name, "Adress:", address};
		String[] options = {"Create", "Cancel"};
		
		int ok = JOptionPane.showOptionDialog(frame, message, "Add a new publisher",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (ok == JOptionPane.OK_OPTION) {
		    if ("".equals(name.getText()) || name.getText().matches(".*\\d.*")) { // name cannot be empty or have numbers
		    	JOptionPane.showMessageDialog(frame, "Name cannot be empty or contain numbers",
		    		    "Bad Name Entered", JOptionPane.ERROR_MESSAGE);
		    	createAuthor(); // keep trying
		    }
		    else if ("".equals(address.getText())) { // address cannot be empty
		    	JOptionPane.showMessageDialog(frame, "Address cannot be empty",
		    		    "Bad Address Entered", JOptionPane.ERROR_MESSAGE);
		    	createAuthor(); // keep trying
		    }
		    else { // a good name was entered
		    	lastPublisherId++;
		    	Publisher p = new Publisher(lastPublisherId, name.getText(), address.getText());
		    	publishersMap.put(p.getId(), p); // the new author to authorsMap
		    	String[] newPublisher = {String.valueOf(p.getId()), p.getName(), p.getAddress()};
		    	tableModel.addRow(newPublisher);
		    }
		}
	}
	public static void createBook() {
		JTextField name = new JTextField("", 10);
		JPanel bookPanel = new JPanel();
		bookPanel.add(new JLabel("Name:"));
		bookPanel.add(name);
		String[] authors = getArrayOfIdNames(authorsMap);
		JComboBox<String> chooseAuthor = new JComboBox<String>(authors);
		bookPanel.add(new JLabel("Author:"));
		bookPanel.add(chooseAuthor);
		String[] publishers = getArrayOfIdNames(publishersMap);
		JComboBox<String> choosePublisher = new JComboBox<String>(publishers);
		bookPanel.add(new JLabel("Publisher:"));
		bookPanel.add(choosePublisher);
		String[] options = {"Create", "Cancel"};
		
		int ok = JOptionPane.showOptionDialog(frame, bookPanel, "Add a new book",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (ok == JOptionPane.OK_OPTION) {
		    if ("".equals(name.getText()) || name.getText().matches(".*\\d.*")) { // name cannot be empty or have numbers
		    	JOptionPane.showMessageDialog(frame, "Name cannot be empty or contain numbers",
		    		    "Bad Name Entered", JOptionPane.ERROR_MESSAGE);
		    	createAuthor(); // keep trying
		    }
		    else { // a good name was entered
		    	lastBookId++;
		    	String a = String.valueOf(chooseAuthor.getSelectedItem());
		    	String p = String.valueOf(choosePublisher.getSelectedItem());
		    	Book b = new Book(lastBookId, name.getText(), 1, 2);
		    	booksMap.put(b.getId(), b); // the new author to authorsMap
		    	String[] newBook = {String.valueOf(b.getId()), b.getName(), a, p};
		    	tableModel.addRow(newBook);
		    }
		}
	}

	
	public static void displayTable(String[] columnNames, Object[][] data) {
		frame = new JFrame(); // frame holds panel
		frame.setSize(500, 500); // size of the JFrame	
		panel = new JPanel(); // holds a table and buttons
		tableModel = new DefaultTableModel(data, columnNames);
		table = new JTable(tableModel);
		table.setRowSelectionAllowed(true);  // you can select rows
		ListSelectionModel select = table.getSelectionModel();  
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // you can only select one row at a time
        select.addListSelectionListener(new ListSelectionListener() {  
            public void valueChanged(ListSelectionEvent e) {  
            	if(! e.getValueIsAdjusting()) row = table.getSelectedRow();
            }
        });
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane); // add table to the panel
		
		JButton create = new JButton("Create new");
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createBook();
			}
		});
		panel.add(create);
		/*
		JButton delete = new JButton("Delete selected");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
		panel.add(delete);
		
		JButton update = new JButton("Update selected");
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		panel.add(update);*/
		
		JButton menu = new JButton("Main Menu");
		menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				mainMenu();
			}
		});
		panel.add(menu);
		
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public static void mainMenu() {
		String[] options = {"Authors", "Publishers", "Books", "Quit"};
		int option = JOptionPane.showOptionDialog(null, "Welcome to my mini-database. Press an option to continue:", "Library mini-database",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[3]);
		switch(option) {
			case 0: // display authors
				String[] authorColumns = {"ID", "Name"};
				displayTable(authorColumns, getData(authorsMap));
				break;
				
			case 1: // display publishers
				String[] publisherColumns = {"ID", "Name", "Address"};
				displayTable(publisherColumns, getData(publishersMap));
				break;
			
			case 2: // display books
				String[] bookColumns = {"ID", "Name", "Author", "Publisher"};
				displayTable(bookColumns, getData(booksMap)); 
				break;
		}
		
		
	}
	
	
	public static void main(String[] args) {
		String authorsFilePath = "resources/authors.csv"; // file holds authors
		String publishersFilePath = "resources/publishers.csv"; // file holds authors
		String booksFilePath = "resources/books.csv"; // file holds authors
		String line = ""; // will hold each line of a CSV file
		
		//AuthorActions authorAction = new AuthorActions();
		
		try {
			// first read all authors from authors.txt
			FileReader authorsFR = new FileReader(authorsFilePath); 
			BufferedReader authorsBR = new BufferedReader(authorsFR); 
			
			while((line = authorsBR.readLine()) != null) {
				String[] authorInfo = line.split(","); // authorInfo contains: id, name
				if(authorInfo.length == 2) {
					int id = Integer.parseInt(authorInfo[0]);
					Author a = new Author(id, authorInfo[1]);
					authorsMap.put(id, a);
					lastAuthorId = id;
				}
			}
			authorsBR.close();
			
			// then read all publishers from publishers.txt
			FileReader publishersFR = new FileReader(publishersFilePath); 
			BufferedReader publishersBR = new BufferedReader(publishersFR);

			while ((line = publishersBR.readLine()) != null) {
				String[] publisherInfo = line.split(","); // publisherInfo contains: id, name, address
				if (publisherInfo.length == 3) {
					int id = Integer.parseInt(publisherInfo[0]);
					Publisher p = new Publisher(id, publisherInfo[1], publisherInfo[2]);
					publishersMap.put(id, p);
					lastPublisherId = id;
				}
			}
			publishersBR.close();
			
			// now read all books from books.txt
			FileReader booksFR = new FileReader(booksFilePath); 
			BufferedReader booksBR = new BufferedReader(booksFR);

			while ((line = booksBR.readLine()) != null) {
				String[] bookInfo = line.split(","); // bookInfo contains: id, name, authorID, publisherID
				if (bookInfo.length == 4) {
					int id = Integer.parseInt(bookInfo[0]);
					int authorID = Integer.parseInt(bookInfo[2]);
					int publisherID = Integer.parseInt(bookInfo[3]);
					Book a = new Book(id, bookInfo[1], authorID, publisherID);
					booksMap.put(id, a);
					lastBookId = id;
				}
			}
			booksBR.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
		mainMenu();
		
		writeCSV(authorsFilePath, authorsMap);
		writeCSV(publishersFilePath, publishersMap);
		writeCSV(booksFilePath, booksMap);	
	}

}
