package com.microblogging.controller;

import com.microblogging.dto.UserDto;
import com.microblogging.service.UserService;
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

  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers() {
    return ResponseEntity.ok(userService.getUsers());
  }

  @GetMapping("/{userName}")
  public ResponseEntity<UserDto> getUser(@PathVariable String userName) {
    return ResponseEntity.ok(userService.getUser(userName));
  }

  @PostMapping
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/")
        .buildAndExpand(userDto.getUserName())
        .toUri();
    return ResponseEntity.created(location).body(userService.createUser(userDto));
  }

  @PostMapping("{userToFollow}/follow")
  public ResponseEntity<String> followUser(@RequestHeader(value = "x-app-user") String userName,
      @PathVariable String userToFollow) {
    userService.followUser(userName, userToFollow);
    return ResponseEntity.ok("Following user " + userToFollow);
  }

}
