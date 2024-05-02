package com.microblogging.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.microblogging.domain.User;
import com.microblogging.dto.UserDto;
import com.microblogging.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  List<UserDto> mockUserDtoList;
  UserDto mockDtoUser;
  List<User> mockUserListEntity;
  User mockUserEntity;

  String userName = "@test";

  @BeforeEach
  public void before() {
    MockitoAnnotations.openMocks(this);
    mockDtoUser = UserDto.builder().userName(userName).followers(0).following(0).build();
    mockUserDtoList = List.of(mockDtoUser);

    mockUserEntity = User.builder().userName(userName).followers(0).following(0).build();
    mockUserListEntity = List.of(mockUserEntity);
  }

  @Test
  void getUsersTest() throws Exception {

    when(userRepository.findAll()).thenReturn(mockUserListEntity);
    var result = userService.getUsers();

    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(1, result.size()),
        () -> assertEquals(userName, result.get(0).getUserName()));
  }

  @Test
  void getSingleUserTest() throws Exception {

    when(userRepository.findById(any(String.class))).thenReturn(
        Optional.ofNullable(mockUserEntity));
    var result = userService.getUser(userName);

    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(userName, result.getUserName()));
  }

  @Test
  void createNewUser() throws Exception {

    when(userRepository.findById(any(String.class))).thenReturn(
        Optional.ofNullable(mockUserEntity));
    when(userRepository.save(any(User.class))).thenReturn(mockUserEntity);

    var result = userService.createUser(mockDtoUser);

    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(userName, result.getUserName()));
  }

  @Test
  void createNonExistingUser() throws Exception {

    when(userRepository.findById(any(String.class))).thenReturn(
        Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(mockUserEntity);

    var result = userService.createUser(mockDtoUser);

    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(userName, result.getUserName()));
  }

}
