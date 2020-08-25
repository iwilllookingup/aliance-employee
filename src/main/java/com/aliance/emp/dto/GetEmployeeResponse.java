package com.aliance.emp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetEmployeeResponse {
  private int id;

  private String name;

  private String surname;

  private String phone;

  private String position;
}
