package com.aliance.emp.dto;

import lombok.Data;

@Data
public class UpdateEmployeeRequest {
  private String name;

  private String surname;

  private String phone;

  private String position;
}
