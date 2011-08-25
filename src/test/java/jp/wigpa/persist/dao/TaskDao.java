package jp.wigpa.persist.dao;

import jp.wigpa.persist.dao.impl.TaskDaoImpl;
import jp.wigpa.persist.entity.Task;

import com.google.inject.ImplementedBy;

@ImplementedBy(TaskDaoImpl.class)
public interface TaskDao extends BaseDao<Task> {
}
