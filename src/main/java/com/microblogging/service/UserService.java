package com.microblogging.service;

import static java.util.Objects.nonNull;

import com.microblogging.domain.Following;
import com.microblogging.domain.Tweet;
import com.microblogging.domain.User;
import com.microblogging.dto.TweetDto;
import com.microblogging.dto.UserDto;
import com.microblogging.repository.FollowingRepository;
import com.microblogging.repository.TweetRepository;
import com.microblogging.repository.UserRepository;
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
  public UserService(TweetService tweetService, UserRepository userRepository, FollowingRepository followingRepository,
      TweetRepository tweetRepository) {
    this.tweetService = tweetService;
    this.userRepository = userRepository;
    this.followingRepository = followingRepository;
    this.tweetRepository = tweetRepository;
  }

  public List<UserDto> getUsers() {
    List<UserDto> userDtos = new ArrayList<>();
    userRepository.findAll().parallelStream()
        .forEach(user ->
            userDtos.add(entityToDto(user, getUserTweets(user.getUserName())))
        );

    return userDtos;
  }

  public UserDto getUser(String name) {
    Optional<User> user = userRepository.findById(name);
    return user.map(value -> entityToDto(value, getUserTweets(name))).orElse(null);
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

  public UserDto createUser(UserDto userDto) {
    UserDto existingUser = getUser(userDto.getUserName());
    if (nonNull(existingUser)) {
      return existingUser;
    }

    User user = new User();
    user.setUserName(userDto.getUserName());
    userRepository.save(user);
    return entityToDto(user, new ArrayList<>());
  }

  private UserDto entityToDto(User user, List<TweetDto> tweetDtos) {
    return UserDto.builder()
        .userName(user.getUserName())
        .tweets(tweetDtos)
        .build();
  }

  public void followUser(String userName, String userToFollow) {
    Following following = Following.builder()
        .userName(userName)
        .following(userToFollow)
        .build();
    followingRepository.save(following);
  }
}
