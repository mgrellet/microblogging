package com.microblogging.repository;

import com.microblogging.domain.Following;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following, Integer> {

}
