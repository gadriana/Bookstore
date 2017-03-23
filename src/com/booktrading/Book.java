package com.booktrading;

import java.util.UUID;

public class Book {

	private String title;
	private String author;
	private double price;
	private String publisher;
	private boolean isForeignBook;
	private int inStock;
	private UUID id;
	
	
	

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public boolean isForeignBook() {
		return isForeignBook;
	}
	public void setForeignBook(boolean isForeignBook) {
		this.isForeignBook = isForeignBook;
	}
	public int getInStock() {
		return inStock;
	}
	public void setInStock(int inStock) {
		this.inStock = inStock;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	
}
