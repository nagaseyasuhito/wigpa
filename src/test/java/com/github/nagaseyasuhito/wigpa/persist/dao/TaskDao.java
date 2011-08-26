package com.github.nagaseyasuhito.wigpa.persist.dao;


import com.github.nagaseyasuhito.wigpa.persist.dao.BaseDao;
import com.github.nagaseyasuhito.wigpa.persist.dao.impl.TaskDaoImpl;
import com.github.nagaseyasuhito.wigpa.persist.entity.Task;
import com.google.inject.ImplementedBy;

@ImplementedBy(TaskDaoImpl.class)
public interface TaskDao extends BaseDao<Task> {
}
