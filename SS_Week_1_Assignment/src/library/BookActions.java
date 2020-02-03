package library;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookActions extends Actions{
	
	public BookActions(String filePath) {
		super(filePath);
		readCSV();
	}
	
	void readCSV() {
		try {
			FR = new FileReader(filePath); 
			BR = new BufferedReader(FR);
	
			while ((line = BR.readLine()) != null) {
				String[] bookInfo = line.split(","); // bookInfo contains: id, name, authorID, publisherID
				if (bookInfo.length == 4) {
					int id = Integer.parseInt(bookInfo[0]);
					int authorID = Integer.parseInt(bookInfo[2]);
					int publisherID = Integer.parseInt(bookInfo[3]);
					Book a = new Book(id, bookInfo[1], authorID, publisherID);
					map.put(id, a);
					lastId = id;
				}
			}
			BR.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	void create() {
		JPanel bookPanel = new JPanel();
		JTextField name = new JTextField("", 10);
		bookPanel.add(new JLabel("Name:"));
		bookPanel.add(name);
		String[] authors = getArrayOfIdNames(driver.authorActions.map);
		JComboBox<String> chooseAuthor = new JComboBox<String>(authors);
		bookPanel.add(new JLabel("Author:"));
		bookPanel.add(chooseAuthor);
		String[] publishers = getArrayOfIdNames(driver.publisherActions.map);
		JComboBox<String> choosePublisher = new JComboBox<String>(publishers);
		bookPanel.add(new JLabel("Publisher:"));
		bookPanel.add(choosePublisher);
		String[] options = {"Create", "Cancel"};
		
		int ok = JOptionPane.showOptionDialog(driver.frame, bookPanel, "Add a new book",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (ok == JOptionPane.OK_OPTION) {
		    if ("".equals(name.getText()) || name.getText().matches(".*\\d.*")) { // name cannot be empty or have numbers
		    	JOptionPane.showMessageDialog(driver.frame, "Name cannot be empty or contain numbers",
		    		    "Bad Name Entered", JOptionPane.ERROR_MESSAGE);
		    	create(); // keep trying
		    }
		    else { // a good name was entered
		    	lastId++;
		    	String selectedAuthorIdName = String.valueOf(chooseAuthor.getSelectedItem());
		    	String selectedPublisherIdName = String.valueOf(choosePublisher.getSelectedItem()); 
		    	int aId = Integer.parseInt(selectedAuthorIdName.split(" ")[0]); // the id of the selected author
		    	int pId = Integer.parseInt(selectedPublisherIdName.split(" ")[0]); // the id of the selected publisher
		    	Book b = new Book(lastId, name.getText(), aId, pId);
		    	map.put(b.getId(), b); // the new author to authorsMap
		    	String[] newBook = {String.valueOf(b.getId()), b.getName(), String.valueOf(aId), String.valueOf(pId)};
		    	driver.tableModel.addRow(newBook);
		    }
		}
	}
	void delete() {
		int id = Integer.valueOf((String)(driver.tableModel.getValueAt(driver.row, 0))); // id of the selected book
		map.remove(id); // remove the selected book row from the books HashMap
		driver.tableModel.removeRow(driver.row); // remove row from the table on display
	}
	void update() {
		int id = Integer.valueOf((String)(driver.tableModel.getValueAt(driver.row, 0))); // id of the selected book
		Book b = (Book) map.get(id); // selected book
	
		JPanel bookPanel = new JPanel();
		bookPanel.add(new JLabel("Name:"));
		JTextField name = new JTextField(b.getName(), 10);
		bookPanel.add(name);
		String[] authors = getArrayOfIdNames(driver.authorActions.map);
		JComboBox<String> chooseAuthor = new JComboBox<String>(authors);
		bookPanel.add(new JLabel("Author:"));
		bookPanel.add(chooseAuthor);
		String[] publishers = getArrayOfIdNames(driver.publisherActions.map);
		JComboBox<String> choosePublisher = new JComboBox<String>(publishers);
		bookPanel.add(new JLabel("Publisher:"));
		bookPanel.add(choosePublisher);
		String[] options = {"Save", "Cancel"};
		
		int ok = JOptionPane.showOptionDialog(driver.frame, bookPanel, "Update book",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (ok == JOptionPane.OK_OPTION) {
		    if ("".equals(name.getText()) || name.getText().matches(".*\\d.*")) { // name cannot be empty or have numbers
		    	JOptionPane.showMessageDialog(driver.frame, "Name cannot be empty or contain numbers",
		    		    "Bad Name Entered", JOptionPane.ERROR_MESSAGE);
		    	update(); // keep trying
		    }
		    else { // a good name was entered
		    	b.setName(name.getText());
		    	String selectedAuthorIdName = String.valueOf(chooseAuthor.getSelectedItem());
		    	String selectedPublisherIdName = String.valueOf(choosePublisher.getSelectedItem()); 
		    	int aId = Integer.parseInt(selectedAuthorIdName.split(" ")[0]); // the id of the selected author
		    	int pId = Integer.parseInt(selectedPublisherIdName.split(" ")[0]); // the id of the selected publisher
		    	b.setAuthorID(aId);
		    	b.setPublisherID(pId);
		    	map.put(id, b); // update the book in the books HashMap
		    	driver.tableModel.setValueAt(b.getName(), driver.row, 1);
		    	driver.tableModel.setValueAt(b.getAuthorID(), driver.row, 2);
		    	driver.tableModel.setValueAt(b.getPublisherID(), driver.row, 3);
		    }
		}
	}
}
