package com.github.nagaseyasuhito.wigpa.persist.dao;

import java.util.List;
import java.util.SortedMap;

import com.github.nagaseyasuhito.wigpa.persist.entity.BaseEntity;

public interface BaseDao<T extends BaseEntity> {
	long countByCriteria(T criteria);

	T findByCriteria(T criteria);

	public List<T> findByCriteria(T criteria, SortedMap<String, Boolean> orders);

	public List<T> findByCriteria(T criteria, SortedMap<String, Boolean> orders, int firstResult, int maxResults);

	public List<T> findByCriteria(T criteria, String order, boolean ascending);

	public List<T> findByCriteria(T criteria, String order, boolean ascending, int firstResult, int maxResults);

	T findById(Long id);

	T merge(T entity);

	void persist(T entity);

	T persistOrMerge(T entity);

	void remove(T entity);
}
