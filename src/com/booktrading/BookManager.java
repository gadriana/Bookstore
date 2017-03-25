package com.booktrading;

import java.io.*;
import java.util.*;

public class BookManager {

	private List<Book> bookList = new ArrayList<>();

	public UUID addBook(Book newBook) {
		newBook.setId(UUID.randomUUID());
		bookList.add(newBook);
		return newBook.getId();
	}

	public List<Book> searchBook(String searchedTitle) {
		List<Book> results = new ArrayList<>();
		for (int i = 0; i < bookList.size(); i++) {
			Book currentBook = bookList.get(i);
			if (currentBook.getTitle().contains(searchedTitle)) {
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

	public Book findBook(UUID id) {
		for (Book book : bookList) {
			if (book.getId().equals(id)) {
				return book;
			}
		}
		throw new IllegalArgumentException("No such book: " + id);
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

	public boolean loadFromFile() throws IOException {
		File source = new File("booklist");
		if (!source.exists()) {
			return false;
		}
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(source))) {
			bookList = (List<Book>) in.readObject();
		} catch (ClassNotFoundException | ClassCastException e) {
			throw new IOException(e);
		}
		return true;
	}

	public void saveToFile() throws IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("booklist"))) {

			out.writeObject(bookList);
		}

	}

}
