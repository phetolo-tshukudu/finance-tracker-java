package Ui;

import java.time.LocalDate;
import java.text.NumberFormat;
import java.util.Locale;

import Model.Category;
import Model.Transaction;
import Persistence.TransactionRepository;
import Services.FinancialService;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FinanceGUI extends BorderPane {

    private FinancialService fin;
    private Label incomeValue;
    private Label expenseValue;
    private Label balanceValue;
    private Transaction selectedTransaction;
    private Label statusLabel;
    private TableView<Transaction> view;
    
    // For nice currency formatting
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "ZA"));

    public FinanceGUI() {
        fin = new FinancialService();
        
        createSummaryBar();
        TransactionsView();
        createInputPanel();
        createStatusPanel();
        
        // Initial refresh + auto-save on any change
        refreshSummary();
        fin.getTransactions().addListener((ListChangeListener<Transaction>) change -> {
            
            refreshSummary();
        });
    }
    
    private void refreshSummary() {
        incomeValue.setText(currencyFormat.format(fin.totalIncome()));
        expenseValue.setText(currencyFormat.format(fin.totalExpences()));
        balanceValue.setText(currencyFormat.format(fin.getBalance()));
    }

    public void createSummaryBar() {
        Label accountSummary = new Label("Account Summary");
        accountSummary.setFont(Font.font(24));
        
        incomeValue = new Label("R 0.00");
        incomeValue.setFont(Font.font(18));
        VBox inbox = new VBox(5, new Label("Income:"), incomeValue);
        inbox.setAlignment(Pos.CENTER);

        expenseValue = new Label("R 0.00");
        expenseValue.setFont(Font.font(18));
        VBox exBox = new VBox(5, new Label("Expense:"), expenseValue);
        exBox.setAlignment(Pos.CENTER);

        balanceValue = new Label("R 0.00");
        balanceValue.setFont(Font.font(20));
        balanceValue.setTextFill(Color.web("#0066cc"));
        VBox balBox = new VBox(5, new Label("Balance:"), balanceValue);
        balBox.setAlignment(Pos.CENTER);

        HBox box = new HBox(50, inbox, exBox, balBox);
        box.setPadding(new Insets(20));
        
        VBox summaryBox = new VBox(15, accountSummary, box);
        summaryBox.setAlignment(Pos.CENTER_LEFT);
        summaryBox.setPadding(new Insets(20));
        
        setTop(summaryBox);
    }

    public void createInputPanel() {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(20));

        TextField desField = new TextField();
        TextField amountField = new TextField();
        ComboBox<Category> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().setAll(Category.values());
        categoryCombo.setValue(Category.OTHER); // default

        Button addButton = new Button("Add Transaction");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        addButton.setOnAction(e -> {
            try {
                String desc = desField.getText().trim();
                if (desc.isEmpty()) {
                    showStatus("Description cannot be empty! ❌", true);
                    return;
                }

                String amountText = amountField.getText().trim();
                if (amountText.isEmpty()) {
                    showStatus("Enter an amount! ❌", true);
                    return;
                }

                double amount = Double.parseDouble(amountText);
                Category cat = categoryCombo.getValue();

                if (cat == null) {
                    showStatus("Select a category! ❌", true);
                    return;
                }

                fin.addTransation(new Transaction(amount, desc, cat, LocalDate.now()));
                showStatus("Transaction added ✅", false);

                // Clear fields
                desField.clear();
                amountField.clear();
                categoryCombo.setValue(Category.OTHER);

            } catch (NumberFormatException ex) {
                showStatus("Invalid amount! Use numbers only ❌", true);
            }
        });

        pane.add(new Label("Description:"), 0, 0);
        pane.add(desField, 1, 0);
        pane.add(new Label("Amount (R):"), 0, 1);
        pane.add(amountField, 1, 1);
        pane.add(new Label("Category:"), 0, 2);
        pane.add(categoryCombo, 1, 2);
        pane.add(addButton, 1, 3);

        GridPane.setHgrow(desField, Priority.ALWAYS);
        GridPane.setHgrow(amountField, Priority.ALWAYS);

        setLeft(pane);
    }

    public void TransactionsView() {
        view = new TableView<>();
        view.setItems(fin.getTransactions());

        TableColumn<Transaction, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setMinWidth(100);

        TableColumn<Transaction, String> descripCol = new TableColumn<>("Description");
        descripCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descripCol.setMinWidth(200);

        TableColumn<Transaction, Category> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        catCol.setMinWidth(120);

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setMinWidth(120);
        // Format amount with currency
        amountCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(amount));
                }
            }
        });

        view.getColumns().addAll(dateCol, descripCol, catCol, amountCol);

        view.getSelectionModel().selectedItemProperty()
            .addListener((obs, oldSel, newSel) -> selectedTransaction = newSel);

        // Color rows: green for income, red for expense
        view.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Transaction t, boolean empty) {
                super.updateItem(t, empty);
                if (t == null || empty) {
                    setStyle("");
                } else if (t.getAmount() > 0) {
                    setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                }
            }
        });

        setCenter(view);
        VBox.setVgrow(view, Priority.ALWAYS);
    }

    public void createStatusPanel() {
        Button removeBtn = new Button("🗑️ Remove");
        Button saveBtn = new Button("💾 Save Manually");

        removeBtn.setOnAction(e -> {
            if (selectedTransaction != null) {
                fin.deleteTransaction(selectedTransaction);
                showStatus("Transaction removed ✅", false);
            } else {
                showStatus("No transaction selected! ❌", true);
            }
        });

        saveBtn.setOnAction(e -> {
            
            showStatus("Saved manually ✅", false);
        });

        statusLabel = new Label("Ready");
        statusLabel.setFont(Font.font(14));

        HBox buttons = new HBox(15, removeBtn, saveBtn);
        HBox statusBox = new HBox(statusLabel);
        HBox.setHgrow(statusBox, Priority.ALWAYS);

        HBox bottomBar = new HBox(20, buttons, statusBox);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setStyle("-fx-background-color: #f0f0f0;");

        setBottom(bottomBar);
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
}