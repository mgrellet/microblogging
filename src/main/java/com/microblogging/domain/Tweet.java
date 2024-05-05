package com.microblogging.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tweet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tweet {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer tweetId;
  private String userName;
  @Size(max = 280)
  @Column(length = 280)
  private String tweet;
  private Date creationDate;
}
