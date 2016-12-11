package rsvp.user.upload;


import rsvp.user.DAO.DBUserDAO;
import rsvp.user.DAO.UserDAO;
import rsvp.user.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Upload {

    private static UserDAO userDAO = new DBUserDAO();


    public static int createUsersFromCsv(File file) throws IOException {
        String splitBy = ",";
        BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
        String line;
        int addedUsers = 0;
        while ((line = reader.readLine()) != null) {
            String[] userData = line.split(splitBy);
            if(userData.length != 4) {
                continue;
            }
            if(userDAO.createUser(new User(userData[0], userData[1], userData[2], Boolean.valueOf(userData[3])))){
                addedUsers++;
            }
        }
        reader.close();
        return addedUsers;
    }
}
