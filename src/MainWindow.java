import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainWindow extends JFrame {

    private Database db = new Database();
    private JTextField loginField, passwordField, phoneField, searchField, recipientField, messageField;
    private JTextArea messageArea;
    private JButton loginButton, registerButton, logoutButton, sendButton, readButton, allUsersButton, searchButton;

    private String loggedInUser = null;

    public MainWindow() {
        setTitle("Telegram App");
        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createLoginPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);

        loginField = new JTextField(10);
        passwordField = new JTextField(10);
        phoneField = new JTextField(10);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginButton.addActionListener(e -> loginAction(e));
        registerButton.addActionListener(e -> registerAction(e));

        panel.add(new JLabel("Login:"));
        panel.add(loginField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        panel.add(loginButton);
        panel.add(registerButton);

        return panel;
    }
    private void loginAction(ActionEvent e) {
        String login = loginField.getText();
        String password = passwordField.getText();

        if (login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both login and password.");
            return;
        }

        if (db.authenticateUser(login, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            loggedInUser = login;
        } else {
            JOptionPane.showMessageDialog(this, "Login failed! Incorrect credentials.");
        }
    }

    private void registerAction(ActionEvent e) {
        String login = loginField.getText();
        String password = passwordField.getText();
        String phone = phoneField.getText();

        if (login.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields to register.");
            return;
        }

        if (db.registerUser(login, password, phone)) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed! The login may already exist.");
        }
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setPreferredSize(new Dimension(1000, 500));
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();

        sendButton = new JButton("Send Message");
        sendButton.addActionListener(this::sendMessageAction);

        readButton = new JButton("Read Messages");
        readButton.addActionListener(this::readMessagesAction);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logoutAction());

        allUsersButton = new JButton("All Users");
        allUsersButton.addActionListener(this::allUsersAction);

        panel.add(sendButton);
        panel.add(readButton);
        panel.add(logoutButton);
        panel.add(allUsersButton);

        return panel;
    }

    private void sendMessageAction(ActionEvent e) {
        if (loggedInUser != null) {
            String recipient = JOptionPane.showInputDialog("Enter recipient login:");
            String message = JOptionPane.showInputDialog("Enter message:");
            db.sendMessage(db.getUserIdByLogin(recipient), loggedInUser + ": " + message);
        }
    }

    private void readMessagesAction(ActionEvent e) {
        if (loggedInUser != null) {
            List<String> messages = db.getMessagesForUser(loggedInUser);
            messageArea.setText(String.join("\n", messages));
        }
    }

    private void logoutAction() {
        loggedInUser = null;
        JOptionPane.showMessageDialog(this, "Logged out!");
    }

    private void allUsersAction(ActionEvent e) {
        List<String> users = db.getAllUsers();
        JOptionPane.showMessageDialog(this, String.join("\n", users));
    }
}
