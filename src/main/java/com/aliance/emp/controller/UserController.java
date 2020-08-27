package com.aliance.emp.controller;

import com.aliance.emp.dto.GetUserResponse;
import com.aliance.emp.dto.LoginRequest;
import com.aliance.emp.dto.RegisterRequest;
import com.aliance.emp.error.UnauthorizedErrorException;
import com.aliance.emp.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConfigurationProperties("aliance.jwt")
@RequestMapping("/v1/user")
public class UserController {

  @Setter
  private String secret;

  @Setter
  private String id;

  @Setter
  private int expireTime;

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }


  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public Map<String, String> login(@RequestBody @Valid LoginRequest request) {
    boolean exist = userService.login(request);
    if (exist) {
      String token = getJWTToken(request.getUsername());
      return Collections.singletonMap("token", token);
    }
    throw new UnauthorizedErrorException();
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void register(@RequestBody @Valid RegisterRequest request) {
    userService.register(request);
  }

  @GetMapping("/")
  @ResponseStatus(HttpStatus.OK)
  public List<GetUserResponse> getAll() {
    return userService.getAll();
  }


  /**
   * This is method for generate JWT for the user
   *
   * @param username username of the user
   * @return string of JWt authentication token
   */
  private String getJWTToken(String username) {
    List<GrantedAuthority> grantedAuthorities = AuthorityUtils
        .commaSeparatedStringToAuthorityList("user");

    String token = Jwts
        .builder()
        .setId(id)
        .setSubject(username)
        .claim("authorities",
            grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expireTime))
        .signWith(SignatureAlgorithm.HS512,
            secret.getBytes()).compact();

    return "Bearer " + token;
  }
}
