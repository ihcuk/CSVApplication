package com.assignment.CSVApplication.services;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.assignment.CSVApplication.configurations.CSVApplicationConstants;
import com.assignment.CSVApplication.configurations.CSVFileUsersDetails;
import com.assignment.CSVApplication.dao.UserRepository;
import com.assignment.CSVApplication.exceptions.CSVFileNotFoundException;
import com.assignment.CSVApplication.models.Role;
import com.assignment.CSVApplication.models.User;
import com.assignment.CSVApplication.models.UserDTO;
import com.assignment.CSVApplication.payload.UploadFileResponse;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Value("${upload-dir}")
	private String dir;

	@Value("${write-dir}")
	private String writeDir;
	
	private volatile Map<User, Set<Role>> userRoleMap = new ConcurrentHashMap<User, Set<Role>>();

	private volatile List<UserDTO> invalidDTOList;

	@Autowired
	private CSVFileUsersDetails csvFileUsersDetails;
	

	protected Set<User> findAllUsers() {
		Set<User> users = (Set<User>) userRepository.findAll();
		if (users.size() > 0) {
			users.forEach(user -> {
				userRoleMap.put(user, user.getRoles());
			});
		}
		return users;
	}

	protected User findByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}

	protected String deleteUser(User user) {
		User existedUser = findByEmail(user.getEmail());
		if (existedUser != null) {
			userRepository.delete(user);
			userRoleMap.remove(user);
		}
		return existedUser != null ? "User Deleted Successfully" : "User Not Exits";
	}

	protected String deleteAllUsers() {
		userRepository.deleteAll();
		userRoleMap.clear();
		return "Users Deleted Successfully";
	}

	@SuppressWarnings("unchecked")
	public UploadFileResponse parseUserCSVFile(MultipartFile file) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

		Reader reader = new InputStreamReader(file.getInputStream());
		@SuppressWarnings("rawtypes")
		CsvToBean<UserDTO> csvToBean = new CsvToBeanBuilder(reader).withType(UserDTO.class).withIgnoreLeadingWhiteSpace(true).build();
		Iterator<UserDTO> csvUserIterator = csvToBean.iterator();
		List<UserDTO> usersDTOList = new ArrayList<UserDTO>();
		while (csvUserIterator.hasNext()) {
			UserDTO userDTO = csvUserIterator.next();
			usersDTOList.add(userDTO);
		}

		this.csvFileUsersDetails.parseUserDTOList(usersDTOList);
		UploadFileResponse uploadFileResponse = new UploadFileResponse();
		uploadFileResponse.setNoOfRowsParsed(String.valueOf(usersDTOList.size()));
		uploadFileResponse.setNoOfRowsFailed(String.valueOf(this.csvFileUsersDetails.getNoOfLinesFailed()));
		
		List<User> usersList = getUserFromUserDTO(this.csvFileUsersDetails.getValidDTOList());
		if (this.csvFileUsersDetails.getInvalidDTOList().size() > 0)
			uploadFileResponse.setErrorFileURL(this.dir +  CSVApplicationConstants.SMAPLE_CSV_FILE);
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					writeInvalidDataToCsv(getCsvFileUsersDetails().getInvalidDTOList());
				} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		saveUserToDB(usersList);
		return uploadFileResponse;
	}


	public List<User> getUserFromUserDTO(List<UserDTO> validDTOList) {
		List<User> list = new ArrayList<User>();
		validDTOList.forEach(userDTO -> {
			User user = userDTO.getUserFromUserDTO(userDTO);
			if (user != null)
				list.add(user);
		});
		return list;
	}

	
	public synchronized void writeInvalidDataToCsv(List<UserDTO> invalidDTOList)
			throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
			try (
		            Writer writer = Files.newBufferedWriter(Paths.get(this.dir+ "error.csv"));

		            CSVWriter csvWriter = new CSVWriter(writer,
		                    CSVWriter.DEFAULT_SEPARATOR,
		                    CSVWriter.NO_QUOTE_CHARACTER,
		                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
		                    CSVWriter.DEFAULT_LINE_END);
		        ) {
		            String[] headerRecord = { "EMAIL", "NAME", "ROLES", "ERRORS" };
		            csvWriter.writeNext(headerRecord);

		            invalidDTOList.forEach(userDTO ->{
		            	csvWriter.writeNext(new String[]{userDTO.getEmail(), userDTO.getName(), userDTO.getRoles(),
		           			 userDTO.getErrorMessage()});
		            });
		            
		        }
		catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(this.dir + "/" + fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new CSVFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new CSVFileNotFoundException("File not found " + fileName, ex);
        }
    }

	public void saveUserToDB(List<User> usersList) {
		this.userRepository.saveAll(usersList);
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getWriteDir() {
		return writeDir;
	}

	public void setWriteDir(String writeDir) {
		this.writeDir = writeDir;
	}

	public Map<User, Set<Role>> getUserRoleMap() {
		return userRoleMap;
	}

	public void setUserRoleMap(Map<User, Set<Role>> userRoleMap) {
		this.userRoleMap = userRoleMap;
	}

	public List<UserDTO> getInvalidDTOList() {
		return invalidDTOList;
	}

	public void setInvalidDTOList(List<UserDTO> invalidDTOList) {
		this.invalidDTOList = invalidDTOList;
	}

	public CSVFileUsersDetails getCsvFileUsersDetails() {
		return csvFileUsersDetails;
	}

	public void setCsvFileUsersDetails(CSVFileUsersDetails csvFileUsersDetails) {
		this.csvFileUsersDetails = csvFileUsersDetails;
	}
	
}
