package com.booktrading.ui;

import java.awt.BorderLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.booktrading.Book;
import com.booktrading.BookManager;

public class MainWindow extends JFrame {

	public MainWindow(BookManager manager) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Bookstore Marusia");
		setBounds(800, 500, 500, 300);
		setLayout(new BorderLayout());
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[] { "ID", "Title", "Author", "Publisher", "Price", "Foreign?", "Stock" });
		for (Book b : manager.showAllBooks()) {
			model.addRow(new Object[] { b.getId(), b.getTitle(), b.getAuthor(), b.getPublisher(), b.getPrice(),
					b.isForeignBook(), b.getInStock() });
		}
		JTable booklist = new JTable(model);
		booklist.getColumnModel().removeColumn(booklist.getColumnModel().getColumn(0));
		add(new JScrollPane(booklist));

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("MENU");
		menu.add(new JMenuItem(new AbstractAction("SEARCH") {
			@Override
			public void actionPerformed(ActionEvent e) {

				String searchedItem = JOptionPane.showInputDialog(MainWindow.this, "Enter the book you seek: ",
						"Search book: ", JOptionPane.PLAIN_MESSAGE);

				if (searchedItem == null) {
					return;
				}
				List<Book> results = new ArrayList<>(manager.searchBook(searchedItem));
				if (results.size() > 0) {

					DefaultTableModel model2 = new DefaultTableModel();
					model2.setColumnIdentifiers(
							new String[] { "ID", "Title", "Author", "Publisher", "Price", "Foreign?", "Stock" });
					for (Book b : results) {
						model2.addRow(new Object[] { b.getId(), b.getTitle(), b.getAuthor(), b.getPublisher(),
								b.getPrice(), b.isForeignBook(), b.getInStock() });
					}
					JTable booklist2 = new JTable(model2);
					Object message = new JScrollPane(booklist2);
					booklist2.getColumnModel().removeColumn(booklist2.getColumnModel().getColumn(0));
					JOptionPane.showMessageDialog(MainWindow.this, new JScrollPane(booklist2), "Results",
							JOptionPane.PLAIN_MESSAGE);
				} else {

					JOptionPane.showMessageDialog(MainWindow.this, "We do not have your book");
				}
			}
		}));
		menu.add(new JMenuItem(new AbstractAction("SELL") {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = booklist.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(MainWindow.this, "Please select a book");
					return;
				}
				Book book = manager.findBook((UUID) model.getValueAt(selectedRow, 0));

				String quantity = JOptionPane.showInputDialog(MainWindow.this, "Enter quantity",
						"Sell book: " + book.getTitle(), JOptionPane.PLAIN_MESSAGE);
				if (quantity == null) {
					return;
				}
				manager.sellBook(book, Integer.parseInt(quantity));
				model.setValueAt(book.getInStock(), selectedRow, 6);
				try {
					manager.saveToFile();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(MainWindow.this, e1.toString(), "Error saving",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}));
		menu.add(new JMenuItem(new AbstractAction("ADD") {
			@Override
			public void actionPerformed(ActionEvent e) {
				JScrollPane addScreen=new JScrollPane();
				//TODO fill addScreen with TextBoxes
				addScreen.add(new TextField());
				
				int result = JOptionPane.showConfirmDialog(MainWindow.this, addScreen, "Add book ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					Book b = new Book();
					//model.addRow(rowData);
				}
				
			}}));

		menu.add(new JMenuItem(new AbstractAction("DELETE") {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = booklist.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(MainWindow.this, "Please select a book");
					return;
				}
				Book book = manager.findBook((UUID) model.getValueAt(selectedRow, 0));

				// String quantity =
				// JOptionPane.showInputDialog(MainWindow.this,
				// "Enter quantity", "Delete book: " + book.getTitle(),
				// JOptionPane.PLAIN_MESSAGE);
				// if (quantity == null) {
				// return;
				// }
				manager.deleteBook(book);
				model.removeRow(selectedRow);
				try {
					manager.saveToFile();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(MainWindow.this, e1.toString(), "Error saving",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}));
		menu.addSeparator();
		menu.add(new JMenuItem(new AbstractAction("EXIT") {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		}));

		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

}
