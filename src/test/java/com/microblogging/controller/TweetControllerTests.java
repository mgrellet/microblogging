package com.microblogging.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.microblogging.dto.TweetDto;
import com.microblogging.service.TweetService;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@ContextConfiguration(classes = TestContext.class)
@Import(TweetController.class)
public class TweetControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private TweetService tweetService;

  private TweetDto mockTweetDto;

  private List<TweetDto> mockTweets;

  private final String mockUserName = "@test";

  @BeforeEach
  public void beforeEach() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    mockTweetDto = TweetDto.builder().userName(mockUserName).tweet("tweet message")
        .creationDate(new Date()).build();
    mockTweets = List.of(mockTweetDto);
  }

  /**
   * { "message": "success", "status": 200, "data": { "tweet": "eeeeee", "userName": "@user2",
   * "creationDate": "2024-05-04T20:08:25.334+00:00" } }
   */
  @Test
  void createTweetTest() throws Exception {

    when(tweetService.createTweet(any(TweetDto.class), anyString())).thenReturn(mockTweetDto);

    mockMvc.perform(post("/tweet")
            .header("x-app-user", mockUserName)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"tweet\": \"tweet message\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.userName").value(mockUserName))
        .andExpect(jsonPath("$.data.tweet").value(mockTweetDto.getTweet()))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.status").value(201));
  }

  @Test
  void createTweetTest_Exception() throws Exception {

    doThrow(new RuntimeException("Error")).when(tweetService).createTweet(any(TweetDto.class),
        anyString());

    mockMvc.perform(post("/tweet")
            .header("x-app-user", mockUserName)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"tweet\": \"tweet message\"}"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.data").value("Error"))
        .andExpect(jsonPath("$.message").value("Error while creating tweet"))
        .andExpect(jsonPath("$.status").value(500));
  }

  @Test
  void getUserTimeline() throws Exception {
    when(tweetService.getUserTimeline(anyString(), anyString())).thenReturn(mockTweets);
    String followingUserName = "@user2";
    mockMvc.perform(get("/tweet/"+followingUserName+"/timeline")
            .header("x-app-user", mockUserName)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].tweet").value(mockTweets.get(0).getTweet()))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.status").value(200));
  }

  @Test
  void getUserTimeline_Exception() throws Exception {

    doThrow(new RuntimeException("Error")).when(tweetService).getUserTimeline(anyString(),
        anyString());
    String followingUserName = "@user2";
    mockMvc.perform(get("/tweet/"+followingUserName+"/timeline")
            .header("x-app-user", mockUserName)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.data").value("Error"))
        .andExpect(jsonPath("$.message").value("Error while fetching user timeline"))
        .andExpect(jsonPath("$.status").value(500));
  }

  @Test
  void getTimeline() throws Exception {
    when(tweetService.getTimeline(anyString())).thenReturn(mockTweets);
    mockMvc.perform(get("/tweet/timeline")
            .header("x-app-user", mockUserName)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].tweet").value(mockTweets.get(0).getTweet()))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.status").value(200));
  }

  @Test
  void getTimeline_Exception() throws Exception {

    doThrow(new RuntimeException("Error")).when(tweetService).getTimeline(anyString());
    mockMvc.perform(get("/tweet/timeline")
            .header("x-app-user", mockUserName)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.data").value("Error"))
        .andExpect(jsonPath("$.message").value("Error while fetching users timeline"))
        .andExpect(jsonPath("$.status").value(500));
  }

}
