package rsvp.user.model;

import rsvp.common.persistence.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.DAO.UserDAO;
import java.io.*;

public abstract class UserUtils {

    private static UserDAO userDAO = new DBUserDAO();

    static String generateLogin(String firstName, String lastName) {
        String login = firstName + lastName;
        Session session = HibernateUtils.getSession();
        String sql = "select count(*) " +
                "from User u " +
                "where u.firstName = :firstName and u.lastName = :lastName";
        Query query = session.createQuery(sql);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        long count = (long) query.uniqueResult();
        // todo which is better: load list of users once and iterate over it or select 1 user from db (potentially) multiple times?
        do {
            count++;
        } while(session.get(User.class, login + count) != null);
        login += count;
        session.close();
        return login;
    }

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
