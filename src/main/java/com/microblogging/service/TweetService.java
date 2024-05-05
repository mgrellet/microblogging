package com.microblogging.service;

import com.microblogging.domain.Following;
import com.microblogging.domain.Tweet;
import com.microblogging.domain.User;
import com.microblogging.dto.TweetDto;
import com.microblogging.repository.FollowingRepository;
import com.microblogging.repository.TweetRepository;
import com.microblogging.repository.UserRepository;
import com.microblogging.service.exceptions.InvalidTweetException;
import com.microblogging.service.exceptions.UserNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TweetService {

  private final UserRepository userRepository;
  private final TweetRepository tweetRepository;
  private final FollowingRepository followingRepository;

  public TweetService(UserRepository userRepository, TweetRepository tweetRepository,
      FollowingRepository followingRepository) {
    this.userRepository = userRepository;
    this.tweetRepository = tweetRepository;
    this.followingRepository = followingRepository;
  }

  /**
   * Creates a new tweet with the provided tweet information and userName.
   *
   * @param tweetDto the tweetDto containing the tweet text
   * @param userName the username of the user creating the tweet
   * @return the created tweetDto
   */
  public TweetDto createTweet(TweetDto tweetDto, String userName) {
    checkUserExistence(userName);
    validateTweetText(tweetDto.getTweet());
    Tweet tweet = Tweet.builder()
        .tweet(tweetDto.getTweet())
        .userName(userName)
        .creationDate(new Date())
        .build();

    tweetRepository.save(tweet);
    return entityToDto(tweet);
  }


  /**
   * Retrieves the user following tweets as timeline.
   *
   * @param userName          the username of the user
   * @param followingUserName the username of the following user
   * @return a list of tweet DTOs representing the user timeline
   */
  public List<TweetDto> getUserTimeline(String userName, String followingUserName) {
    checkUserExistence(userName);
    checkUserExistence(followingUserName);
    List<Following> followingList = followingRepository
        .findByUserNameAndFollowing(userName, followingUserName);
    return getTweetsByFollowing(followingList);
  }

  /**
   * Retrieves the tweets as timeline of all the users that current user follows.
   *
   * @param userName the username of the user
   * @return a list of tweet DTOs representing the user timeline
   */
  public List<TweetDto> getTimeline(String userName) {
    checkUserExistence(userName);
    List<Following> followingList = followingRepository.findByUserName(userName);
    return getTweetsByFollowing(followingList);
  }

  private List<TweetDto> getTweetsByFollowing(List<Following> followingList) {
    List<TweetDto> tweetDtos = new ArrayList<>();
    followingList.forEach(following -> {
      List<Tweet> tweets = tweetRepository.findByUserName(following.getFollowing());
      tweets.parallelStream().forEach(tweet -> tweetDtos.add(entityToDto(tweet)));
    });

    return orderByDateDesc(tweetDtos);
  }

  private List<TweetDto> orderByDateDesc(List<TweetDto> list) {
    return list.stream()
        .sorted(Comparator.comparing(TweetDto::getCreationDate).reversed())
        .toList();
  }

  private TweetDto entityToDto(Tweet tweet) {
    return TweetDto.builder()
        .tweet(tweet.getTweet())
        .userName(tweet.getUserName())
        .creationDate(tweet.getCreationDate())
        .build();
  }

  private void checkUserExistence(String userName) {
    Optional<User> user = userRepository.findById(userName);
    if (user.isEmpty()) {
      throw new UserNotFoundException(String.format("User with user name %s not found", userName));
    }
  }

  private void validateTweetText(String tweet) {
    if (tweet == null || tweet.isEmpty() || tweet.length() > 280) {
      throw new InvalidTweetException("Tweet cannot be empty or greater than 280 characters");
    }
  }
}
