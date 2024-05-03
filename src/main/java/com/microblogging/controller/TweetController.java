package com.microblogging.controller;

import com.microblogging.dto.TweetDto;
import com.microblogging.service.TweetService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/tweet")
public class TweetController {

  private final TweetService tweetService;

  @Autowired
  public TweetController(TweetService tweetService) {
    this.tweetService = tweetService;
  }

  @PostMapping
  public ResponseEntity<TweetDto> createTweet(@RequestHeader(value = "x-app-user") String userName,
      @RequestBody TweetDto tweetDto) {
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/")
        .buildAndExpand(tweetDto.getTweet())
        .toUri();
    return ResponseEntity.created(location).body(tweetService.createTweet(tweetDto, userName));
  }

}
