package rsvp.user.upload;

import rsvp.user.DAO.DBUserDAO;
import rsvp.user.DAO.UserDAO;
import rsvp.user.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Upload {

    private static UserDAO userDAO = new DBUserDAO();


    public static List<User> createUsersFromCsv(File file) throws IOException {
        List<User> createdUsers = new ArrayList<>();
        String splitBy = ",";
        BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] userData = line.split(splitBy);
            if(userData.length != 4) {
                continue;
            }
            User u = new User(userData[0], userData[1], userData[2], Boolean.valueOf(userData[3]));
            if(userDAO.createUser(u)){
                createdUsers.add(u);
            }
        }
        reader.close();
        return createdUsers;
    }
}
