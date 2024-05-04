package com.microblogging.config;

import com.microblogging.domain.User;
import com.microblogging.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataInsert implements ApplicationRunner {

  private final UserRepository userRepository;

  @Autowired
  public InitialDataInsert(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    User user = User.builder()
        .userName("@user1")
        .build();

    userRepository.save(user);
  }
}
