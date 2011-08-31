package com.github.nagaseyasuhito.wigpa.wicket;

import javax.persistence.NoResultException;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import com.github.nagaseyasuhito.wigpa.persist.dao.UserDao;
import com.github.nagaseyasuhito.wigpa.persist.entity.User;
import com.google.inject.Inject;

public class TestSession extends WigpaWebSession {
	private static final long serialVersionUID = 3462346385061265286L;

	private Long id;

	@Inject
	private UserDao userDao;

	public TestSession(Request request) {
		super(request);
	}

	@Override
	public boolean authenticate(String signInId, String password) {

		try {
			this.id = this.userDao.findBySignInIdAndPassword(signInId, password).getId();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

	@Override
	public Roles getRoles() {
		if (this.isSignedIn()) {
			return new Roles("USER");
		}
		return null;
	}

	public User getUser() {
		if (this.isSignedIn()) {
			return null;
		}

		return this.userDao.findById(this.id);
	}

	@Override
	public void signOut() {
		super.signOut();

		this.id = null;
	}
}
