package com.github.nagaseyasuhito.wigpa.persist.dao;

import javax.persistence.PersistenceException;


import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.nagaseyasuhito.wigpa.persist.dao.PredicateBuilder;
import com.github.nagaseyasuhito.wigpa.persist.dao.PredicateBuilder.And;
import com.github.nagaseyasuhito.wigpa.persist.dao.PredicateBuilder.Null;
import com.github.nagaseyasuhito.wigpa.persist.dao.PredicateBuilder.Or;
import com.github.nagaseyasuhito.wigpa.persist.entity.Task;
import com.github.nagaseyasuhito.wigpa.persist.entity.User;
import com.google.inject.Inject;

@RunWith(WigpaRunner.class)
public class UserDaoTest {

	@Inject
	private UserDao userDao;

	@Test
	public void testCriteria() {
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setSignInId("signInId" + i);
			user.setPassword("password");
			this.userDao.persist(user);
		}

		User criteria = new User();
		And<CharSequence> signInIds = PredicateBuilder.and(CharSequence.class);
		signInIds.add("signInId3");
		signInIds.add("signInId4");
		Null<CharSequence> nullable = PredicateBuilder.nullable(CharSequence.class);
		nullable.setNull(true);

		Or<CharSequence> and = PredicateBuilder.or(CharSequence.class);
		and.add((CharSequence) nullable);
		and.add((CharSequence) signInIds);

		criteria.setSignInId((CharSequence) and);
		this.userDao.findByCriteria(criteria, "id", true);
	}

	@Test(expected = PersistenceException.class)
	public void testPersistFailedWithDuplicateSignInId() {
		User userA = new User();
		userA.setSignInId("signInId");
		userA.setPassword("password");
		this.userDao.persist(userA);

		User userB = new User();
		userB.setSignInId("signInId");
		userB.setPassword("password");
		this.userDao.persist(userB);
	}

	@Test(expected = PersistenceException.class)
	public void testPersistFailedWithNullPassword() {
		User user = new User();
		user.setSignInId("signInId");
		this.userDao.persist(user);
	}

	@Test(expected = PersistenceException.class)
	public void testPersistFailedWithNullSignInId() {
		User user = new User();
		user.setPassword("password");
		this.userDao.persist(user);
	}

	@Test(expected = PersistenceException.class)
	public void testPersistFailedWithNullSignInIdAndNullPassword() {
		User user = new User();
		this.userDao.persist(user);
	}

	@Test
	public void testPersistSuccessed() {
		User user = new User();
		user.setSignInId("signInId");
		user.setPassword("password");
		this.userDao.persist(user);
	}

	@Test
	public void testRemoveSuccessed() {
		User user = new User();
		user.setSignInId("signInId");
		user.setPassword("password");
		for (int i = 0; i < 10; i++) {
			Task task = new Task();
			task.setUser(user);
			task.setSubject("subject" + i);
			user.getTasks().add(task);
		}
		this.userDao.persist(user);

		this.userDao.remove(user);
	}
}
