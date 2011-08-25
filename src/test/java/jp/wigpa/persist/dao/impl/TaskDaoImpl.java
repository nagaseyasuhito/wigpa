package jp.wigpa.persist.dao.impl;

import jp.wigpa.persist.dao.TaskDao;
import jp.wigpa.persist.entity.Task;

public class TaskDaoImpl extends BaseDaoImpl<Task> implements TaskDao {

	@Override
	public Class<Task> getEntityClass() {
		return Task.class;
	}
}
