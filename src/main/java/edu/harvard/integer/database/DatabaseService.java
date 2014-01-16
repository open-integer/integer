package edu.harvard.integer.database;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.user.User;

@Singleton
public class DatabaseService implements DatabaseServiceEJB {

	@PersistenceContext
	private EntityManager em;
	
	@PostConstruct
	public void init() {
		
		System.out.println("Create user dtaylor");
		
		User u = new User();
		u.setName("dtaylor");
		
		IDType userType = new IDType();
		userType.setClassType(User.class);
		u.setIdType(userType);
		u.setFirstName("Dave");
		u.setLastName("Taylor");
		
		em.persist(u);
	}
}
