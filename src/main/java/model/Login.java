package model;

public class Login {
    private int userId;
    private String username;
    private byte[] userPassword;
    private String userType; // novo campo

    // Construtores
    public Login() {}

    public Login(int userId, String username, byte[] userPassword, String userType) {
        this.userId = userId;
        this.username = username;
        this.userPassword = userPassword;
        this.userType = userType;
    }

    public Login(String username, byte[] userPassword, String userType) {
        this.username = username;
        this.userPassword = userPassword;
        this.userType = userType;
    }

    // Getters e Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(byte[] userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

