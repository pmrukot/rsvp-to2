package model;

public class Main {
    public static void main(String[] args) {
        if(AuthenticationService.authenticateUser("admin", "admin1")) {
            System.out.println("Logged in as: " + AuthenticationService.getCurrentUser().getLogin());
        } else {
            System.out.println("Error logging in.");
        }
        AuthenticationService.logOut();
        HibernateUtils.shutdown();
    }
}
