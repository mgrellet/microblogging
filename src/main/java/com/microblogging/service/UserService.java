package com.microblogging.service;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final TweetService tweetService;

  private final UserRepository userRepository;
  private final FollowingRepository followingRepository;
  private final TweetRepository tweetRepository;

  @Autowired
  public UserService(TweetService tweetService, UserRepository userRepository,
      FollowingRepository followingRepository,
      TweetRepository tweetRepository) {
    this.tweetService = tweetService;
    this.userRepository = userRepository;
    this.followingRepository = followingRepository;
    this.tweetRepository = tweetRepository;
  }

  /**
   * Retrieves a list of all users created.
   *
   * @return a list of UserDto objects
   */
  public List<UserDto> getUsers() {
    List<UserDto> userDtos = new ArrayList<>();
    userRepository.findAll().parallelStream()
        .forEach(user ->
            userDtos.add(entityToDto(user, getUserTweets(user.getUserName())))
        );

    return userDtos;
  }

  /**
   * Retrieves a user with the given user name.
   *
   * @param name the name of the user to retrieve
   * @return the UserDto object representing the user
   * @throws UserNotFoundException if the user with the given name is not found
   */
  public UserDto getUser(String name) {
    Optional<User> user = userRepository.findById(name);
    if (user.isEmpty()) {
      throw new UserNotFoundException(String.format("User with user name %s not found", name));
    }
    return user.map(value -> entityToDto(value, getUserTweets(name))).orElse(null);
  }

  /**
   * Creates a new user based on the provided username.
   *
   * @param userDto the UserDto object with user details to create
   * @return the UserDto object representing the created user
   */
  public UserDto createUser(UserDto userDto) {
    Optional<User> existingUser = userRepository.findById(userDto.getUserName());
    if (existingUser.isPresent()) {
      throw new UserAlreadyExistException(String.format("User %s already exist",
          userDto.getUserName()));
    }

    User user = new User();
    user.setUserName(userDto.getUserName());
    userRepository.save(user);
    return entityToDto(user, new ArrayList<>());
  }

  /**
   * Creates a new follow relationship between a user and another user.
   *
   * @param userName     the username of the user who is following
   * @param userToFollow the username of the user being followed
   */
  public void followUser(String userName, String userToFollow) {
    checkUserExistence(userName);
    checkUserExistence(userToFollow);
    Following following = Following.builder()
        .userName(userName)
        .following(userToFollow)
        .build();
    followingRepository.save(following);
  }

  private List<TweetDto> getUserTweets(String name) {
    List<TweetDto> tweetDtos = new ArrayList<>();
    List<Tweet> tweets = tweetRepository.findByUserName(name);
    tweets.parallelStream().forEach(tweet ->
        tweetDtos.add(TweetDto.builder()
            .tweet(tweet.getTweet())
            .creationDate(tweet.getCreationDate())
            .build())
    );
    return tweetService.orderByDateDesc(tweetDtos);
  }

  private UserDto entityToDto(User user, List<TweetDto> tweetDtos) {
    return UserDto.builder()
        .userName(user.getUserName())
        .tweets(tweetDtos)
        .build();
  }

  private void checkUserExistence(String userName) {
    Optional<User> user = userRepository.findById(userName);
    if (user.isEmpty()) {
      throw new UserNotFoundException(String.format("User with user name %s not found", userName));
    }
  }
}
