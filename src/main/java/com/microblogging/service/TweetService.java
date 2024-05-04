package com.microblogging.service;

import com.microblogging.domain.Following;
import com.microblogging.domain.Tweet;
import com.microblogging.dto.TweetDto;
import com.microblogging.repository.FollowingRepository;
import com.microblogging.repository.TweetRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TweetService {

  private final TweetRepository tweetRepository;
  private final FollowingRepository followingRepository;

  public TweetService(TweetRepository tweetRepository, FollowingRepository followingRepository) {
    this.tweetRepository = tweetRepository;
    this.followingRepository = followingRepository;
  }

  public TweetDto createTweet(TweetDto tweetDto, String userName) {
    Tweet tweet = Tweet.builder()
        .tweet(tweetDto.getTweet())
        .userName(userName)
        .creationDate(new Date())
        .build();

    tweetRepository.save(tweet);
    return entityToDto(tweet);
  }


  public List<TweetDto> getUserTimeline(String userName, String followingUserName) {
    List<Following> followingList = followingRepository
        .findByUserNameAndFollowing(userName, followingUserName);
    List<TweetDto> tweetDtos = new ArrayList<>();
    for (Following following : followingList) {
      List<Tweet> tweets = tweetRepository.findByUserName(following.getFollowing());
      for (Tweet tweet : tweets) {
        tweetDtos.add(entityToDto(tweet));
      }

    }
    return tweetDtos;
  }

  private TweetDto entityToDto(Tweet tweet) {
    return TweetDto.builder()
        .tweet(tweet.getTweet())
        .userName(tweet.getUserName())
        .creationDate(tweet.getCreationDate())
        .build();
  }
}
