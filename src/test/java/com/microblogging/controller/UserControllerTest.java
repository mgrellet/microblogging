package com.microblogging.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.microblogging.dto.UserDto;
import com.microblogging.service.UserService;
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
//@ContextConfiguration(classes = UserController.class)
@ContextConfiguration(classes = TestContext.class)
@Import(UserController.class)
class UserControllerTest {

  @MockBean
  private UserService userService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  List<UserDto> mockUserList;
  UserDto mockUser;

  @BeforeEach
  public void before() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    mockUser = UserDto.builder().userName("@test").build();
    mockUserList = List.of(mockUser);
  }

  @Test
  void getUsersTest() throws Exception {

    when(userService.getUsers()).thenReturn(mockUserList);
    mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(1))
        .andExpect(jsonPath("$.data[0].userName").value("@test"))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.status").value(200));
  }

  @Test
  void getSingleUserTest() throws Exception {

    when(userService.getUser(mockUser.getUserName())).thenReturn(mockUser);
    mockMvc.perform(get("/users/" + mockUser.getUserName()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.userName").value("@test"))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.status").value(200));
  }

  @Test
  void createUser() throws Exception {

    when(userService.createUser(any(UserDto.class))).thenReturn(mockUser);

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"userName\": \"@test\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.userName").value("@test"))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.status").value(201));
  }

}
