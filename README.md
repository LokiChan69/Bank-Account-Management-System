Bank Account GUI Application

This project is a simple Java Swing application that simulates basic bank account operations. It supports both Savings Accounts and Checking Accounts, with the ability to credit, debit, and calculate interest.
What’s New
Refactoring and Code Cleanup

    Separation of Concerns:
    The code has been reorganized for better readability and maintainability. Although still presented in a single file for demonstration, you may separate the Account, SavingsAccount, and CheckingAccount classes into their own files.

    Improved Error Handling:
    When parsing user input (e.g., balance, amount, interest rate, fee), invalid inputs now result in clear and concise error messages. Exceptions are caught and displayed to the user via the output area and status bar.

    Enhanced User Interface:
    The UI has been updated to be more intuitive:
        A dropdown (JComboBox) allows choosing between "Savings Account" and "Checking Account" before creation.
        A single "Create Account" button replaces separate buttons for each account type.
        Buttons like "Credit," "Debit," and "Calculate Interest" are disabled until an account is successfully created, preventing null operations.

    Status Bar:
    A new status bar at the bottom of the window provides quick feedback on operations, enhancing user experience and clarity.

New Features

    Monthly Interest Application for Savings Accounts:
    A new button, "Apply Monthly Interest," has been added. For SavingsAccount users, clicking this button will calculate and then credit the interest directly to the account balance, simulating a monthly accrual.

    Confirmation for Large Debits:
    If the user attempts to debit an amount greater than a specified threshold (e.g., 1000), a confirmation dialog appears. The user must confirm before proceeding with the transaction.

    Better Feedback and Validation:
    The status bar and output area provide clearer messages when actions succeed or fail. Invalid input (e.g., non-numeric amounts) are caught and communicated to the user.

Summary of Changes

    Introduced JComboBox for selecting account type.
    Added a single "Create Account" button for initializing either a Savings or Checking Account.
    Disabled action buttons until an account is created.
    Added a "Status Bar" at the bottom of the UI.
    Added a new button "Apply Monthly Interest" for SavingsAccount.
    Implemented confirmation dialogs for large debit operations.
    Centralized input parsing and error handling to reduce code duplication.
    Improved exception messages and user feedback throughout the UI.
