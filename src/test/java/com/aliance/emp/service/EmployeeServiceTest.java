package com.aliance.emp.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aliance.emp.dto.GetEmployeeResponse;
import com.aliance.emp.dto.SaveEmployeeRequest;
import com.aliance.emp.dto.UpdateEmployeeRequest;
import com.aliance.emp.error.InternalErrorException;
import com.aliance.emp.error.ResourceAlreadyExistsException;
import com.aliance.emp.error.ResourceNotFoundException;
import com.aliance.emp.model.Employee;
import com.aliance.emp.repository.EmployeeRepository;
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

public class EmployeeServiceTest {

  @InjectMocks
  private EmployeeService employeeService;

  @Mock
  private EmployeeRepository employeeRepository;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Captor
  private ArgumentCaptor<Employee> employeeCaptor;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getAllEmployee_whenGotErrorFromDatabase_thenThrowInternalErrorException() {
    expectedException.expect(InternalErrorException.class);
    when(employeeRepository.findAll()).thenThrow(Exception.class);

    employeeService.getAllEmployee();
  }

  @Test
  public void getAllEmployee_whenGotZeroEmployeeFromDatabase_thenThrowResourceNotFoundException() {
    expectedException.expect(ResourceNotFoundException.class);
    when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

    employeeService.getAllEmployee();
  }

  @Test
  public void getAllEmployee_whenGotListOfEmployeeFromDatabase_thenReturnEmployees() {
    when(employeeRepository.findAll())
        .thenReturn(Collections.singletonList(new Employee(1, "name", "surname",
            "0888888888", "developer")));

    List<GetEmployeeResponse> employees = employeeService.getAllEmployee();

    assertThat(employees.size(), is(1));
    assertThat(employees.get(0).getId(), is(1));
    assertThat(employees.get(0).getName(), is("name"));
    assertThat(employees.get(0).getSurname(), is("surname"));
    assertThat(employees.get(0).getPhone(), is("0888888888"));
    assertThat(employees.get(0).getPosition(), is("developer"));
  }

  @Test
  public void getEmployee_whenGotErrorFromDatabase_thenThrowInternalErrorException() {
    expectedException.expect(InternalErrorException.class);
    int employeeID = 1;
    when(employeeRepository.findById(employeeID)).thenThrow(Exception.class);

    employeeService.getEmployee(employeeID);
  }

  @Test
  public void getEmployee_whenGotNullFromDatabase_thenThrowResourceNotFoundException() {
    expectedException.expect(ResourceNotFoundException.class);
    int employeeID = 1;
    when(employeeRepository.findById(employeeID)).thenReturn(null);

    employeeService.getEmployee(employeeID);
  }

  @Test
  public void getEmployee_whenGotEmployeeFromDatabase_thenReturnTheEmployee() {
    int employeeID = 1;
    when(employeeRepository.findById(employeeID))
        .thenReturn(new Employee(employeeID, "name", "surname",
            "0888888888", "developer"));

    GetEmployeeResponse response = employeeService.getEmployee(employeeID);

    assertThat(response.getId(), is(employeeID));
    assertThat(response.getName(), is("name"));
    assertThat(response.getSurname(), is("surname"));
    assertThat(response.getPhone(), is("0888888888"));
    assertThat(response.getPosition(), is("developer"));
  }

  @Test
  public void saveEmployee_whenGotErrorFromDatabaseWhileCheckExistingSource_thenThrowInternalErrorException() {
    expectedException.expect(InternalErrorException.class);
    int employeeID = 1;
    SaveEmployeeRequest request = new SaveEmployeeRequest(employeeID, "test_name", "test_surname",
        "0888888888", "position");
    when(employeeRepository.findById(employeeID)).thenThrow(Exception.class);

    employeeService.saveEmployee(request);
  }

  @Test
  public void saveEmployee_whenGotNotNullFromDatabaseWhileCheckExistingSource_thenThrowResourceAlreadyExistsException() {
    expectedException.expect(ResourceAlreadyExistsException.class);
    int employeeID = 1;
    SaveEmployeeRequest request = new SaveEmployeeRequest(employeeID, "test_name", "test_surname",
        "0888888888", "position");
    when(employeeRepository.findById(employeeID)).thenReturn(new Employee());

    employeeService.saveEmployee(request);
  }

  @Test
  public void saveEmployee_whenGotErrorWhileSavingTheEmployee_thenThrowInternalErrorException() {
    expectedException.expect(InternalErrorException.class);
    int employeeID = 1;
    SaveEmployeeRequest request = new SaveEmployeeRequest(employeeID, "test_name", "test_surname",
        "0888888888", "position");
    when(employeeRepository.findById(employeeID)).thenReturn(null);
    when(employeeRepository.save(employeeCaptor.capture())).thenThrow(Exception.class);

    employeeService.saveEmployee(request);

    Employee employeeCaptureValue = employeeCaptor.getValue();
    assertThat(employeeCaptureValue.getId(), is(request.getId()));
    assertThat(employeeCaptureValue.getName(), is(request.getName()));
    assertThat(employeeCaptureValue.getSurname(), is(request.getSurname()));
    assertThat(employeeCaptureValue.getPhone(), is(request.getPhone()));
    assertThat(employeeCaptureValue.getPosition(), is(request.getPosition()));
  }

