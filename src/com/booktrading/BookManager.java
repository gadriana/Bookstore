package com.booktrading;
import java.io.Serializable;
import java.util.*;

public class BookManager {

	private List<Book> bookList = new ArrayList<>();

	public UUID addBook(Book newBook) {
		newBook.setId(UUID.randomUUID());
		bookList.add(newBook);
		return newBook.getId();
	}

	public List<Book> searchBook(String searchedBook) {
		List<Book> results = new ArrayList<>();
		for (int i = 0; i < bookList.size(); i++) {
			Book currentBook = bookList.get(i);
			if (currentBook.getTitle().contains(searchedBook)) {
				results.add(currentBook);
			}
		}
		return results;
	}

	public Book searchExactMatch(String searchedBook) {
		for (int i = 0; i < bookList.size(); i++) {
			Book currentBook = bookList.get(i);
			if (currentBook.getTitle().equals(searchedBook)) {
				return currentBook;
			}
		}
		return null;
	}

	public List<Book> showAllBooks() {
		return bookList;
	}

	public boolean sellBook(Book book, int order) {
		int availability = book.getInStock();
		if (availability >= order) {
			availability = book.getInStock() - order;
			book.setInStock(availability);
			return true;
		}
		return false;

	}

	
	public boolean deleteBook(Book bookToRemove) {
		return bookList.remove(bookToRemove);
	}
	
	
	
	public void loadFromFile(){
		
	}
	
	public void saveToFile(){
		
	}
	
}
