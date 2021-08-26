package com.crio.starter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meme {
  private String id;
  private String name;
  private String url;
  private String caption;
}