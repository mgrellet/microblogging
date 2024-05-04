package com.microblogging.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.microblogging.domain.Following;
import com.microblogging.domain.Tweet;
import com.microblogging.domain.User;
import com.microblogging.dto.TweetDto;
import com.microblogging.dto.UserDto;
import com.microblogging.repository.FollowingRepository;
import com.microblogging.repository.TweetRepository;
import com.microblogging.repository.UserRepository;
import com.microblogging.service.exceptions.UserAlreadyExistException;
import com.microblogging.service.exceptions.UserNotFoundException;
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

  @Mock
  private FollowingRepository followingRepository;

  List<UserDto> mockUserDtoList;
  UserDto mockDtoUser;

  List<User> mockUserListEntity;
  User mockUserEntity;

  List<TweetDto> mockTweetDtoList;
  TweetDto mockTweetDto;

  List<Tweet> mockTweetListEntity;
  Tweet mockTweetEntity;

  String userName = "@test";
  String userToFollow = "@userToFollow";

  Following following;

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

    following = Following.builder().userName(userName).following(userToFollow).build();
  }

  @Test
  void getUsersTest() {

    when(userRepository.findAll()).thenReturn(mockUserListEntity);
    when(tweetRepository.findByUserName(anyString())).thenReturn(mockTweetListEntity);

    var result = userService.getUsers();

    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(1, result.size()),
        () -> assertEquals(userName, result.get(0).getUserName()));
  }

  @Test
  void getSingleUserTest() {

    when(userRepository.findById(any(String.class))).thenReturn(
        Optional.ofNullable(mockUserEntity));
    var result = userService.getUser(userName);

    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(userName, result.getUserName()));
  }

  @Test
  void createNewUserTest_ExistingUser() {

    when(userRepository.findById(any(String.class))).thenReturn(
        Optional.ofNullable(mockUserEntity));
    when(userRepository.save(any(User.class))).thenReturn(mockUserEntity);

    assertThrows(UserAlreadyExistException.class, () ->
        userService.createUser(mockDtoUser));
  }

  @Test
  void createNewUserTest_NonExistingUser() {

    when(userRepository.findById(anyString())).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(mockUserEntity);

    var result = userService.createUser(mockDtoUser);

    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(userName, result.getUserName()));
  }

  @Test
  void followUserTest() {

    when(userRepository.findById(anyString())).thenReturn(
        Optional.ofNullable(mockUserEntity));
    when(followingRepository.save(following)).thenReturn(following);

    userService.followUser(mockDtoUser.getUserName(), userToFollow);

    verify(userRepository, atLeastOnce()).findById(anyString());
    verify(followingRepository, atLeastOnce()).save(any(Following.class));
  }

  @Test
  void followUserTest_UserNotExist() {

    when(userRepository.findById(userName)).thenReturn(Optional.empty());
    when(userRepository.findById(userToFollow)).thenReturn(Optional.of(new User(userToFollow)));

    assertThrows(UserNotFoundException.class, () -> userService.followUser(userName, userToFollow));

    verify(userRepository).findById(userName);
    verifyNoInteractions(followingRepository);

  }

}
