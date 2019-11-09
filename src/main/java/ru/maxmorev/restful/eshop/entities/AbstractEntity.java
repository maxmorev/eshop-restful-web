package ru.maxmorev.restful.eshop.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by recommendation of iuliana.cosmina from the book
 * "Pro Spring 5: An In-Depth Guide to the Spring Framework and Its Tools 5th ed. Edition"
 * https://www.apress.com/gp/book/9781484228074
 */
@Data
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR)
	@Column(updatable = false)
	protected Long id;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AbstractEntity)) return false;
		AbstractEntity that = (AbstractEntity) o;
		return this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
