import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

class Account {

    private double balance; 

    public Account(double initialBalance) {
        if (initialBalance >= 0.0) {
            balance = initialBalance;
        } else {
            throw new IllegalArgumentException("Initial balance must be >= 0");
        }
    }

    public double getBalance() {
        return balance;
    }

    public void credit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            throw new IllegalArgumentException("Credit amount must be > 0");
        }
    }

    public boolean debit(double amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Debit amount exceeded account balance");
        } 
        if (amount <= 0) {
            throw new IllegalArgumentException("Debit amount must be > 0");
        }
        balance -= amount;
        return true;
    }
}

class SavingsAccount extends Account {

    private double interestRate; 

    public SavingsAccount(double initialBalance, double interestRate) {
        super(initialBalance);
        if (interestRate >= 0.0) {
            this.interestRate = interestRate;
        } else {
            throw new IllegalArgumentException("Interest rate must be >= 0");
        }
    }

    public double calculateInterest() {
        return getBalance() * interestRate;
    }

    public void applyMonthlyInterest() {
        double interest = calculateInterest();
        credit(interest);
    }
}

class CheckingAccount extends Account {

    private double fee; 

    public CheckingAccount(double initialBalance, double fee) {
        super(initialBalance);
        if (fee >= 0.0) {
            this.fee = fee;
        } else {
            throw new IllegalArgumentException("Fee must be >= 0");
        }
    }

    @Override
    public void credit(double amount) {
        super.credit(amount);
        super.debit(fee); 
    }

    @Override
    public boolean debit(double amount) {
        boolean success = super.debit(amount);
        if (success) {
            super.debit(fee);
        }
        return success;
    }
}

public class BankGUI extends JFrame {

    private JTextField balanceField, amountField, interestField, feeField;
    private JTextArea outputArea;
    private JButton creditButton, debitButton, interestButton, applyInterestButton;
    private JComboBox<String> accountTypeCombo;
    private JLabel statusLabel;

    private Account currentAccount;

    public BankGUI() {
        super("Bank Account System");

        setLayout(new BorderLayout());

        initInputPanel();
        initOutputArea();
        initButtonPanel();
        initStatusBar();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        updateButtonsState();
    }

    private void initInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        JLabel initialBalanceLabel = new JLabel("Initial Balance:");
        initialBalanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(initialBalanceLabel);
        balanceField = new JTextField();
        inputPanel.add(balanceField);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(amountLabel);
        amountField = new JTextField();
        inputPanel.add(amountField);

        JLabel interestRateLabel = new JLabel("Interest Rate (Savings):");
        interestRateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(interestRateLabel);
        interestField = new JTextField();
        inputPanel.add(interestField);

        JLabel feeLabel = new JLabel("Fee (Checking):");
        feeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(feeLabel);
        feeField = new JTextField();
        inputPanel.add(feeField);

        JLabel accountTypeLabel = new JLabel("Select Account Type:");
        accountTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(accountTypeLabel);

        accountTypeCombo = new JComboBox<>(new String[]{"Savings Account", "Checking Account"});
        inputPanel.add(accountTypeCombo);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBackground(Color.BLACK);
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.addActionListener(e -> createAccount());
        inputPanel.add(new JLabel()); 
        inputPanel.add(createAccountButton);

