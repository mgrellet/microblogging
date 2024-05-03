package com.microblogging.service;

import static java.util.Objects.nonNull;

import com.microblogging.domain.Following;
import com.microblogging.domain.User;
import com.microblogging.dto.UserDto;
import com.microblogging.repository.FollowingRepository;
import com.microblogging.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final FollowingRepository followingRepository;

  @Autowired
  public UserService(UserRepository userRepository, FollowingRepository followingRepository) {
    this.userRepository = userRepository;
    this.followingRepository = followingRepository;
  }

  public List<UserDto> getUsers() {
    List<UserDto> userDtos = new ArrayList<>();

    userRepository.findAll().parallelStream()
        .forEach(user -> userDtos.add(entityToDto(user)));

    return userDtos;
  }

  public UserDto getUser(String name) {
    Optional<User> users = userRepository.findById(name);
    return users.map(this::entityToDto).orElse(null);
  }

  public UserDto createUser(UserDto userDto) {
    UserDto existingUser = getUser(userDto.getUserName());
    if (nonNull(existingUser)) {
      return existingUser;
    }

    User user = new User();
    user.setUserName(userDto.getUserName());
    user.setFollowers(0);
    user.setFollowing(0);
    userRepository.save(user);
    return entityToDto(user);
  }

  private UserDto entityToDto(User user) {
    return UserDto.builder()
        .userName(user.getUserName())
        .followers(user.getFollowers())
        .following(user.getFollowing())
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
