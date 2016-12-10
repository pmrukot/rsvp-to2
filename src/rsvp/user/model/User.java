package rsvp.user.model;

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
    @Column(name = "login", unique = true, nullable = false)
    private String login;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    public String getLogin() {
        return login;
    }

    public void setLogin() {
        this.login = UserUtils.generateLogin(this.firstName, this.lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
