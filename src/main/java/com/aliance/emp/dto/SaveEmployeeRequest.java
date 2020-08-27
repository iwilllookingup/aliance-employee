package com.aliance.emp.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveEmployeeRequest {

  @NotNull
  @Min(1)
  private int id;

  @NotEmpty
  private String name;

  @NotEmpty
  private String surname;

  private String phone;

  private String position;
}
