package com.microblogging.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

  private String userName;
  private int followers;
  private int following;
  private int totalPosts;
  private List<PostDto> posts;

}
