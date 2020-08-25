package com.aliance.emp.service;

import com.aliance.emp.repository.EmployeeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class EmployeeServiceTest {

  private EmployeeService employeeService;

  @Mock
  private EmployeeRepository employeeRepository;

  @Before
  public void setup() {
    employeeService = new EmployeeService(employeeRepository);
  }

  @After
  public void teardown() {
    employeeService = null;
  }

  @Test
  public void getAllEmployee_whenGotErrorFromDatabase_thenThrowInternalErrorException() {

  }

  @Test
  public void getAllEmployee_whenGotZeroEmployeeFromDatabase_thenThrowResourceNotFoundException() {

  }

  @Test
  public void getAllEmployee_whenGotListOfEmployeeFromDatabase_thenReturnEmployees() {

  }

  @Test
  public void getEmployee_whenGotErrorFromDatabase_thenThrowInternalErrorException() {

  }

  @Test
  public void getEmployee_whenGotNullFromDatabase_thenThrowResourceNotFoundException() {

  }

  @Test
  public void getEmployee_whenGotListOfEmployeeFromDatabase_thenReturnTheEmployee() {

  }

  @Test
  public void saveEmployee_whenGotErrorFromDatabaseWhileCheckExistingSource_thenThrowInternalErrorException() {

  }

  @Test
  public void saveEmployee_whenGotNullFromDatabaseWhileCheckExistingSource_thenThrowResourceAlreadyExistsException() {

  }

  @Test
  public void saveEmployee_whenGotErrorWhileSavingTheEmployee_thenThrowInternalErrorException() {

  }

  @Test
  public void saveEmployee_whenSaveTheEmployeeSuccess_thenDoNothing() {

  }

  @Test
  public void updateEmployee_whenGotErrorFromDatabaseWhileRetrieveExistingSource_thenThrowInternalErrorException() {

  }

  @Test
  public void updateEmployee_whenGotNullFromDatabaseWhileRetrieveExistingSource_thenThrowResourceNotFoundException() {

  }

  @Test
  public void updateEmployee_whenGotErrorFromDatabaseWhileUpdatingTheEmployee_thenThrowInternalErrorException() {

  }

  @Test
  public void updateEmployee_whenUpdateTheEmployeeSuccess_thenDoNothing() {

  }

  @Test
  public void deleteEmployee_whenGotErrorWhileDeletingEmployee_thenThrowInternalErrorException() {

  }

  @Test
  public void deleteEmployee_whenDeleteEmployeeSuccess_thenDoNothing() {

  }


}
