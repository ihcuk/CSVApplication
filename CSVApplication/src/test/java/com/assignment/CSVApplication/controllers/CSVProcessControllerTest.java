package com.assignment.CSVApplication.controllers;

import static org.junit.Assert.*;

import org.junit.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.assignment.CSVApplication.dao.UserRepository;
import com.assignment.CSVApplication.services.UserService;

@WebMvcTest(controllers = CSVProcessController.class)
@AutoConfigureWebClient
public class CSVProcessControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserService userService;

	@MockBean
	UserRepository userRepo;

	@Test
	public void testService() throws Exception {
		MvcResult result = mockMvc.perform(get("/").header("Header", "Some-Optional-Header")).andExpect(status().isOk())
				.andReturn();
		String content = result.getResponse().getContentAsString();
		assertEquals("Testing", content);
	}

	@Test
	public void testRegister_Negative() throws Exception {
		try {
			File file = new File("D:/Personal/Shashank/PrateekKumar_CV_TVC.png");
			FileInputStream input = new FileInputStream(file);
			MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", input);

			mockMvc.perform(MockMvcRequestBuilders.post("/register")
					.header("Header", "Some-Optional-Header").content(multipartFile.getBytes()))
					.andExpect(status().is(200));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().contains("org.springframework.web.multipart.MultipartException"));
			assertTrue(e.getMessage().contains("Current request is not a multipart request"));
			assertTrue(e.getMessage().contains("Request processing failed"));
		}
	}
}
