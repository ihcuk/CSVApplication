package com.assignment.CSVApplication.controllers;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.assignment.CSVApplication.configurations.CSVApplicationConstants;
import com.assignment.CSVApplication.payload.FIleUploadResponseWithError;
import com.assignment.CSVApplication.services.UserService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@RestController("/csvApplication")
public class CSVProcessController {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CSVProcessController.class);
	
	@Autowired
	UserService userService;
	
	@GetMapping("/")
	public String testService() {
		return "Testing";
	}
	
	@PostMapping("/register")
	public FIleUploadResponseWithError register(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException, ExecutionException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, InterruptedException {
		
		return file == null ? new FIleUploadResponseWithError(CSVApplicationConstants.CSV_FILE_NOT_FOUND)  : 
			(file.isEmpty() ? new FIleUploadResponseWithError(CSVApplicationConstants.CSV_FILE_EMPTY) : 
				(file.getOriginalFilename().indexOf(".csv") < 0 ?  new FIleUploadResponseWithError(CSVApplicationConstants.CSV_FILE_NOT_SUPPORTED) : 
					(this.userService.parseUserCSVFile(file)))) ;  
	}
	
	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = this.userService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            //contentType = "application/octet-stream";
        	contentType = "text/csv";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
