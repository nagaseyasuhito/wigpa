package jp.wigpa.persist.dao.impl;

import jp.wigpa.persist.dao.UserDao;
import jp.wigpa.persist.entity.User;

public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}
}
