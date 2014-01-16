package edu.harvard.integer.common.topology;

import javax.persistence.Entity;

@Entity
public class SnmpV2cCredentail extends Credential {
	private String readCommunity = null;
	private String writeCommunity = null;

	/**
	 * @return the readCommunity
	 */
	public String getReadCommunity() {
		return readCommunity;
	}

	/**
	 * @param readCommunity
	 *            the readCommunity to set
	 */
	public void setReadCommunity(String readCommunity) {
		this.readCommunity = readCommunity;
	}

	/**
	 * @return the writeCommunity
	 */
	public String getWriteCommunity() {
		return writeCommunity;
	}

	/**
	 * @param writeCommunity
	 *            the writeCommunity to set
	 */
	public void setWriteCommunity(String writeCommunity) {
		this.writeCommunity = writeCommunity;
	}

}
