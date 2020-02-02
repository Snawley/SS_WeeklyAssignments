package library;

public class Item {
	
	protected int id;
	protected String name;
	
	public Item(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getLine() {
		return Integer.toString(id) + "," + name;
	}
	
	protected int getId() {
		return id;
	}
	protected String getName() {
		return name;
	}
	protected void setName(String name) {
		this.name = name;
	}
	
	
	
}
