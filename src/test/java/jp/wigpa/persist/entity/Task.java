package jp.wigpa.persist.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.google.common.collect.Lists;

@Entity
public class Task extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column
	@Lob
	private CharSequence detail;

	@Column(nullable = false)
	private CharSequence subject;

	@ManyToMany
	private List<Tag> tags = Lists.newArrayList();

	@ManyToOne(optional = false)
	private User user;

	public CharSequence getDetail() {
		return this.detail;
	}

	public CharSequence getSubject() {
		return this.subject;
	}

	public List<Tag> getTags() {
		return this.tags;
	}

	public User getUser() {
		return this.user;
	}

	public void setDetail(CharSequence detail) {
		this.detail = detail;
	}

	public void setSubject(CharSequence subject) {
		this.subject = subject;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
