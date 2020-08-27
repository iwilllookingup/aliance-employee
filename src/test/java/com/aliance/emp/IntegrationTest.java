package com.aliance.emp;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aliance.emp.dto.LoginRequest;
import com.aliance.emp.dto.RegisterRequest;
import com.aliance.emp.dto.SaveEmployeeRequest;
import com.aliance.emp.dto.UpdateEmployeeRequest;
import com.aliance.emp.model.Employee;
import com.aliance.emp.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Test
  public void IntegrationTestAndValidationEmployee() throws Exception {
    RegisterRequest registerReq = new RegisterRequest("username", "password");
    mockMvc.perform(post("/v1/user/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(registerReq)))
        .andExpect(status().isNoContent());

    LoginRequest loginReq = new LoginRequest("username", "password");
    String loginResponse = mockMvc.perform(post("/v1/user/login")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(loginReq)))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    String token = objectMapper.readTree(loginResponse).get("token").asText();

    SaveEmployeeRequest saveEmployeeReq = new SaveEmployeeRequest(5, "name", "surname",
        "0999999999", "developer");
    mockMvc.perform(post("/v1/emp")
        .contentType("application/json")
        .header("Authorization", token)
        .content(objectMapper.writeValueAsString(saveEmployeeReq)))
        .andExpect(status().isNoContent());

    Employee employee = employeeRepository.findById(5);
    assertThat(employee.getName(), is(saveEmployeeReq.getName()));
    assertThat(employee.getSurname(), is(saveEmployeeReq.getSurname()));
    assertThat(employee.getPhone(), is(saveEmployeeReq.getPhone()));
    assertThat(employee.getPosition(), is(saveEmployeeReq.getPosition()));

    UpdateEmployeeRequest updateEmployeeReq = new UpdateEmployeeRequest("new-name", "new-surname",
        "0888888888", "new-developer");
    mockMvc.perform(put("/v1/emp/5")
        .contentType("application/json")
        .header("Authorization", token)
        .content(objectMapper.writeValueAsString(updateEmployeeReq)))
        .andExpect(status().isNoContent());

    Employee updatedEmployee = employeeRepository.findById(5);
    assertThat(updatedEmployee.getName(), is(updateEmployeeReq.getName()));
    assertThat(updatedEmployee.getSurname(), is(updateEmployeeReq.getSurname()));
    assertThat(updatedEmployee.getPhone(), is(updateEmployeeReq.getPhone()));
    assertThat(updatedEmployee.getPosition(), is(updateEmployeeReq.getPosition()));

    mockMvc.perform(delete("/v1/emp/5")
        .contentType("application/json")
        .header("Authorization", token)
        .content(objectMapper.writeValueAsString(updateEmployeeReq)))
        .andExpect(status().isNoContent());
    Employee deletedEmployee = employeeRepository.findById(5);
    assertNull(deletedEmployee);
  }
}
