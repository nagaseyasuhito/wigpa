package jp.wigpa.persist.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.google.common.collect.Lists;

@Entity
public class Tag extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, unique = true)
	private CharSequence name;

	@ManyToMany(mappedBy = "tags")
	private List<Task> tasks = Lists.newArrayList();

	public CharSequence getName() {
		return this.name;
	}

	public List<Task> getTasks() {
		return this.tasks;
	}

	public void setName(CharSequence name) {
		this.name = name;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
