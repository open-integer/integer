/**
 * 
 */
package edu.harvard.integer.common;

import javax.persistence.EmbeddedId;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

/**
 * @author dtaylor
 *
 */
@MappedSuperclass
public abstract class BaseEntity implements IDInterface {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long identifier = null;

	private IDType idType = null;

	@Size(min = 1, max = 50)
	private String name = null;

	/**
	 * @return the identifier
	 */
	public Long getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the idType
	 */
	public IDType getIdType() {
		return idType;
	}

	/**
	 * @param idType
	 *            the idType to set
	 */
	public void setIdType(IDType idType) {
		this.idType = idType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		if (getIdentifier() != null)
			return getIdentifier().hashCode();
		else if (name != null)
			return name.hashCode();
		else
			return 1;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ID))
			return false;

		ID other = (ID) obj;

		if (getIdentifier() != null)
			return getIdentifier().equals(other.getIdentifier());
		else {
			if (other.getIdentifier() != null)
				return false;
			else
				return true;
		}

	}

}
