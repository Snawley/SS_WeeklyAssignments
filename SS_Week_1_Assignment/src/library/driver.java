package library;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * @author John Butler
 *
 */
public class driver {
	static AuthorActions authorActions;
	static PublisherActions publisherActions;
	static BookActions bookActions;

	static JFrame frame;
	static JPanel panel;
	static JTable table; 
	static DefaultTableModel tableModel;
	
	static int row; // current row that is selected in the table

	public static void displayTable(String[] columnNames, Actions actions) {
		frame = new JFrame(); // frame holds panel
		frame.setSize(500, 500); // size of the JFrame	
		panel = new JPanel(); // holds a table and buttons
		Object[][] data = actions.getData(); // get an array of rows from the HashMap
		tableModel = new DefaultTableModel(data, columnNames); // make a DefaultTableModel from the data and column names
		table = new JTable(tableModel);
		table.setRowSelectionAllowed(true);  // you can select rows
		ListSelectionModel select = table.getSelectionModel();  
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // you can only select one row at a time
        
        row = -1; // no rows are currently selected
        select.addListSelectionListener(new ListSelectionListener() {  
        	// when a row is selected, store the row number in row
            public void valueChanged(ListSelectionEvent e) {  
            	if(! e.getValueIsAdjusting()) row = table.getSelectedRow();
            }
        });
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane); // add table to the panel
		
		JButton createButton = new JButton("Create new");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actions.create();
			}
		});
		panel.add(createButton);
		
		JButton delete = new JButton("Delete selected");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				// if row == -1, no rows are currently selected 
				System.out.println(row);
				if(row != -1) actions.delete(); // make sure a row is selected
			}
		});
		panel.add(delete);

		JButton update = new JButton("Update selected");
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if row == -1, no rows are currently selected 
				if(row != -1) actions.update(); // make sure a row is selected
			}
		});
		panel.add(update);
		
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
		String[] options = {"Authors", "Publishers", "Books", "Save and Exit"};
		int option = JOptionPane.showOptionDialog(null, "Welcome to my mini-database. Press an option to continue:", "Library mini-database",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[3]);
		switch(option) {
			case 0: // display authors
				String[] authorColumns = {"ID", "Name"};
				displayTable(authorColumns, authorActions);
				break;
				
			case 1: // display publishers
				String[] publisherColumns = {"ID", "Name", "Address"};
				displayTable(publisherColumns, publisherActions);
				break;
			
			case 2: // display books
				String[] bookColumns = {"ID", "Name", "Author ID", "Publisher ID"};
				displayTable(bookColumns, bookActions); 
				break;
			case 3: // write to CSV files 
				authorActions.writeCSV();
				publisherActions.writeCSV();
				bookActions.writeCSV();	
				break;
		}
	}
	
	public static void main(String[] args) {
		authorActions = new AuthorActions("resources/authors.csv"); // file holds authors
		publisherActions = new PublisherActions("resources/publishers.csv"); // file holds authors
		bookActions = new BookActions("resources/books.csv"); // file holds authors
	
		mainMenu(); // allows you to view Authors, Publishers, Books, or Quit
	}

}
