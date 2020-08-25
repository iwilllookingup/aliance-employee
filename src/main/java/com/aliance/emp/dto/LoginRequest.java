package com.aliance.emp.dto;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;
}
