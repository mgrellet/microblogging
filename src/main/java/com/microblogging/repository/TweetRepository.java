package com.microblogging.repository;

import com.microblogging.domain.Tweet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Integer> {

  List<Tweet> findByUserName(String following);
}
