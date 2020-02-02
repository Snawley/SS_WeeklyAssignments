package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Actions {

	String filePath;
	FileReader FR;
	BufferedReader BR;
	HashMap<Integer, Item> map;
	
	public Actions(String filePath) {
		this.filePath = filePath;
		try {
			FR = new FileReader(filePath); 
			BR = new BufferedReader(FR); 
		}
		catch(Exception e) {
			System.out.println(e);
		}
		map = new HashMap<Integer, Item>(); // empty HashMap
	}
}
