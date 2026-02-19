package Persistence;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Model.Category;
import Model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransactionRepository {
	
	private static Connection connection;
	public TransactionRepository() {
		con(); //initializes connection
	}
	private void con() {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:C:\\sqlite\\Finance_Database.db");
			Statement stmt = connection.createStatement();
			
			stmt.execute("CREATE TABLE IF NOT EXISTS transactions("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "amount REAL NOT NULL,"
					+ "description TEXT NOT NULL,"
					+ "category TEXT,"
					+ "date TEXT NOT NULL"
					+ ");");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public  void saveTransaction(Transaction t) {
		try {
			PreparedStatement pstmt = connection.prepareStatement("INSERT INTO transactions(amount,description,category,date) VALUES(?,?,?,?)");
			pstmt.setDouble(1, t.getAmount());
			pstmt.setString(2, t.getDescription());
			pstmt.setString(3, t.getCategory().toString());
			pstmt.setString(4, t.getDate().toString());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				t.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeTransaction(Transaction t) {
			try {
				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM transactions Where id=?");
				pstmt.setInt(1, t.getId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static ObservableList<Transaction> loadTransaction(){
		ObservableList<Transaction> loaded = FXCollections.observableArrayList();
		
		try {
			ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM transactions");
			while(rs.next()) {
				Transaction t = new Transaction(Integer.parseInt(rs.getString("id")),rs.getDouble("amount"),rs.getString("description"),Category.valueOf(rs.getString("category")),LocalDate.parse(rs.getString("date")));
				loaded.add(t);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return loaded;
		
	}
}
