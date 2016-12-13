package rsvp.user.model;

import rsvp.user.generator.Generator;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private long id;
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

    public User() {}

    public User(String firstName, String lastName, String password, boolean isAdmin) {
        this.login = Generator.generateLogin(firstName, lastName);
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(String firstName, String lastName, boolean isAdmin) {
        this.login = Generator.generateLogin(firstName, lastName);
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = Generator.generatePassword();
        this.isAdmin = isAdmin;
    }

    public User(String login, String firstName, String lastName, String password, boolean isAdmin) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin() {
        this.login = Generator.generateLogin(this.firstName, this.lastName);
    }
    public void setLogin(String login) {
        this.login = login;
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

    @Override
    public String toString(){
        return login;
    }
}
