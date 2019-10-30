package com.assignment.CSVApplication.payload;

import org.springframework.stereotype.Component;

@Component
public class UploadFileResponse extends FIleUploadResponseWithError{

	private String noOfRowsParsed;
	private String noOfRowsFailed;
	private String errorFileURL;
	
	public UploadFileResponse() {
		super("");
	}
	
	
	public UploadFileResponse(String noOfRowsParsed, String noOfRowsFailed, String errorFileURL) {
		super("");
		this.noOfRowsParsed = noOfRowsParsed;
		this.noOfRowsFailed = noOfRowsFailed;
		this.errorFileURL = errorFileURL;
	}
	public String getNoOfRowsParsed() {
		return noOfRowsParsed;
	}
	public void setNoOfRowsParsed(String noOfRowsParsed) {
		this.noOfRowsParsed = noOfRowsParsed;
	}
	public String getNoOfRowsFailed() {
		return noOfRowsFailed;
	}
	public void setNoOfRowsFailed(String noOfRowsFailed) {
		this.noOfRowsFailed = noOfRowsFailed;
	}
	public String getErrorFileURL() {
		return errorFileURL ;
	}
	public void setErrorFileURL(String errorFileURL) {
		this.errorFileURL = errorFileURL;
	}

}
