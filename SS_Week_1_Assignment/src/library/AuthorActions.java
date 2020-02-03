package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AuthorActions extends Actions{

	public AuthorActions(String filePath) {
		super(filePath);
		readCSV();
	} 
	
	void readCSV() { 
		// read all authors from authors.txt
		try {
			FR = new FileReader(filePath); 
			BR = new BufferedReader(FR); 
			while((line = BR.readLine()) != null) {
				String[] authorInfo = line.split(","); // authorInfo contains: id, name
				if(authorInfo.length == 2) {
					int id = Integer.parseInt(authorInfo[0]);
					Author a = new Author(id, authorInfo[1]);
					lastId = id;
					map.put(id, a);
				}
			}
			BR.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	void create() {
		JTextField name = new JTextField();
		Object[] message = {"Name:", name };
		String[] options = {"Create", "Cancel"};
		
		int ok = JOptionPane.showOptionDialog(driver.frame, message, "Add a new author",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (ok == JOptionPane.OK_OPTION) {
		    if ("".equals(name.getText()) || name.getText().matches(".*\\d.*")) { // name cannot be empty or have numbers
		    	JOptionPane.showMessageDialog(driver.frame, "Name cannot be empty or contain numbers",
		    		    "Bad Name Entered", JOptionPane.ERROR_MESSAGE);
		    	create(); // keep trying
		    }
		    else { // a good name was entered
		    	lastId++;
		    	Author a = new Author(lastId, name.getText());
		    	map.put(lastId, a); // the new author to authorsMap
		    	String[] newAuthor = {String.valueOf(a.getId()), a.getName()};
		    	driver.tableModel.addRow(newAuthor);
		    }
		}
	}
	void delete() {
		int id = Integer.valueOf((String)(driver.tableModel.getValueAt(driver.row, 0))); // id of the selected author
		map.remove(id); // remove the selected author row from the authors HashMap
		// new books HashMap will keep all books except for those that match the deleted author 
		HashMap<Integer, Item> newBooksMap = new HashMap<Integer, Item>();
		driver.bookActions.map.forEach((bookId, book) -> { // lambda expression to iterate through books HashMap
			// only copy books that do not match the deleted author
			if (((Book) book).getAuthorID() != id) {
				newBooksMap.put(bookId, book);
			}
		});
		driver.bookActions.map = newBooksMap; // replace the old books HashMap with the new one
		driver.tableModel.removeRow(driver.row); // remove row from the table on display
	}
	void update() {
		int id = Integer.valueOf((String)(driver.tableModel.getValueAt(driver.row, 0))); // id of the selected author
		Author a = (Author) map.get(id); // selected author
		JTextField name = new JTextField(a.getName());
		Object[] message = {"Name:", name };
		String[] options = {"Save", "Cancel"};
		
		int ok = JOptionPane.showOptionDialog(driver.frame, message, "Edit author",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (ok == JOptionPane.OK_OPTION) {
		    if ("".equals(name.getText()) || name.getText().matches(".*\\d.*")) { // name cannot be empty or have numbers
		    	JOptionPane.showMessageDialog(driver.frame, "Name cannot be empty or contain numbers",
		    		    "Bad Name Entered", JOptionPane.ERROR_MESSAGE);
		    	update(); // keep trying
		    }
		    else { // a good name was entered
		    	a.setName(name.getText());
		    	map.put(id, a); // update the author in authors HashMap
		    	driver.tableModel.setValueAt(a.getName(), driver.row, 1);
		    }
		}
	}
	
}