        add(inputPanel, BorderLayout.NORTH);
    }

    private void initOutputArea() {
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
    }

    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();

        creditButton = createButton("Credit", Color.BLACK, Color.WHITE, e -> doCredit());
        debitButton = createButton("Debit", Color.BLACK, Color.WHITE, e -> doDebit());
        interestButton = createButton("Calculate Interest", Color.BLACK, Color.WHITE, e -> doCalculateInterest());
        applyInterestButton = createButton("Apply Monthly Interest", Color.BLACK, Color.WHITE, e -> doApplyMonthlyInterest());

        buttonPanel.add(creditButton);
        buttonPanel.add(debitButton);
        buttonPanel.add(interestButton);
        buttonPanel.add(applyInterestButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initStatusBar() {
        statusLabel = new JLabel("Welcome! Please create an account.");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.PAGE_END);
    }

    private JButton createButton(String text, Color bg, Color fg, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.addActionListener(action);
        return button;
    }

    private void createAccount() {
        try {
            double balance = parseDouble(balanceField.getText(), "Invalid initial balance");
            String selectedType = (String) accountTypeCombo.getSelectedItem();

            if ("Savings Account".equals(selectedType)) {
                double interest = parseDouble(interestField.getText(), "Invalid interest rate");
                currentAccount = new SavingsAccount(balance, interest);
                appendOutput("Savings Account created with balance: " + balance + " and interest rate: " + interest);
                setStatus("Savings Account created successfully.");
            } else {
                double fee = parseDouble(feeField.getText(), "Invalid fee amount");
                currentAccount = new CheckingAccount(balance, fee);
                appendOutput("Checking Account created with balance: " + balance + " and fee: " + fee);
                setStatus("Checking Account created successfully.");
            }

            updateButtonsState();
        } catch (Exception ex) {
            appendOutput("Error: " + ex.getMessage());
            setStatus("Failed to create account.");
        }
    }

    private void doCredit() {
        if (currentAccount == null) return;
        try {
            double amount = parseDouble(amountField.getText(), "Invalid amount");
            currentAccount.credit(amount);
            appendOutput("Credited " + amount + ". New balance: " + currentAccount.getBalance());
            setStatus("Credit successful.");
        } catch (Exception ex) {
            appendOutput("Error: " + ex.getMessage());
            setStatus("Credit failed.");
        }
    }

    private void doDebit() {
        if (currentAccount == null) return;
        try {
            double amount = parseDouble(amountField.getText(), "Invalid amount");

            if (amount > 1000) {
                int choice = JOptionPane.showConfirmDialog(this, 
                        "You are about to debit a large amount: " + amount + ". Proceed?",
                        "Confirm Debit", JOptionPane.YES_NO_OPTION);
                if (choice != JOptionPane.YES_OPTION) {
                    appendOutput("Debit operation canceled by user.");
                    setStatus("Debit canceled.");
                    return;
                }
            }

            currentAccount.debit(amount);
            appendOutput("Debited " + amount + ". New balance: " + currentAccount.getBalance());
            setStatus("Debit successful.");
        } catch (Exception ex) {
            appendOutput("Error: " + ex.getMessage());
            setStatus("Debit failed.");
        }
    }

    private void doCalculateInterest() {
        if (currentAccount == null) return;
        if (currentAccount instanceof SavingsAccount) {
            SavingsAccount savings = (SavingsAccount) currentAccount;
            double interest = savings.calculateInterest();
            appendOutput("Calculated interest: " + interest);
            setStatus("Interest calculated.");
        } else {
            appendOutput("Interest calculation is only for SavingsAccount.");
            setStatus("No interest calculation available.");
        }
    }

    private void doApplyMonthlyInterest() {
        if (currentAccount == null) return;
        if (currentAccount instanceof SavingsAccount) {
            SavingsAccount savings = (SavingsAccount) currentAccount;
            savings.applyMonthlyInterest();
            appendOutput("Monthly interest applied. New balance: " + savings.getBalance());
            setStatus("Monthly interest applied.");
        } else {
            appendOutput("Apply interest is only available for SavingsAccount.");
            setStatus("Not a SavingsAccount.");
        }
    }

    private double parseDouble(String text, String errorMessage) {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void appendOutput(String message) {
        outputArea.append(message + "\n");
    }

    private void setStatus(String message) {
        statusLabel.setText(message);
    }

    private void updateButtonsState() {
        boolean accountCreated = (currentAccount != null);
        creditButton.setEnabled(accountCreated);
        debitButton.setEnabled(accountCreated);
        interestButton.setEnabled(accountCreated);
        applyInterestButton.setEnabled(accountCreated && currentAccount instanceof SavingsAccount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankGUI::new);
    }
}
