package com.microblogging.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "following")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Following {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private String userName;
  private String following;
}
