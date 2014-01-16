package edu.harvard.integer.common;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
public class ID implements IDInterface {

	
	protected Long identifier = null;

	
	@Size(min = 1, max = 50)
	private String name = null;

	
	private IDType idType = null;

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

		if (identifier != null)
			return identifier.hashCode();
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

		if (identifier != null)
			return identifier.equals(other.getIdentifier());
		else {
			if (other.getIdentifier() != null)
				return false;
			else
				return true;
		}

	}

}
