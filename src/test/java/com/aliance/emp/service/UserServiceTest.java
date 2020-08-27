package com.aliance.emp.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.aliance.emp.dto.GetUserResponse;
import com.aliance.emp.dto.LoginRequest;
import com.aliance.emp.dto.RegisterRequest;
import com.aliance.emp.error.InternalErrorException;
import com.aliance.emp.model.User;
import com.aliance.emp.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Captor
  private ArgumentCaptor<User> userCaptor;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  private final String USERNAME = "username";
  private final String PASSWORD = "password";

  @Test
  public void login_whenGotErrorFromDatabase_thenThrowInternalErrorException() {
    expectedException.expect(InternalErrorException.class);
    when(userRepository.findByUsername(USERNAME)).thenThrow(Exception.class);

    userService.login(new LoginRequest(USERNAME, PASSWORD));
  }

  @Test
  public void login_whenRetrieveNullUser_thenThrowInternalErrorException() {
    expectedException.expect(InternalErrorException.class);
    when(userRepository.findByUsername(USERNAME)).thenReturn(null);

    userService.login(new LoginRequest(USERNAME, PASSWORD));
  }

  @Test
  public void login_whenRetrieveUserAndGotUnMatchingHash_thenReturnFalse() {
    when(userRepository.findByUsername(USERNAME)).thenReturn(new User(USERNAME, PASSWORD));
    when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(false);

    boolean exist = userService.login(new LoginRequest(USERNAME, PASSWORD));

    assertThat(exist, is(false));
  }

  @Test
  public void login_whenRetrieveUserAndGotMatchingHash_thenReturnTrue() {
    when(userRepository.findByUsername(USERNAME)).thenReturn(new User(USERNAME, PASSWORD));
    when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);

    boolean exist = userService.login(new LoginRequest(USERNAME, PASSWORD));

    assertThat(exist, is(true));
  }

  @Test
  public void register_whenGotErrorToSaveUserIntoDatabase_thenThrowInternalErrorException() {
    expectedException.expect(InternalErrorException.class);
    when(passwordEncoder.encode(PASSWORD)).thenReturn("encrypt-password");
    when(userRepository.save(userCaptor.capture())).thenThrow(Exception.class);

    userService.register(new RegisterRequest(USERNAME, PASSWORD));

    assertThat(userCaptor.getValue().getPassword(), is("encrypt-password"));
  }

  @Test
  public void register_whenSaveUserIntoDatabaseSuccess_thenDoNothing() {
    when(userRepository.save(userCaptor.capture())).thenReturn(null);
    when(passwordEncoder.encode(PASSWORD)).thenReturn("encrypt-password");

    userService.register(new RegisterRequest(USERNAME, PASSWORD));

    assertThat(userCaptor.getValue().getPassword(), is("encrypt-password"));
  }

  @Test
  public void getAll_whenGotErrorWhileFindUser_thenThrowInternalErrorException() {
    expectedException.expect(InternalErrorException.class);
    when(userRepository.findAll()).thenThrow(Exception.class);

    userService.getAll();
  }

  @Test
  public void getAll_whenFindAllUserSuccess_thenReturnUsers() {
    when(userRepository.findAll())
        .thenReturn(Collections.singletonList(new User("username", "encrypt-password")));

    List<GetUserResponse> users = userService.getAll();

    assertThat(users.size(), is(1));
    assertThat(users.get(0).getUsername(), is("username"));
    assertThat(users.get(0).getPassword(), is("encrypt-password"));
  }

}
