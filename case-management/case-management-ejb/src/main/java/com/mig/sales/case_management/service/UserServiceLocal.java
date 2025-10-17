package com.mig.sales.case_management.service;

import com.mig.sales.case_management.model.User;
import javax.ejb.Local;

@Local
public interface UserServiceLocal {
    void registerUser(User user);
    User login(String username, String password);
}