package com.github.nagaseyasuhito.wigpa.persist.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@MappedSuperclass
@TypeDefs({ @TypeDef(typeClass = String.class, defaultForType = CharSequence.class), @TypeDef(typeClass = Long.class, defaultForType = Number.class) })
public abstract class BaseEntity implements Serializable, Comparable<BaseEntity> {
	private static final long serialVersionUID = 6739209232001992337L;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimestamp;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTimestamp;

	@Column(nullable = false)
	@Version
	private Long version;

	@Override
	public int compareTo(BaseEntity o) {
		return new CompareToBuilder().append(this.getId(), o.getId()).toComparison();
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}

		if (!this.getClass().isAssignableFrom(object.getClass()) && !object.getClass().isAssignableFrom(this.getClass())) {
			return false;
		}

		BaseEntity entity = (BaseEntity) object;
		if (this.getId() == null || entity.getId() == null) {
			return super.equals(entity);
		}

		return this.getId().equals(entity.getId());
	}

	public Date getCreateTimestamp() {
		return this.createTimestamp;
	}

	public Long getId() {
		return this.id;
	}

	public Date getUpdateTimestamp() {
		return this.updateTimestamp;
	}

	public Long getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		return this.getId() == null ? super.hashCode() : this.getId().hashCode();
	}

	@PrePersist
	public void prePersist() {
		Date now = new Date();
		this.createTimestamp = now;
		this.updateTimestamp = now;
	}

	@PreUpdate
	public void preUpdate() {
		Date now = new Date();
		this.updateTimestamp = now;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	@Override
	public String toString() {
		Class<?> clazz = this.getClass();
		List<String> excludeFieldNames = new ArrayList<String>();
		do {
			for (Field field : clazz.getDeclaredFields()) {
				Class<?> type = field.getType();

				if (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {
					excludeFieldNames.add(field.getName());
				}
			}
		} while (null != (clazz = clazz.getSuperclass()));

		ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(this);
		reflectionToStringBuilder.setExcludeFieldNames(excludeFieldNames.toArray(new String[0]));
		return reflectionToStringBuilder.toString();
	}
}
