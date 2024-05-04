package com.microblogging.controller;

import com.microblogging.dto.TweetDto;
import com.microblogging.service.TweetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @Operation(summary = "Create Tweet", description = "Tweet creation by current user")
  @ApiResponse(responseCode = "201", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @PostMapping
  public ResponseEntity<TweetDto> createTweet(@RequestHeader(value = "x-app-user") String userName,
      @RequestBody TweetDto tweetDto) {
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/")
        .buildAndExpand(tweetDto.getTweet())
        .toUri();
    return ResponseEntity.created(location).body(tweetService.createTweet(tweetDto, userName));
  }

  @Operation(summary = "Gets user timeline", description = "Gets the list of tweets by user as timeline")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @GetMapping("/{followingUserName}/timeline")
  public ResponseEntity<List<TweetDto>> followUser(
      @RequestHeader(value = "x-app-user") String userName,
      @PathVariable String followingUserName) {
    return ResponseEntity.ok(tweetService.getUserTimeline(userName, followingUserName));
  }

  @Operation(summary = "Gets timeline", description = "Gets the list of tweets by all user as timeline")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @GetMapping("/timeline")
  public ResponseEntity<List<TweetDto>> followUser(
      @RequestHeader(value = "x-app-user") String userName) {
    return ResponseEntity.ok(tweetService.getTimeline(userName));
  }

}