  @Test
  public void saveEmployee_whenSaveTheEmployeeSuccess_thenDoNothing() {
    int employeeID = 1;
    SaveEmployeeRequest request = new SaveEmployeeRequest(employeeID, "test_name", "test_surname",
        "0888888888", "developer");
    when(employeeRepository.findById(employeeID)).thenReturn(null);

    employeeService.saveEmployee(request);

    verify(employeeRepository).save(employeeCaptor.capture());
    Employee employeeCaptureValue = employeeCaptor.getValue();
    assertThat(employeeCaptureValue.getId(), is(request.getId()));
    assertThat(employeeCaptureValue.getName(), is(request.getName()));
    assertThat(employeeCaptureValue.getSurname(), is(request.getSurname()));
    assertThat(employeeCaptureValue.getPhone(), is(request.getPhone()));
    assertThat(employeeCaptureValue.getPosition(), is(request.getPosition()));
  }

  @Test
  public void updateEmployee_whenGotErrorFromDatabaseWhileRetrieveExistingSource_thenThrowInternalErrorException() {
    expectedException.expect(InternalErrorException.class);
    int employeeID = 1;
    UpdateEmployeeRequest request = new UpdateEmployeeRequest("test_name", "test_surname",
        "0888888888", "developer");
    when(employeeRepository.findById(employeeID)).thenThrow(Exception.class);

    employeeService.updateEmployee(employeeID, request);
  }

  @Test
  public void updateEmployee_whenGotNullFromDatabaseWhileRetrieveExistingSource_thenThrowResourceNotFoundException() {
    expectedException.expect(ResourceNotFoundException.class);
    int employeeID = 1;
    UpdateEmployeeRequest request = new UpdateEmployeeRequest("test_name", "test_surname",
        "0888888888", "developer");
    when(employeeRepository.findById(employeeID)).thenReturn(null);

    employeeService.updateEmployee(employeeID, request);
  }

  @Test
  public void updateEmployee_whenGotErrorFromDatabaseWhileUpdatingTheEmployee_thenThrowInternalErrorException() {
    expectedException.expect(InternalErrorException.class);
    int employeeID = 1;
    UpdateEmployeeRequest request = new UpdateEmployeeRequest("test_name", "test_surname",
        "0888888888", "developer");
    when(employeeRepository.findById(employeeID))
        .thenReturn(new Employee(employeeID, "name", "surname", "phone", "position"));
    when(employeeRepository.save(employeeCaptor.capture())).thenThrow(Exception.class);

    employeeService.updateEmployee(employeeID, request);
    Employee employeeCaptureValue = employeeCaptor.getValue();
    assertThat(employeeCaptureValue.getName(), is(request.getName()));
    assertThat(employeeCaptureValue.getSurname(), is(request.getSurname()));
    assertThat(employeeCaptureValue.getPhone(), is(request.getPhone()));
    assertThat(employeeCaptureValue.getPosition(), is(request.getPosition()));

  }

  @Test
  public void updateEmployee_whenUpdateTheEmployeeSuccess_thenDoNothing() {
    int employeeID = 1;
    UpdateEmployeeRequest request = new UpdateEmployeeRequest("test_name", "test_surname",
        "0888888888", "developer");
    when(employeeRepository.findById(employeeID))
        .thenReturn(new Employee(employeeID, "name", "surname", "phone", "position"));

    employeeService.updateEmployee(employeeID, request);

    verify(employeeRepository).save(employeeCaptor.capture());
    Employee employeeCaptureValue = employeeCaptor.getValue();
    assertThat(employeeCaptureValue.getName(), is(request.getName()));
    assertThat(employeeCaptureValue.getSurname(), is(request.getSurname()));
    assertThat(employeeCaptureValue.getPhone(), is(request.getPhone()));
    assertThat(employeeCaptureValue.getPosition(), is(request.getPosition()));
  }

  @Test
  public void deleteEmployee_whenGotErrorWhileDeletingEmployee_thenThrowInternalErrorException() {
    int employeeID = 1;
    expectedException.expect(InternalErrorException.class);
    doThrow(Exception.class).when(employeeRepository).deleteById(1);

    employeeService.deleteEmployee(employeeID);
  }

  @Test
  public void deleteEmployee_whenDeleteEmployeeSuccess_thenDoNothing() {
    int employeeID = 1;
    doNothing().when(employeeRepository).deleteById(1);

    employeeService.deleteEmployee(employeeID);
  }


}
