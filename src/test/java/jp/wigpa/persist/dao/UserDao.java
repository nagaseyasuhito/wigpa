package jp.wigpa.persist.dao;

import jp.wigpa.persist.dao.impl.UserDaoImpl;
import jp.wigpa.persist.entity.User;

import com.google.inject.ImplementedBy;

@ImplementedBy(UserDaoImpl.class)
public interface UserDao extends BaseDao<User> {
}
