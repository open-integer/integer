package edu.harvard.integer.common;


public interface IDInterface {
	
	/**
	 * @return the identifier
	 */
	public Long getIdentifier();

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(Long identifier);

	/**
	 * @return the name
	 */
	public String getName();

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name);

	/**
	 * @return the idType
	 */
	public IDType getIdType();
	
	/**
	 * @param idType
	 *            the idType to set
	 */
	public void setIdType(IDType idType);

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj);	
	
}
