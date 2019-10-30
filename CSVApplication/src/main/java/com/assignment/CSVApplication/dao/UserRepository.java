package com.assignment.CSVApplication.dao;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.assignment.CSVApplication.models.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>{

	User findByEmail(String email);
}
