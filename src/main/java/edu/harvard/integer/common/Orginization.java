package edu.harvard.integer.common;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Orginization extends BaseEntity {

	
	private String orginizationType = null;

	@OneToMany
	private List<Orginization> orinizations = null;


	/**
	 * @return the type
	 */
	public String getType() {
		return orginizationType;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.orginizationType = type;
	}

	/**
	 * @return the orinizations
	 */
	public List<Orginization> getOrinizations() {
		return orinizations;
	}

	/**
	 * @param orinizations
	 *            the orinizations to set
	 */
	public void setOrinizations(List<Orginization> orinizations) {
		this.orinizations = orinizations;
	}

}
