package jp.wigpa.wicket.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

import jp.wigpa.persist.dao.BaseDao;
import jp.wigpa.persist.entity.BaseEntity;
import jp.wigpa.wicket.WigpaWebApplication;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.Maps;

public class EntityProvider<T extends BaseEntity> extends SortableDataProvider<T> implements IFilterStateLocator<T> {
	private static final long serialVersionUID = 1L;

	private static SortedMap<String, Boolean> convertToSortedMap(String order, boolean asc) {
		SortedMap<String, Boolean> orders = Maps.newTreeMap();
		orders.put(order, asc);

		return orders;
	}

	private T criteria;

	private Class<? extends BaseDao<T>> daoClass;

	private SortedMap<String, Boolean> orders = Maps.newTreeMap();

	public EntityProvider(T criteria, Class<? extends BaseDao<T>> daoClass, SortedMap<String, Boolean> orders) {
		this.criteria = criteria;
		this.daoClass = daoClass;
		this.orders = orders;
	}

	public EntityProvider(T criteria, Class<? extends BaseDao<T>> daoClass, String order, boolean asc) {
		this(criteria, daoClass, EntityProvider.convertToSortedMap(order, asc));
	}

	public List<T> get() {
		return this.getDao().findByCriteria(this.criteria, this.getReversedOrders());
	}

	public List<T> get(int first, int count) {
		return this.getDao().findByCriteria(this.criteria, this.getReversedOrders(), first, count);
	}

	protected BaseDao<T> getDao() {
		return WigpaWebApplication.get().getInjector().getInstance(this.daoClass);
	}

	@Override
	public T getFilterState() {
		return this.criteria;
	}

	public SortedMap<String, Boolean> getReversedOrders() {
		if (this.getSort() != null) {
			if (this.orders.containsKey(this.getSort().getProperty())) {
				this.orders.remove(this.getSort().getProperty());
			}
			this.orders.put(this.getSort().getProperty(), this.getSort().isAscending());
		}

		List<String> reverseKeys = new ArrayList<String>(this.orders.keySet());
		Collections.reverse(reverseKeys);

		SortedMap<String, Boolean> reversed = Maps.newTreeMap();
		for (String key : reverseKeys) {
			reversed.put(key, this.orders.get(key));
		}

		return reversed;
	}

	@Override
	public Iterator<T> iterator(int first, int count) {
		return this.get(first, count).iterator();
	}

	@Override
	public IModel<T> model(T object) {
		return Model.of(object);
	}

	@Override
	public void setFilterState(T state) {
		this.criteria = state;
	}

	@Override
	public int size() {
		return (int) this.getDao().countByCriteria(this.criteria);
	}
}
