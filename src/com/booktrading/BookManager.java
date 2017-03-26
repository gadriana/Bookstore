package com.booktrading;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

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
		File source = new File("booklist.csv");
		if (!source.exists()) {
			return false;
		}
		try (Reader in = new FileReader(source)) {
			Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
			for (CSVRecord record : records) {
				Book b = new Book();
				b.setTitle(record.get("Title"));
				b.setAuthor(record.get("Author"));
				b.setPrice(Double.parseDouble(record.get("Price")));
				b.setPublisher(record.get("Publisher"));
				b.setForeignBook(Boolean.parseBoolean(record.get("FL")));
				b.setInStock(Integer.parseInt(record.get("Stock")));
				b.setId(UUID.fromString(record.get("ID")));
				bookList.add(b);

			}

		} catch (RuntimeException e) {
			throw new IOException(e);
		}
		return true;
	}

	public void saveToFile() throws IOException {
		try (Writer out = new FileWriter("booklist.csv")) {

			final CSVPrinter printer = CSVFormat.DEFAULT
					.withHeader("Title", "Author", "Price", "Publisher", "FL", "Stock", "ID").print(out);
			for (Book book : bookList) {
				printer.printRecord(book.getTitle(), book.getAuthor(), book.getPrice(), book.getPublisher(), book.isForeignBook(),book.getInStock(), book.getId());

			}
		}

	}

}
