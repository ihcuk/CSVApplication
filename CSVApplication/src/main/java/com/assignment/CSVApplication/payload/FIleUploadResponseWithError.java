package com.assignment.CSVApplication.payload;

public class FIleUploadResponseWithError {

	private String error;
	
	public FIleUploadResponseWithError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
