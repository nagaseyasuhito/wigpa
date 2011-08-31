package com.github.nagaseyasuhito.wigpa.persist.dao;

import javax.persistence.NoResultException;

import com.github.nagaseyasuhito.wigpa.persist.dao.impl.UserDaoImpl;
import com.github.nagaseyasuhito.wigpa.persist.entity.User;
import com.google.inject.ImplementedBy;

@ImplementedBy(UserDaoImpl.class)
public interface UserDao extends BaseDao<User> {

	/**
	 * {@code signInId}と{@code password}に合致する{@link User}を返します。
	 * 
	 * @param signInId
	 *            サインインID
	 * @param password
	 *            パスワード
	 * @return 合致した{@link User}
	 * @throws NoResultException
	 *             {@code signInId}もしくは{@code password}が{@code null}、もしくは合致する
	 *             {@link User}が見つからなかった場合
	 */
	User findBySignInIdAndPassword(String signInId, String password);
}
