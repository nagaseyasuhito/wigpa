package com.github.nagaseyasuhito.wigpa.persist.dao;


import com.github.nagaseyasuhito.wigpa.persist.dao.BaseDao;
import com.github.nagaseyasuhito.wigpa.persist.dao.impl.UserDaoImpl;
import com.github.nagaseyasuhito.wigpa.persist.entity.User;
import com.google.inject.ImplementedBy;

@ImplementedBy(UserDaoImpl.class)
public interface UserDao extends BaseDao<User> {
}
