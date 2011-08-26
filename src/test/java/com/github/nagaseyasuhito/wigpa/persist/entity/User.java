package com.github.nagaseyasuhito.wigpa.persist.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.github.nagaseyasuhito.wigpa.persist.entity.BaseEntity;
import com.google.common.collect.Lists;

@Entity
public class User extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private CharSequence password;

	@Column(nullable = false, unique = true)
	private CharSequence signInId;

	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Task> tasks = Lists.newArrayList();

	public CharSequence getPassword() {
		return this.password;
	}

	public CharSequence getSignInId() {
		return this.signInId;
	}

	public List<Task> getTasks() {
		return this.tasks;
	}

	public void setPassword(CharSequence password) {
		this.password = password;
	}

	public void setSignInId(CharSequence signInId) {
		this.signInId = signInId;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
