package com.microblogging.repository;

import com.microblogging.domain.Following;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following, Integer> {

  List<Following> findByUserNameAndFollowing(String userName, String followingUserName);
}
