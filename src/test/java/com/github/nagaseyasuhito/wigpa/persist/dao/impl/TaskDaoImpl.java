package com.github.nagaseyasuhito.wigpa.persist.dao.impl;

import com.github.nagaseyasuhito.wigpa.persist.dao.TaskDao;
import com.github.nagaseyasuhito.wigpa.persist.dao.impl.BaseDaoImpl;
import com.github.nagaseyasuhito.wigpa.persist.entity.Task;

public class TaskDaoImpl extends BaseDaoImpl<Task> implements TaskDao {

	@Override
	public Class<Task> getEntityClass() {
		return Task.class;
	}
}
