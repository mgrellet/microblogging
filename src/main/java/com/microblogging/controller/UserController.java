package com.microblogging.controller;

import com.microblogging.dto.UserDto;
import com.microblogging.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "Get users", description = "Get all the microblogging users")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers() {
    return ResponseEntity.ok(userService.getUsers());
  }

  @Operation(summary = "Get user", description = "Get user info by user name")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @GetMapping("/{userName}")
  public ResponseEntity<UserDto> getUser(@PathVariable String userName) {
    return ResponseEntity.ok(userService.getUser(userName));
  }

  @Operation(summary = "Create user", description = "User creation")
  @ApiResponse(responseCode = "201", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @PostMapping
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/")
        .buildAndExpand(userDto.getUserName())
        .toUri();
    return ResponseEntity.created(location).body(userService.createUser(userDto));
  }

  @Operation(summary = "Follow user", description = "Follow user by user name")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @PostMapping("{userToFollow}/follow")
  public ResponseEntity<String> followUser(@RequestHeader(value = "x-app-user") String userName,
      @PathVariable String userToFollow) {
    userService.followUser(userName, userToFollow);
    return ResponseEntity.ok("Following user " + userToFollow);
  }

}
