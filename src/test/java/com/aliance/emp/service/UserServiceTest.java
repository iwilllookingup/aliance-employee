package com.aliance.emp.service;

import com.aliance.emp.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Before
  public void setup() {
    userService = new UserService(passwordEncoder, userRepository);
  }

  @After
  public void teardown() {
    userService = null;
  }

  @Test
  public void login_whenGotErrorFromDatabase_thenThrowInternalErrorException(){

  }

  @Test
  public void login_whenRetrieveNullUser_thenThrowInternalErrorException(){

  }

  @Test
  public void login_whenRetrieveUserAndGotUnMatchingHash_thenReturnFalse(){

  }

  @Test
  public void login_whenRetrieveUserAndGotMatchingHash_thenReturnTrue(){

  }

  @Test
  public void register_whenGotErrorToSaveUserIntoDatabase_thenThrowInternalErrorException(){

  }

  @Test
  public void register_whenSaveUserIntoDatabaseSuccess_thenDoNothing(){

  }

}
