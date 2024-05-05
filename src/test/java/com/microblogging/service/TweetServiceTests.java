package com.microblogging.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.microblogging.domain.Following;
import com.microblogging.domain.Tweet;
import com.microblogging.domain.User;
import com.microblogging.dto.TweetDto;
import com.microblogging.repository.FollowingRepository;
import com.microblogging.repository.TweetRepository;
import com.microblogging.repository.UserRepository;
import com.microblogging.service.exceptions.UserNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TweetServiceTests {

  @InjectMocks
  private TweetService tweetService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private TweetRepository tweetRepository;

  @Mock
  private FollowingRepository followingRepository;

  private final String mockUserName = "@test";
  private final String mockUserName2 = "@test2";

  private User mockUser;
  private User mockUser2;

  private TweetDto mockTweetDto;
  private List<TweetDto> mockTweets;

  private Following mockFollowing;
  private List<Following> mockFollowingList;

  private Tweet mockTweetEntity;
  List<Tweet> mockTweetListEntity;

  @BeforeEach
  public void before() {
    MockitoAnnotations.openMocks(this);
    mockUser = User.builder().userName(mockUserName).build();
    mockUser2 = User.builder().userName(mockUserName2).build();

    mockTweetDto = TweetDto.builder().userName(mockUserName).tweet("tweet message").build();
    mockTweets = List.of(mockTweetDto);

    mockFollowing = Following.builder().userName(mockUserName).following(mockUserName2).build();
    mockFollowingList = List.of(mockFollowing);

    mockTweetEntity = Tweet.builder().tweet("tweet message").userName(mockUserName)
        .creationDate(new Date()).build();
    mockTweetListEntity = List.of(mockTweetEntity);

  }

  @Test
  void createTweetTest() {

    when(userRepository.findById(mockUserName)).thenReturn(Optional.of(mockUser));
    when(tweetRepository.save(mockTweetEntity)).thenReturn(mockTweetEntity);

    TweetDto result = tweetService.createTweet(mockTweetDto, mockUserName);

    verify(userRepository).findById(mockUserName);

    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(mockUserName, result.getUserName()),
        () -> assertEquals(mockTweetDto.getTweet(), result.getTweet()));
  }

  @Test
  void createTweetTest_UserNotFound() {
    String userName = "nonExistingUser";

    when(userRepository.findById(userName)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> tweetService.createTweet(mockTweetDto, userName));
    verify(userRepository).findById(userName);
    verifyNoInteractions(tweetRepository);
  }

  @Test
  void getUserTimeLineTest() {

    when(userRepository.findById(mockUserName)).thenReturn(Optional.of(mockUser));
    when(userRepository.findById(mockUserName2)).thenReturn(Optional.of(mockUser2));
    when(followingRepository.findByUserNameAndFollowing(mockUserName, mockUserName2))
        .thenReturn(mockFollowingList);
    when(tweetRepository.findByUserName(mockUserName2)).thenReturn(mockTweetListEntity);


    List<TweetDto> result = tweetService.getUserTimeline(mockUserName, mockUserName2);

    verify(userRepository).findById(mockUserName);
    verify(userRepository).findById(mockUserName2);

    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(mockTweets.get(0).getUserName(), result.get(0).getUserName()),
        () -> assertEquals(mockTweets.get(0).getTweet(), result.get(0).getTweet()));
  }

  @Test
  void getTimeLineTest() {

    when(userRepository.findById(mockUserName)).thenReturn(Optional.of(mockUser));
    when(followingRepository.findByUserName(mockUserName)).thenReturn(mockFollowingList);
    when(tweetRepository.findByUserName(mockUserName2)).thenReturn(mockTweetListEntity);

    List<TweetDto> result = tweetService.getTimeline(mockUserName);

    verify(userRepository).findById(mockUserName);
    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(mockTweets.get(0).getUserName(), result.get(0).getUserName()),
        () -> assertEquals(mockTweets.get(0).getTweet(), result.get(0).getTweet()));
  }
}
