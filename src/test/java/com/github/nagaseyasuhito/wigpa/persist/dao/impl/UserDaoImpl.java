package com.github.nagaseyasuhito.wigpa.persist.dao.impl;

import com.github.nagaseyasuhito.wigpa.persist.dao.UserDao;
import com.github.nagaseyasuhito.wigpa.persist.dao.impl.BaseDaoImpl;
import com.github.nagaseyasuhito.wigpa.persist.entity.User;


public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}
}
