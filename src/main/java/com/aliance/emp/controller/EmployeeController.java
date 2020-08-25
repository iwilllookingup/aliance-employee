package com.aliance.emp.controller;

import com.aliance.emp.dto.GetEmployeeResponse;
import com.aliance.emp.dto.SaveEmployeeRequest;
import com.aliance.emp.dto.UpdateEmployeeRequest;
import com.aliance.emp.service.EmployeeService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class EmployeeController {

  private final EmployeeService employeeService;

  @Autowired
  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping("/emp")
  @ResponseStatus(HttpStatus.OK)
  public List<GetEmployeeResponse> getAllEmployee() {
    return employeeService.getAllEmployee();
  }

  @GetMapping("/emp/{id}")
  @ResponseStatus(HttpStatus.OK)
  public GetEmployeeResponse getEmployee(@PathVariable int id) {
    return employeeService.getEmployee(id);
  }

  @PostMapping("/emp")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void saveEmployee(@RequestBody @Valid SaveEmployeeRequest request) {
    employeeService.saveEmployee(request);
  }

  @PutMapping("/emp/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateEmployee(@PathVariable int id,
      @RequestBody UpdateEmployeeRequest request) {
    employeeService.updateEmployee(id, request);
  }

  @DeleteMapping("/emp/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteEmployee(@PathVariable int id) {
    employeeService.deleteEmployee(id);
  }

}
