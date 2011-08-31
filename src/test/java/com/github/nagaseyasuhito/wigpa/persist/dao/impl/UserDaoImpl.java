package com.github.nagaseyasuhito.wigpa.persist.dao.impl;

import javax.persistence.NoResultException;

import com.github.nagaseyasuhito.wigpa.persist.dao.UserDao;
import com.github.nagaseyasuhito.wigpa.persist.entity.User;

public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	@Override
	public User findBySignInIdAndPassword(String signInId, String password) {
		if (signInId == null || password == null) {
			throw new NoResultException();
		}

		User criteria = new User();
		criteria.setSignInId(signInId);
		criteria.setPassword(password);
		return this.findByCriteria(criteria);
	}

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}
}
