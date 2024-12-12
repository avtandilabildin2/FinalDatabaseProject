import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static final String URL = "jdbc:postgresql://localhost:5432/telegramae";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public int getCountMessages(String login) {
        String query = "SELECT COUNT(m.id) AS message_count " +
                "FROM messages m " +
                "INNER JOIN users u ON m.user_id = u.id " +
                "WHERE u.login = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("message_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ResultSet searchUserByLogin(String login) {
        String query = "SELECT * FROM users WHERE login = ?";
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, login);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getUserIdByLogin(String login) {
        String query = "SELECT id FROM users WHERE login = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean registerUser(String login, String password, String phone) {
        String query = "INSERT INTO users (login, password, phone) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, phone);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authenticateUser(String login, String password) {
        String query = "SELECT * FROM users WHERE login = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendMessage(int userId, String message) {
        String query = "INSERT INTO messages (user_id, message) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setString(2, message);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getMessagesForUser(String login) {
        List<String> messages = new ArrayList<>();
        String query = "SELECT message, user_id FROM messages m INNER JOIN users u ON m.user_id = u.id WHERE u.login = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add("From: " + rs.getString("user_id") + " | Message: " + rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String query = "SELECT id, login, phone FROM users";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String userInfo = String.format("ID: %d, Login: %s, Phone: %s", rs.getInt("id"), rs.getString("login"), rs.getString("phone"));
                users.add(userInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
