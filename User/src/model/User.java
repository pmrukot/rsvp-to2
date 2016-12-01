package model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    public User() {}

    public User(String firstName, String lastName, String password, boolean isAdmin) {
        this.login = UserUtils.generateLogin(firstName, lastName);
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    @Id
    //@GeneratedValue(generator = "asigned")
    @Column(name = "login")
    private String login;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;
    // todo think about setter methods modifiers - should they be private or package level?
    public String getLogin() {
        return login;
    }

    void setLogin() {
        this.login = UserUtils.generateLogin(this.firstName, this.lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
