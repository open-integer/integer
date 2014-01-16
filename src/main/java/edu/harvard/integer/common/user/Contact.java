package edu.harvard.integer.common.user;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.EmailAddress;
import edu.harvard.integer.common.PhoneNumber;

@Entity
public class Contact extends BaseEntity {

	private ContactType contactType = null;
	
	@Embedded
	@AttributeOverride(name="emailAddress", column=@Column(name="primaryEmail")) 
	private EmailAddress primaryEmail = null;
	
	@Embedded
	@AttributeOverride(name="emailAddress", column=@Column(name="secondaryEmail")) 
	private EmailAddress secondaryEmail = null;
	private String address1 = null;
	private String address2 = null;
	private String city = null;
	private String state = null;
	private String zipCode = null;
	
	@Embedded
	@AttributeOverride(name="phoneNumber", column=@Column(name="primaryPhone")) 
	private PhoneNumber primaryPhone = null;
	
	@Embedded
	@AttributeOverride(name="phoneNumber", column=@Column(name="secondaryPhone")) 
	private PhoneNumber secondaryPhone = null;
	
	@Embedded
	@AttributeOverride(name="phoneNumber", column=@Column(name="smsPhone")) 
	private PhoneNumber smsPhone = null;


	/**
	 * @return the contactType
	 */
	public ContactType getContactType() {
		return contactType;
	}

	/**
	 * @param contactType
	 *            the contactType to set
	 */
	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	/**
	 * @return the primaryEmail
	 */
	public EmailAddress getPrimaryEmail() {
		return primaryEmail;
	}

	/**
	 * @param primaryEmail
	 *            the primaryEmail to set
	 */
	public void setPrimaryEmail(EmailAddress primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	/**
	 * @return the secondaryEmail
	 */
	public EmailAddress getSecondaryEmail() {
		return secondaryEmail;
	}

	/**
	 * @param secondaryEmail
	 *            the secondaryEmail to set
	 */
	public void setSecondaryEmail(EmailAddress secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1
	 *            the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2
	 *            the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode
	 *            the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the primaryPhone
	 */
	public PhoneNumber getPrimaryPhone() {
		return primaryPhone;
	}

	/**
	 * @param primaryPhone
	 *            the primaryPhone to set
	 */
	public void setPrimaryPhone(PhoneNumber primaryPhone) {
		this.primaryPhone = primaryPhone;
	}

	/**
	 * @return the secondaryPhone
	 */
	public PhoneNumber getSecondaryPhone() {
		return secondaryPhone;
	}

	/**
	 * @param secondaryPhone
	 *            the secondaryPhone to set
	 */
	public void setSecondaryPhone(PhoneNumber secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}

	/**
	 * @return the smsPhone
	 */
	public PhoneNumber getSmsPhone() {
		return smsPhone;
	}

	/**
	 * @param smsPhone
	 *            the smsPhone to set
	 */
	public void setSmsPhone(PhoneNumber smsPhone) {
		this.smsPhone = smsPhone;
	}
}
