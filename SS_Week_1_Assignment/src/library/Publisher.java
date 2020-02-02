package library;

public class Publisher extends Item{
	String address;
	
	public Publisher(int id, String name, String address) {
		super(id, name);
		this.address = address;
	}
	
	public String getLine() {
		return super.getLine() + "," + address;
	}

	protected String getAddress() {
		return address;
	}

	protected void setAddress(String address) {
		this.address = address;
	}
	
	
}
