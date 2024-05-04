package com.microblogging.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.microblogging.domain.Tweet;
import com.microblogging.domain.User;
import com.microblogging.dto.TweetDto;
import com.microblogging.dto.UserDto;
import com.microblogging.repository.TweetRepository;
import com.microblogging.repository.UserRepository;
import com.microblogging.service.exceptions.UserAlreadyExistException;
import java.util.Date;
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

  @InjectMocks
  private TweetService tweetService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private TweetRepository tweetRepository;

  List<UserDto> mockUserDtoList;
  UserDto mockDtoUser;

  List<User> mockUserListEntity;
  User mockUserEntity;

  List<TweetDto> mockTweetDtoList;
  TweetDto mockTweetDto;

  List<Tweet> mockTweetListEntity;
  Tweet mockTweetEntity;


  String userName = "@test";

  @BeforeEach
  public void before() {
    MockitoAnnotations.openMocks(this);
    mockDtoUser = UserDto.builder().userName(userName).build();
    mockUserDtoList = List.of(mockDtoUser);

    mockUserEntity = User.builder().userName(userName).build();
    mockUserListEntity = List.of(mockUserEntity);

    mockTweetEntity = Tweet.builder().userName(userName).tweet("tweet message")
        .creationDate(new Date()).build();
    mockTweetListEntity = List.of(mockTweetEntity);

    mockTweetDto = TweetDto.builder().tweet("tweet message").build();
    mockTweetDtoList = List.of(mockTweetDto);
  }

  @Test
  void getUsersTest() throws Exception {

    when(userRepository.findAll()).thenReturn(mockUserListEntity);
    when(tweetRepository.findByUserName(anyString())).thenReturn(mockTweetListEntity);

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
  void createNewUser_ExistingUser() throws Exception {

    when(userRepository.findById(any(String.class))).thenReturn(
        Optional.ofNullable(mockUserEntity));
    when(userRepository.save(any(User.class))).thenReturn(mockUserEntity);

    assertThrows(UserAlreadyExistException.class, () ->
        userService.createUser(mockDtoUser));
  }

  @Test
  void createNonExistingUser() throws Exception {

    when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(mockUserEntity);

    var result = userService.createUser(mockDtoUser);

    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(userName, result.getUserName()));
  }

}
