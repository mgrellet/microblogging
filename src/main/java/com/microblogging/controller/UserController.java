package com.microblogging.controller;

import com.microblogging.dto.Response;
import com.microblogging.dto.UserDto;
import com.microblogging.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

  public static final String SUCCESS = "success";
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Get users
   *
   * @return Response entity with the list of users
   */
  @Operation(summary = "Get users", description = "Get all the microblogging users")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @GetMapping
  public ResponseEntity<Response> getUsers() {

    try {
      Response response = Response.builder()
          .data(userService.getUsers())
          .message(SUCCESS)
          .status(HttpStatus.OK.value())
          .build();
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      Response errorResponse = Response.builder()
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .data(e.getMessage())
          .message("Error while fetching users")
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorResponse);
    }
  }

  /**
   * Get user info by username.
   *
   * @param userName the username to retrieve info
   * @return the response entity containing user info
   */
  @Operation(summary = "Get user", description = "Get user info by user name")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @GetMapping("/{userName}")
  public ResponseEntity<Response> getUser(@PathVariable String userName) {
    try {
      Response response = Response.builder()
          .data(userService.getUser(userName))
          .message(SUCCESS)
          .status(HttpStatus.OK.value())
          .build();
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      Response errorResponse = Response.builder()
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .data(e.getMessage())
          .message("Error while fetching user")
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorResponse);
    }
  }

  /**
   * Create a new user.
   *
   * @param userDto the user data to create the user
   * @return the response entity with the created user information
   */
  @Operation(summary = "Create user", description = "User creation")
  @ApiResponse(responseCode = "201", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @PostMapping
  public ResponseEntity<Response> createUser(@RequestBody UserDto userDto) {
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/")
        .buildAndExpand(userDto.getUserName())
        .toUri();
    try {
      Response response = Response.builder()
          .data(userService.createUser(userDto))
          .message(SUCCESS)
          .status(HttpStatus.CREATED.value())
          .build();
      return ResponseEntity.created(location).body(response);

    } catch (Exception e) {
      Response errorResponse = Response.builder()
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .data(e.getMessage())
          .message("Error while creating user")
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorResponse);
    }
  }

  /**
   * Follow user by username.
   *
   * @param userName     the username
   * @param userToFollow the user to follow
   * @return the response entity of the operation
   */
  @Operation(summary = "Follow user", description = "Follow user by user name")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @PostMapping("{userToFollow}/follow")
  public ResponseEntity<Response> followUser(@RequestHeader(value = "x-app-user") String userName,
      @PathVariable String userToFollow) {

    try {
      userService.followUser(userName, userToFollow);
      Response response = Response.builder()
          .data("Following user " + userToFollow)
          .message(SUCCESS)
          .status(HttpStatus.OK.value())
          .build();
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      Response errorResponse = Response.builder()
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .data(e.getMessage())
          .message("Error while following user")
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorResponse);
    }
  }

}
