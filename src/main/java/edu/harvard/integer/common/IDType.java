package edu.harvard.integer.common;

import javax.persistence.Embeddable;


@Embeddable
public class IDType {

	private Class classType = null;

	public Class getClassType() {
		return classType;
	}

	public void setClassType(Class classType) {
		this.classType = classType;
	}
}
