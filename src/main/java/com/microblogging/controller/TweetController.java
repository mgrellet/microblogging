package com.microblogging.controller;

import com.microblogging.dto.Response;
import com.microblogging.dto.TweetDto;
import com.microblogging.service.TweetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

  public static final String SUCCESS = "success";
  private final TweetService tweetService;

  @Autowired
  public TweetController(TweetService tweetService) {
    this.tweetService = tweetService;
  }


  /**
   * Create Tweet by current user.
   *
   * @param userName the username creating the tweet
   * @param tweetDto the tweet data object
   * @return the response entity with the tweet creation status
   */
  @Operation(summary = "Create Tweet", description = "Tweet creation by current user")
  @ApiResponse(responseCode = "201", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @PostMapping
  public ResponseEntity<Response> createTweet(@RequestHeader(value = "x-app-user") String userName,
      @RequestBody TweetDto tweetDto) {
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/")
        .buildAndExpand(tweetDto.getTweet())
        .toUri();

    try {
      Response response = Response.builder()
          .data(tweetService.createTweet(tweetDto, userName))
          .message(SUCCESS)
          .status(HttpStatus.OK.value())
          .build();
      return ResponseEntity.created(location).body(response);

    } catch (Exception e) {
      Response errorResponse = Response.builder()
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .data(e.getMessage())
          .message("Error while creating tweet")
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorResponse);
    }
  }

  /**
   * Gets the user timeline for a specific user and following user.
   *
   * @param userName          the username of the user
   * @param followingUserName the username of the following user
   * @return the ResponseEntity containing the user timeline
   */
  @Operation(summary = "Gets user timeline", description = "Gets the list of tweets by user as timeline")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @GetMapping("/{followingUserName}/timeline")
  public ResponseEntity<Response> followUser(
      @RequestHeader(value = "x-app-user") String userName,
      @PathVariable String followingUserName) {

    try {
      Response response = Response.builder()
          .data(tweetService.getUserTimeline(userName, followingUserName))
          .message(SUCCESS)
          .status(HttpStatus.OK.value())
          .build();
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      Response errorResponse = Response.builder()
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .data(e.getMessage())
          .message("Error while fetching user timeline")
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorResponse);
    }
  }

  /**
   * Gets timeline
   *
   * @param userName the username of the user
   * @return ResponseEntity containing the user timeline
   */
  @Operation(summary = "Gets timeline", description = "Gets the list of tweets by all user as timeline")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json"))
  @GetMapping("/timeline")
  public ResponseEntity<Response> followUser(
      @RequestHeader(value = "x-app-user") String userName) {

    try {
      Response response = Response.builder()
          .data(tweetService.getTimeline(userName))
          .message(SUCCESS)
          .status(HttpStatus.OK.value())
          .build();
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      Response errorResponse = Response.builder()
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .data(e.getMessage())
          .message("Error while fetching users timeline")
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorResponse);
    }
  }

}
