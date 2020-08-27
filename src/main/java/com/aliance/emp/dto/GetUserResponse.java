package com.aliance.emp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserResponse {

  private String username;

  private String password;

}
