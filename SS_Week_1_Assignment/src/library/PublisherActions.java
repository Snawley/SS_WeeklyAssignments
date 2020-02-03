package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class PublisherActions extends Actions{
	
	public PublisherActions(String filePath) {
		super(filePath);
		readCSV();
	}
	
	void readCSV() {
		// read all publishers from publishers.txt
		try {
			FR = new FileReader(filePath); 
			BR = new BufferedReader(FR);
	
			while ((line = BR.readLine()) != null) {
				String[] publisherInfo = line.split(","); // publisherInfo contains: id, name, address
				if (publisherInfo.length == 3) {
					int id = Integer.parseInt(publisherInfo[0]);
					Publisher p = new Publisher(id, publisherInfo[1], publisherInfo[2]);
					map.put(id, p);
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
		JTextField name = new JTextField();
		JTextField address = new JTextField();
		Object[] message = {"Name:", name, "Adress:", address};
		String[] options = {"Create", "Cancel"};
		
		int ok = JOptionPane.showOptionDialog(driver.frame, message, "Add a new publisher",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (ok == JOptionPane.OK_OPTION) {
		    if ("".equals(name.getText()) || name.getText().matches(".*\\d.*")) { // name cannot be empty or have numbers
		    	JOptionPane.showMessageDialog(driver.frame, "Name cannot be empty or contain numbers",
		    		    "Bad Name Entered", JOptionPane.ERROR_MESSAGE);
		    	create(); // keep trying
		    }
		    else if ("".equals(address.getText())) { // address cannot be empty
		    	JOptionPane.showMessageDialog(driver.frame, "Address cannot be empty",
		    		    "Bad Address Entered", JOptionPane.ERROR_MESSAGE);
		    	create(); // keep trying
		    }
		    else { // a good name was entered
		    	lastId++;
		    	Publisher p = new Publisher(lastId, name.getText(), address.getText());
		    	map.put(p.getId(), p); // the new author to authorsMap
		    	String[] newPublisher = {String.valueOf(p.getId()), p.getName(), p.getAddress()};
		    	driver.tableModel.addRow(newPublisher);
		    }
		}
	}
	void delete() {
		int id = Integer.valueOf((String)(driver.tableModel.getValueAt(driver.row, 0))); // id of the selected publisher
		map.remove(id); // remove the selected publisher row from the publishers HashMap
		// new books HashMap will keep all books except for those that match the deleted publisher 
		HashMap<Integer, Item> newBooksMap = new HashMap<Integer, Item>(); 
		driver.bookActions.map.forEach((bookId, book) -> { // lambda expression to iterate through books HashMap
			// only copy books that do not match the deleted publisher
			if(((Book) book).getPublisherID() != id) newBooksMap.put(bookId, book);
		});
		driver.bookActions.map = newBooksMap; // replace the old books HashMap with the new one
		driver.tableModel.removeRow(driver.row); // remove row from the table on display
	}
	void update() {
		int id = Integer.valueOf((String)(driver.tableModel.getValueAt(driver.row, 0))); // id of the selected publisher
		Publisher p = (Publisher) map.get(id); // selected publisher
		JTextField name = new JTextField(p.getName());
		JTextField address = new JTextField(p.getAddress());
		Object[] message = {"Name:", name, "Adress:", address};
		String[] options = {"Save", "Cancel"};
		
		int ok = JOptionPane.showOptionDialog(driver.frame, message, "Edit publisher",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (ok == JOptionPane.OK_OPTION) {
		    if ("".equals(name.getText()) || name.getText().matches(".*\\d.*")) { // name cannot be empty or have numbers
		    	JOptionPane.showMessageDialog(driver.frame, "Name cannot be empty or contain numbers",
		    		    "Bad Name Entered", JOptionPane.ERROR_MESSAGE);
		    	update(); // keep trying
		    }
		    else if ("".equals(address.getText())) { // address cannot be empty
		    	JOptionPane.showMessageDialog(driver.frame, "Address cannot be empty",
		    		    "Bad Address Entered", JOptionPane.ERROR_MESSAGE);
		    	update(); // keep trying
		    }
		    else { // a good name was entered
		    	p.setName(name.getText());
		    	p.setAddress(address.getText());
		    	map.put(id, p); // update the publisher in authors HashMap
		    	driver.tableModel.setValueAt(p.getName(), driver.row, 1);
		    	driver.tableModel.setValueAt(p.getAddress(), driver.row, 2);
		    }
		}
	}
}
