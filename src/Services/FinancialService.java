package Services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Category;
import Model.Transaction;
import Persistence.TransactionRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FinancialService implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	ObservableList<Transaction> transactions;
	private double Balance;
	private Map<Category,ObservableList<Transaction>> categorizedList;
	private TransactionRepository service = new TransactionRepository();
	
	public FinancialService() {
		this.transactions = FXCollections.observableArrayList();
		this.categorizedList = new HashMap<>();
		refreshTransactions();
	}
	
	public void addTransation(Transaction tran) {
		if(tran != null && tran.isValid()) {
			service.saveTransaction(tran);
			refreshTransactions();
			
			
			updateBalance();
		}
	}
	
	private void refreshTransactions() {
	    transactions.setAll(service.loadTransaction());
	}
	
	

	public void deleteTransaction(Transaction tran) {
		if(tran!=null) {
			service.removeTransaction(tran);;
			
			refreshTransactions();
			
			updateBalance();
			
		}
	}
	
	public double totalIncome() {
		double result = 0.0;
		if(!transactions.isEmpty()) {
			for(Transaction t:transactions) {
				if(t.isIncome()){
					result+=t.getAmount();
				}
			}
			
		}
		return result;
	}
	
	public double totalExpences() {
		double result = 0.0;
		if(!transactions.isEmpty()) {
			for(Transaction t:transactions) {
				if(!t.isIncome()){
					result+=t.getAmount();
				}
			}
			
		}
		return result;
		
	}
	
	
	
	public ObservableList<Transaction> loadTransactions(){
		return service.loadTransaction();
	}
	
	public void updateBalance() {
		Balance= totalIncome()+totalExpences();
		
	}

	public ObservableList<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(ObservableList<Transaction> transactions) {
		this.transactions = transactions;
	}

	public double getBalance() {
		return Balance;
	}

	public void setBalance(double balance) {
		Balance = balance;
	}

	public List<Transaction> getCategorizedList(Category c) {
		return categorizedList.get(c);
	}

	
	
	
}
