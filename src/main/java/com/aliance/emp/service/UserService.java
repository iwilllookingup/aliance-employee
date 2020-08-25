package com.aliance.emp.service;

import com.aliance.emp.dto.LoginRequest;
import com.aliance.emp.dto.RegisterRequest;
import com.aliance.emp.error.InternalErrorException;
import com.aliance.emp.model.User;
import com.aliance.emp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * This is service for manage operation with user
 */
@Service
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  /**
   * This is method for authentication the user
   *
   * @param request user details (username, password)
   * @return boolean that show existing of this user
   */
  public boolean login(LoginRequest request) {
    User user;
    try {
      user = userRepository
          .findByUsername(request.getUsername());
    } catch (Exception e) {
      throw new InternalErrorException();
    }

    if (user == null) {
      throw new InternalErrorException();
    }

    return BCrypt.checkpw(request.getPassword(), user.getPassword());
  }

  /**
   * This is method for register the user
   *
   * @param request user details (username, password)
   */
  public void register(RegisterRequest request) {
    try {
      userRepository
          .save(new User(request.getUsername(), passwordEncoder.encode(request.getPassword())));
    } catch (Exception e) {
      throw new InternalErrorException();
    }
  }
}
