package library;

public class Book extends Item{
	protected int authorID;
	protected int publisherID;
	
	public Book(int id, String name, int authorID, int publisherID) {
		super(id, name);
		this.authorID = authorID;
		this.publisherID = publisherID;
	}
	
	public String getLine() {
		return super.getLine() + "," + Integer.toString(authorID) + "," + Integer.toString(publisherID);
	}
	
	protected int getAuthorID() {
		return authorID;
	}
	protected void setAuthorID(int authorID) {
		this.authorID = authorID;
	}
	protected int getPublisherID() {
		return publisherID;
	}
	protected void setPublisherID(int publisherID) {
		this.publisherID = publisherID;
	}
	
	
}
