import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String login;
    private String password;
    private String phone;
    private List<String> messages;
    private int countMessages=0;
    public void autoIncrement(){
        this.countMessages++;
    }

    public User(int id, String login, String password, String phone) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.phone = phone;
        messages = new ArrayList<>();
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public List<String> getMessages() {
        return messages;
    }

    public User setMessages(List<String> messages) {
        this.messages = messages;
        return this;
    }

    public int getCountMessages() {
        return countMessages;
    }

    public User setCountMessages(int countMessages) {
        this.countMessages = countMessages;
        return this;
    }
}
