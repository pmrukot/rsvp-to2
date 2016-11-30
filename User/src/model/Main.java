package model;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*if(AuthenticationService.authenticateUser("admin", "admin1")) {
            System.out.println("Logged in as: " + AuthenticationService.getCurrentUser().getLogin());
        } else {
            System.out.println("Error logging in.");
        }
        AuthenticationService.logOut();*/
        //UserUtils.createUser("imie", "nazwisko", "haslo", false);
        /*List<User> users = UserUtils.getUsersByName("imie nazwisko");
        for(User u : users) {
            System.out.println(u.getLogin());
        }*/
        /*UserUtils.deleteUser("imienazwisko2");
        List<User> users = UserUtils.getUsersByName("imie nazwisko");
        for(User u : users) {
            System.out.println(u.getLogin());
        }*/
        for(User u : UserUtils.getUsersByName("imie nazwisko")) {
            System.out.println(u.getLogin());
        }
        HibernateUtils.shutdown();
    }
}
