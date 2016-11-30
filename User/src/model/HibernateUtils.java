package model;

import java.io.File;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
	private static String path = "src/model/hibernate.cfg.xml";
	private static SessionFactory sessionFactory = null;
	
	public static synchronized SessionFactory getSessionFactory() 
	{		
		if (sessionFactory == null)
		{
			sessionFactory = new Configuration().configure(new File(path)).buildSessionFactory();
		}
		return sessionFactory;
	}
	
	public static Session getSession() 
	{
		return getSessionFactory().openSession();
	}
	
	public static void shutdown() 
	{
		getSessionFactory().close();
	}
}
