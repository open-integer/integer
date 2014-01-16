package edu.harvard.integer.common;

import javax.persistence.Embeddable;

@Embeddable
public class EmailAddress {

	private String emailAddress = null;

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
