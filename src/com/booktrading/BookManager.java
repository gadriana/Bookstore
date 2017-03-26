package com.booktrading;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class BookManager {

	private static final String DEFAULT_FILE_NAME = "booklist.csv";
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
		return loadFromFile(DEFAULT_FILE_NAME);

	}

	public boolean loadFromFile(String fileName) throws IOException {
		File source = new File(fileName);
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
		saveToFile(DEFAULT_FILE_NAME);
	}

	public void saveToFile(String target) throws IOException {
		try (Writer out = new FileWriter(target)) {

			final CSVPrinter printer = CSVFormat.DEFAULT
					.withHeader("Title", "Author", "Price", "Publisher", "FL", "Stock", "ID").print(out);
			for (Book book : bookList) {
				printer.printRecord(book.getTitle(), book.getAuthor(), book.getPrice(), book.getPublisher(),
						book.isForeignBook(), book.getInStock(), book.getId());

			}
		}
		

	}
	 public void saveTableModel(TableModel data, String target) throws IOException {
	        int columnCount = data.getColumnCount();
	        String[] header = new String[columnCount];
	        for (int i = 0; i < columnCount; i++) {
	            header[i] = data.getColumnName(i);
	        }

	        try (Writer out = new FileWriter(target)) {
	            CSVPrinter printer = CSVFormat.DEFAULT.withHeader(header).print(out);
	            for (int row = 0, len = data.getRowCount(); row < len; row++) {
	                Object[] values = new Object[columnCount];
	                for (int col = 0; col < values.length; col++) {
	                    values[col] = data.getValueAt(row, col);
	                }
	                printer.printRecord(values);
	            }
	        }
	    }

	    public TableModel loadTableModel(String source) throws IOException {
	        File file = new File(source);
	        if (!file.exists()) {
	            String[] defaultColumns = { "ID", "Title", "Author", "Price", "Publisher", "FL", "Stock" };
	            DefaultTableModel data = new DefaultTableModel(defaultColumns, 0);
	            return data;
	        }

	        try (Reader in = new FileReader(source)) {
	            CSVParser records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
	            Map<String, Integer> headerMap = records.getHeaderMap();
	            String[] header = new String[headerMap.size()];
	            for (Entry<String, Integer> entry : headerMap.entrySet()) {
	                header[entry.getValue()] = entry.getKey();
	            }

	            DefaultTableModel data = new DefaultTableModel(header, 0);
	            for (CSVRecord record : records) {
	                data.addRow(toArray(record.iterator()));
	            }
	            return data;

	        } catch (RuntimeException e) {
	            throw new IOException(e);
	        }
	    }

	    private static Object[] toArray(Iterator<?> iterator) {
	        List<Object> values = new ArrayList<>();
	        while (iterator.hasNext()) {
	            values.add(iterator.next());
	        }
	        return values.toArray();
	    }

}
