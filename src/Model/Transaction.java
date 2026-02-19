package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transaction {
	private int id;
	private double amount;
	private String description;
	private Category category;
	private LocalDate date;
	
	public Transaction(double amount, String description, Category category, LocalDate date) {
		this.amount = amount;
		this.description = description;
		this.category = category;
		this.date = date;
	}
	
	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public Transaction(int id, double amount, String description, Category category, LocalDate date) {
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.category = category;
		this.date = date;
	}



	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public boolean isIncome() {
		if(amount>0) 
			return true;
		
		return false;
	}
	
	public boolean isValid() {
		if(amount==0) 
			return false;
		return true;
	}
}
