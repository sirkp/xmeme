package com.crio.starter.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "memes")
public class MemeEntity {

  @Id
	private String id;
	
  private String name;
	
  private String url;

  private String caption;
}