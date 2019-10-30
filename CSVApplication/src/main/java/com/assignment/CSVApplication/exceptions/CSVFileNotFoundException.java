package com.assignment.CSVApplication.exceptions;

public class CSVFileNotFoundException extends RuntimeException {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CSVFileNotFoundException(String message) {
        super(message);
    }

    public CSVFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
