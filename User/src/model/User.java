package model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    public User() {}
    // todo constructor used to create user from within app
    // todo login creation policy: concatenate first and last name + number
    // (something like (select count users with the same first and last name) +1)
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

    private void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    private void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
