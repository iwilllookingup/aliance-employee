package com.aliance.emp.service;

import com.aliance.emp.dto.GetEmployeeResponse;
import com.aliance.emp.dto.SaveEmployeeRequest;
import com.aliance.emp.dto.UpdateEmployeeRequest;
import com.aliance.emp.error.InternalErrorException;
import com.aliance.emp.error.ResourceAlreadyExistsException;
import com.aliance.emp.error.ResourceNotFoundException;
import com.aliance.emp.model.Employee;
import com.aliance.emp.repository.EmployeeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * This is service for manage operation with employees
 */
@Service
public class EmployeeService {

  private EmployeeRepository employeeRepository;

  @Autowired
  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  /**
   * This is method for retrieve all employees
   *
   * @return all list of employee in database
   */
  public List<GetEmployeeResponse> getAllEmployee() {
    List<Employee> employees;
    try {
      employees = employeeRepository.findAll();
    } catch (Exception e) {
      throw new InternalErrorException();
    }

    if (employees.size() == 0) {
      throw new ResourceNotFoundException();
    }

    return
        employees.stream().map(
            emp -> new GetEmployeeResponse(emp.getId(), emp.getName(), emp.getSurname(),
                emp.getPhone(), emp.getPosition())).collect(Collectors.toList());

  }

  /**
   * This is method for retrieve the employee by id
   *
   * @param id identification of each employee
   * @return one employee matching with id
   */
  public GetEmployeeResponse getEmployee(int id) {
    Employee employee;
    try {
      employee = employeeRepository.findById(id);
    } catch (Exception e) {
      throw new InternalErrorException();
    }

    if (employee == null) {
      throw new ResourceNotFoundException();
    }

    return new GetEmployeeResponse(employee.getId(), employee.getName(), employee.getSurname(),
        employee.getPhone(), employee.getPosition());
  }

  /**
   * This is method for save new employee
   *
   * @param request detail of new employee (id, name, surname, phone, position)
   */
  public void saveEmployee(SaveEmployeeRequest request) {
    Employee employee;
    try {
      employee = employeeRepository.findById(request.getId());
    } catch (Exception e) {
      throw new InternalErrorException();
    }

    if (employee != null) {
      throw new ResourceAlreadyExistsException();
    }

    try {
      employeeRepository.save(new Employee(request.getId(), request.getName(), request.getSurname(),
          request.getPhone(), request.getPosition()));
    } catch (Exception e) {
      throw new InternalErrorException();
    }
  }

  /**
   * This is method for update employee
   *
   * @param id identification of each employee
   * @param request detail for update employee
   */
  public void updateEmployee(int id, UpdateEmployeeRequest request) {
    Employee employee;
    try {
      employee = employeeRepository.findById(id);
    } catch (Exception e) {
      throw new InternalErrorException();
    }

    if (employee == null) {
      throw new ResourceNotFoundException();
    }

    if (!StringUtils.isEmpty(request.getName())) {
      employee.setName(request.getName());
    }

    if (!StringUtils.isEmpty(request.getSurname())) {
      employee.setSurname(request.getSurname());
    }

    if (!StringUtils.isEmpty(request.getPhone())) {
      employee.setPhone(request.getPhone());
    }

    if (!StringUtils.isEmpty(request.getPosition())) {
      employee.setPosition(request.getPosition());
    }

    try {
      employeeRepository.save(employee);
    } catch (Exception e) {
      throw new InternalErrorException();
    }
  }

  /**
   * This is method for delete the employee
   *
   * @param id identification of each employee
   */
  public void deleteEmployee(int id) {
    try {
      employeeRepository.deleteById(id);
    } catch (Exception e) {
      throw new InternalErrorException();
    }
  }

}
