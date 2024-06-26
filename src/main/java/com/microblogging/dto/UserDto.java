package com.microblogging.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {

  private String userName;
  private List<TweetDto> tweets;

}
