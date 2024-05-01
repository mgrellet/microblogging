package com.microblogging.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Post {

  @Id
  private String postId;
  private String userName;
  private String postText;
}
