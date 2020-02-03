package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Actions {

	String filePath;
	FileReader FR;
	BufferedReader BR;
	HashMap<Integer, Item> map;
	String line; // will hold each line of a CSV file
	int lastId;
	
	public Actions(String filePath) {
		this.filePath = filePath;
		map = new HashMap<Integer, Item>(); // empty HashMap
	}
	
	Object[][] getData() {
		// returns an Object[][] from the HashMap<Integer, Item> map
		Object[][] data = new Object[map.size()][];
		int index = 0;
		for(Item item : map.values()) {
			data[index] = (item.getLine()).split(",");
			index++;
		}
		return data;
	}
	
	public static String[] getArrayOfIdNames(HashMap<Integer, Item> itemsMap) {
		// returns a String[] of "ID - Name" from the input HashMap
		String[] arr = new String[itemsMap.size()];
		int index = 0;
		for(Item i : itemsMap.values()) {
			arr[index] = Integer.toString(i.getId()) + " - " + i.getName();
			index++;
		}
		return arr;
	}
	void create() {} // abstract method
	void delete() {} // abstract method
	void update() {} // abstract method
	
	void writeCSV() {	
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
}
