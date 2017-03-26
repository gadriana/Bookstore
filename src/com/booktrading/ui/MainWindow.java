package com.booktrading.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
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

import com.booktrading.Book;
import com.booktrading.BookManager;

public class MainWindow extends JFrame {

	public MainWindow(BookManager manager) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Bookstore Marusia");
		setBounds(800, 500, 500, 300);
		setLayout(new BorderLayout());
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[]{"ID","Title","Author","Publisher","Price","Foreign?","Stock"});
		for (Book b : manager.showAllBooks()) {
			model.addRow(new Object[]{b.getId(),b.getTitle(),b.getAuthor(),b.getPublisher(),b.getPrice(),b.isForeignBook(),b.getInStock()});
		}
		JTable booklist = new JTable(model);
		booklist.getColumnModel().removeColumn(booklist.getColumnModel().getColumn(0));
		add(new JScrollPane(booklist));
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("MENU");
		menu.add(new JMenuItem("SEARCH"));
		menu.add(new JMenuItem(new AbstractAction("SELL") {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = booklist.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(MainWindow.this, "Please select a book");
					return;
				}
				Book book = manager.findBook((UUID) model.getValueAt(selectedRow, 0));

				String quantity = JOptionPane.showInputDialog(MainWindow.this,
						"Enter quantity", "Sell book: " + book.getTitle(), JOptionPane.PLAIN_MESSAGE);
				if (quantity == null) {
					return;
				}
				manager.sellBook(book, Integer.parseInt(quantity));
				model.setValueAt(book.getInStock(), selectedRow, 6);
				try {
					manager.saveToFile();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(MainWindow.this, e1.toString(), "Error saving", JOptionPane.ERROR_MESSAGE);					
				}
			}
		}));
		menu.add(new JMenuItem("ADD"));
		menu.add(new JMenuItem("DELETE"));
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
