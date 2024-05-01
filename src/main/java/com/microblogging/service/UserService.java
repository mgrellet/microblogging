package com.microblogging.service;

import com.microblogging.domain.User;
import com.microblogging.dto.UserDto;
import com.microblogging.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<UserDto> getUsers() {
    List<User> users = userRepository.findAll();
    List<UserDto> userDtos = new ArrayList<>();

    users.parallelStream()
        .forEach(user -> userDtos.add(entityToDto(user)));

    return userDtos;
  }

  public UserDto getUser(String name) {
    Optional<User> users = userRepository.findById(name);
    return users.map(this::entityToDto).orElse(null);
  }

  private UserDto entityToDto(User user) {
    return UserDto.builder()
        .userName(user.getUserName())
        .followers(user.getFollowers())
        .following(user.getFollowing())
        .build();
  }
}
