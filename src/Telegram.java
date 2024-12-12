import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.sql.DriverManager.getConnection;

public class Telegram {
    User auth;
    List<User> users = new ArrayList<>();
    int id;
    Scanner scanner = new Scanner(System.in);
    Database db = new Database();
    public void run() {
        System.out.println("Print help to view commands!");
        while (true) {
            System.out.print("Print command: ");
            String command = scanner.next();
            if (command.equals("exit")) {
                exit();
            } else if (command.equals("help")) {
                help();
            } else if (command.equals("register")) {
                register();
            } else if (command.equals("login")) {
                login();
            } else if (command.equals("all_users")) {
                all_users();
            } else if (command.equals("search_user_by_login")) {
                search_user_by_login();
            } else if (command.equals("logout")) {
                logout();
            } else if (command.equals("info")) {
                info();
            } else if (command.equals("send_message")) {
                sendMessage();
            } else if (command.equals("mail_info")) {
                mailInfo();
            } else if (command.equals("read_all_messages")) {
                readAllMessages();
            } else {
                System.out.println("Command not found!");
            }
        }
    }

    public void readAllMessages() {
        try (Connection conn = getConnection(Database.URL,Database.USER,Database.PASSWORD)) { // Подключение
            String query = "SELECT message FROM messages";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                if (!rs.isBeforeFirst()) {
                    System.out.println("Message not found!.");
                } else {
                    while (rs.next()) {
                        System.out.println("- " + rs.getString("message"));
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mailInfo() {
        if (auth == null) {
            System.out.println("You are not logged in!.");
            return;
        }

        Database db = new Database();
        int count = db.getCountMessages(auth.getLogin());
        System.out.println("Number of messages: " + count);
    }

    public void sendMessage() {
        if (auth == null) {
            System.out.println("You are not logged in!.");
            return;
        }

        System.out.print("Print login of the user to send him a message:  ");
        String login = scanner.next();
        Database db = new Database();
        int userId = db.getUserIdByLogin(login);

        if (userId != -1) {
            System.out.print("Print message: ");
            String message = Console.readLine();
            db.sendMessage(userId, auth.getLogin() + ": " + message);
            System.out.println("Message sent!.");
        } else {
            System.out.println("User not found!.");
        }

    }

    public void info() {
        if (auth == null) {
            System.out.println("You are not logged in!");
            return;
        }

        System.out.println(auth.toString());

    }

    public void logout() {
        if (auth == null) {
            System.out.println("You are not logged in!.");
            return;
        }
        System.out.println("Logging out of the user: " + auth.getLogin());
        auth = null;
    }

    public void search_user_by_login() {
        if (auth == null) {
            System.out.println("Not authorized user can not search user by login!!");
            return;
        }

        System.out.print("Print login of the user to send him a message: ");
        String login = scanner.next();
        Database db = new Database();
        ResultSet rs = db.searchUserByLogin(login);

        try {
            if (rs != null && rs.next()) {
                System.out.println("User found!: ");
                System.out.println("ID: " + rs.getInt("id") + ", Login: " + rs.getString("login") + ", Phone: " + rs.getString("phone"));
            } else {
                System.out.println("User with this login not found!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void all_users() {
        Database db = new Database();
        ResultSet rs = (ResultSet) db.getAllUsers();

        try {
            while (rs != null && rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Login: " + rs.getString("login") + ", Phone: " + rs.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        System.out.println("End of the program!");
        System.exit(0);
    }

    public void help() {
        if (auth == null) {
            System.out.println("exit - end of the program.");
            System.out.println("help - prints list of commands.");
            System.out.println("all_users - prints all users in the database.");
            System.out.println("login - authorization of the user.");
            System.out.println("register - registration of the user.");
        } else {
            System.out.println("exit - end of the program.");
            System.out.println("help - prints list of commands.");
            System.out.println("search_user_by_login - searchs user by login.");
            System.out.println("logout - logging out of the user.");
            System.out.println("read_all_message - prints all gained messages.");
            System.out.println("all_users - prints all users in the database.");
            System.out.println("send_message - sends a message to the user by login.");
            System.out.println("mail_info - prints number of messages gained.");
            System.out.println("info - prints information about authorized user.");
        }
    }

    public void register() {

        if (auth != null) {
            System.out.println("Authorized user can not register.");
            return;
        }

        System.out.print("Print login: ");
        String login = scanner.next();
        System.out.print("Print password: ");
        String password = scanner.next();
        System.out.print("Print phone number: ");
        String phone = scanner.next();

        Database db = new Database();
        if (db.registerUser(login, password, phone)) {
            System.out.println("Registration successful!.");
        } else {
            System.out.println("Mistake in registration.");
        }
    }

    public void login() {
        if (auth != null) {
            System.out.println("Authorized user can not login.");
            return;
        }

        System.out.print("Print login: ");
        String login = scanner.next();
        System.out.print("Print password: ");
        String password = scanner.next();

        Database db = new Database();
        if (db.authenticateUser(login, password)) {
            auth = new User();
            auth.setLogin(login);
            System.out.println("You successfully logged in!");
        } else {
            System.out.println("Mistake in login.");
        }
    }


}
