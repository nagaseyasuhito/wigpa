package com.github.nagaseyasuhito.wigpa.persist.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.github.nagaseyasuhito.wigpa.persist.dao.BaseDao;
import com.github.nagaseyasuhito.wigpa.persist.dao.PredicateBuilder.And;
import com.github.nagaseyasuhito.wigpa.persist.dao.PredicateBuilder.Between;
import com.github.nagaseyasuhito.wigpa.persist.dao.PredicateBuilder.Not;
import com.github.nagaseyasuhito.wigpa.persist.dao.PredicateBuilder.Null;
import com.github.nagaseyasuhito.wigpa.persist.dao.PredicateBuilder.Or;
import com.github.nagaseyasuhito.wigpa.persist.entity.BaseEntity;
import com.google.common.collect.Lists;

public abstract class BaseDaoImpl<T extends BaseEntity> implements BaseDao<T> {

	@Inject
	private Provider<EntityManager> entityManager;

	private List<Predicate> buildChildQuery(BaseEntity criteria, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Path<?> path) {
		Class<?> criteriaClass = criteria.getClass();

		List<Predicate> predicates = Lists.newArrayList();

		while (criteriaClass != null) {
			for (Field field : criteriaClass.getDeclaredFields()) {
				if (!this.isProperty(field)) {
					continue;
				}

				boolean accessible = field.isAccessible();

				Object property = null;
				try {
					field.setAccessible(true);
					property = field.get(criteria);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} finally {
					field.setAccessible(accessible);
				}

				Predicate predicate = this.buildExpression(criteriaBuilder, criteriaQuery, path, field.getName(), property);
				if (predicate != null) {
					predicates.add(predicate);
				}
			}

			criteriaClass = criteriaClass.getSuperclass();
		}

		return predicates;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Predicate buildExpression(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Path<?> path, String propertyName, Object property) {
		if (property == null) {
			return null;
		}

		if (property instanceof Or<?>) {
			List<Predicate> predicates = Lists.newArrayList();
			for (Object object : (Or<?>) property) {
				Predicate predicate = this.buildExpression(criteriaBuilder, criteriaQuery, path, propertyName, object);
				if (predicate != null) {
					predicates.add(predicate);
				}
			}
			return this.processNotPredicate(property, criteriaBuilder, criteriaBuilder.or(predicates.toArray(new Predicate[0])));
		}

		if (property instanceof And<?>) {
			List<Predicate> predicates = Lists.newArrayList();
			for (Object object : (And<?>) property) {
				Predicate predicate = this.buildExpression(criteriaBuilder, criteriaQuery, path, propertyName, object);
				if (predicate != null) {
					predicates.add(predicate);
				}
			}
			return this.processNotPredicate(property, criteriaBuilder, criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		}

		if (property instanceof In<?>) {
			return this.processNotPredicate(property, criteriaBuilder, path.get(propertyName).in((List<?>) property));
		}

		if (property instanceof Between<?>) {
			Comparable<?> from = ((Between<?>) property).getFrom();
			Comparable<?> to = ((Between<?>) property).getTo();
			return this.processNotPredicate(property, criteriaBuilder, criteriaBuilder.between(path.<Comparable> get(propertyName), from, to));
		}

		if (property instanceof Collection<?>) {
			List<Predicate> predicates = Lists.newArrayList();
			for (Object object : (Collection<?>) property) {
				if (object instanceof BaseEntity) {
					predicates.addAll(this.buildChildQuery((BaseEntity) object, criteriaBuilder, criteriaQuery, ((Root<?>) path).join(propertyName)));
				} else {
					return this.processNotPredicate(property, criteriaBuilder, criteriaBuilder.equal(path.get(propertyName), property));
				}
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		}

		if (property instanceof Null<?>) {
			if (((Null<?>) property).isNull()) {
				return this.processNotPredicate(property, criteriaBuilder, criteriaBuilder.isNull(path.get(propertyName)));
			} else {
				return null;
			}
		}

		if (property instanceof BaseEntity) {
			if (((BaseEntity) property).getId() != null) {
				return this.processNotPredicate(property, criteriaBuilder, criteriaBuilder.equal(path.get(propertyName), property));
			} else {
				return criteriaBuilder.and(this.buildChildQuery((BaseEntity) property, criteriaBuilder, criteriaQuery, path.get(propertyName)).toArray(new Predicate[0]));
			}
		} else {
			return this.processNotPredicate(property, criteriaBuilder, criteriaBuilder.equal(path.get(propertyName), property));
		}
	}

	private <X> Root<X> buildQuery(T criteria, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Root<X> root) {
		criteriaQuery.where(this.buildChildQuery(criteria, criteriaBuilder, criteriaQuery, root).toArray(new Predicate[0]));

		return root;
	}

	@Override
	public long countByCriteria(T criteria) {
		EntityManager entityManager = this.entityManager.get();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<T> root = criteriaQuery.from(this.getEntityClass());

		return entityManager.createQuery(criteriaQuery.select(criteriaBuilder.count(this.buildQuery(criteria, criteriaBuilder, criteriaQuery, root)))).getSingleResult();
	}

	@Override
	public T findByCriteria(T criteria) {
		EntityManager entityManager = this.entityManager.get();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.getEntityClass());
		Root<T> root = criteriaQuery.from(this.getEntityClass());

		return entityManager.createQuery(criteriaQuery.select(this.buildQuery(criteria, criteriaBuilder, criteriaQuery, root))).getSingleResult();
	}

	@Override
	public List<T> findByCriteria(T criteria, SortedMap<String, Boolean> orders) {
		return this.findByCriteria(criteria, orders, -1, -1);
	}

	@Override
	public List<T> findByCriteria(T criteria, SortedMap<String, Boolean> orders, int firstResult, int maxResults) {
		EntityManager entityManager = this.entityManager.get();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.getEntityClass());
		Root<T> root = criteriaQuery.from(this.getEntityClass());

		for (Map.Entry<String, Boolean> entry : orders.entrySet()) {
			if (entry.getValue()) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get(entry.getKey())));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get(entry.getKey())));
			}
		}

		TypedQuery<T> query = entityManager.createQuery(criteriaQuery.select(this.buildQuery(criteria, criteriaBuilder, criteriaQuery, root)));
		if (firstResult >= 0) {
			query.setFirstResult(firstResult);
		}
		if (maxResults >= 0) {
			query.setMaxResults(maxResults);
		}
		return query.getResultList();
	}

	@Override
	public List<T> findByCriteria(T criteria, String order, boolean ascending) {
		SortedMap<String, Boolean> orders = new TreeMap<String, Boolean>();
		orders.put(order, ascending);
		return this.findByCriteria(criteria, orders);
	}

	@Override
	public List<T> findByCriteria(T criteria, String order, boolean ascending, int firstResult, int maxResults) {
		SortedMap<String, Boolean> orders = new TreeMap<String, Boolean>();
		orders.put(order, ascending);
		return this.findByCriteria(criteria, orders, firstResult, maxResults);
	}

	@Override
	public T findById(Long id) {
		return this.entityManager.get().find(this.getEntityClass(), id);
	}

	public abstract Class<T> getEntityClass();

	protected EntityManager getEntityManager() {
		return this.entityManager.get();
	}

	protected boolean isProperty(Field field) {
		return field.getAnnotation(Transient.class) == null && (field.getModifiers() & (Modifier.FINAL | Modifier.STATIC | Modifier.TRANSIENT)) == 0;
	}

	@Override
	public T merge(T entity) {
		return this.entityManager.get().merge(entity);
	}

	@Override
	public void persist(T entity) {
		this.getEntityManager().persist(entity);
	}

	@Override
	public T persistOrMerge(T entity) {
		if (entity.getId() == null) {
			this.persist(entity);
			return entity;
		} else {
			return this.merge(entity);
		}
	}

	private Predicate processNotPredicate(Object property, CriteriaBuilder criteriaBuilder, Predicate predicate) {
		if (property instanceof Not<?> && ((Not<?>) property).isNot()) {
			return criteriaBuilder.not(predicate);
		} else {
			return predicate;
		}
	}

	@Override
	public void remove(T entity) {
		this.entityManager.get().remove(entity);
	}

	public void setEntityManager(Provider<EntityManager> entityManager) {
		this.entityManager = entityManager;
	}
}
