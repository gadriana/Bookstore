package com.booktrading;

import java.io.IOException;

import com.booktrading.ui.MainWindow;

public class Bookstore {

	public static void main(String[] args) throws Exception {
		BookManager manager = new BookManager();
		if (manager.loadFromFile()) {
			System.out.println("I have loaded the Books from the file,madam!");
		} else {
			manager.addBook(new Book("Joker for dummies", "Adriana", 24.98, "Painer", false, 23));
			manager.addBook(new Book("Joker Media", "Adriana", 4.98, "Painer", false, 3));
			manager.addBook(new Book("Joker Batman", "Adriana", 34.98, "Painer", false, 15));
			manager.addBook(new Book("Joker Poker", "Adriana", 104.98, "Painer", true, 7));
			manager.addBook(new Book("Casa Diva", "Adriana", 64.98, "Painer", false, 231));
			manager.addBook(new Book("Casa Viva", "Adriana", 84.98, "Painer", false, 398));
			manager.addBook(new Book("Mada Faka", "Adriana", 94.98, "Painer", false, 156));
			manager.addBook(new Book("Mada Joker", "Adriana", 154.98, "Painer", true, 75));
		}
		//Book searchExactMatch = manager.searchExactMatch("Joker Media");
		//System.out.println(searchExactMatch.getInStock());
		// manager.sellBook(searchExactMatch, 1);
	//	for (Book b : manager.showAllBooks()) {
//			System.out.println(b.getTitle() + " , " + b.getInStock() + " in stock");
//		}
		new MainWindow(manager).setVisible(true);
		manager.saveToFile();

	}

}
