package edu.harvard.integer.capabilitySetter;

public class CapabilitySetterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * This is used to specify what actual exception to generate this exception.
	 * 
	 */
	private String sourceException;


	public CapabilitySetterException( String message ) {
		super( message );
	}
	
	public CapabilitySetterException( String excepClzName, String message ) {

		super(message);
		sourceException = excepClzName;
	}

	public String getSourceExceptionName() {
		return sourceException;
	}

}
