package com.microblogging.service;

import com.microblogging.domain.Tweet;
import com.microblogging.dto.TweetDto;
import com.microblogging.repository.TweetRepository;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class TweetService {

  private final TweetRepository tweetRepository;

  public TweetService(TweetRepository tweetRepository) {
    this.tweetRepository = tweetRepository;
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

  private TweetDto entityToDto(Tweet tweet) {
    return TweetDto.builder()
        .tweet(tweet.getTweet())
        .creationDate(tweet.getCreationDate())
        .build();
  }
}
