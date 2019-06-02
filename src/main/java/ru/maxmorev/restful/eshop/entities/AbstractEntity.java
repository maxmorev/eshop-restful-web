package ru.maxmorev.restful.eshop.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by recommendation of iuliana.cosmina from the book
 * "Pro Spring 5: An In-Depth Guide to the Spring Framework and Its Tools 5th ed. Edition"
 * https://www.apress.com/gp/book/9781484228074
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false)
	protected Long id;

	/**
	 * Returns the entity identifier. This identifier is unique per entity. It is used by persistence frameworks used in a project,
	 * and although is public, it should not be used by application code.
	 * This identifier is mapped by ORM (Object Relational Mapper) to the database primary key of the Person record to which
	 * the entity instance is mapped.
	 *
	 * @return the unique entity identifier
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the entity identifier. This identifier is unique per entity.  Is is used by persistence frameworks
	 * and although is public, it should never be set by application code.
	 *
	 * @param id the unique entity identifier
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AbstractEntity that = (AbstractEntity) o;
		if (id != null ? !id.equals(that.id) : that.id != null)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
