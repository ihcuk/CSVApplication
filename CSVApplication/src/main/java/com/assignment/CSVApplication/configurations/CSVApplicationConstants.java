package com.assignment.CSVApplication.configurations;

import org.springframework.stereotype.Component;

@Component
public class CSVApplicationConstants {

	public static final String INVALID_EMAIL_PREFIX = "Invalid Email";
	public static final String INVALID_ROLE_PREFIX = "Invalid Role :";
	public static final String NO_OF_ROWS_PARSED_PREFIX = "no_of_rows_parsed : ";
	public static final String NO_OF_ROWS_FAILED_PREFIX = "no_of_rows_failed : ";
	public static final String ERROR_FILE_URL_PREFIX = "error_file_url :";
	public static final String CSV_FILE_EMPTY = "CSV File Empty";
	public static final String CSV_FILE_NOT_SUPPORTED = "CSV File Support Only! Please provide the file in CSV format";
	public static final String CSV_FILE_NOT_FOUND = "Please Provide the users CSV file";
	public static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	public static final String VALID_ROLES = "SA,ADMIN,USER";
	public static final String SMAPLE_CSV_FILE = "error.csv";
	
}
