package com.crio.starter.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MemePostRequest {
  @NotNull
	private String name;

  @NotNull
	private String url;

  @NotNull
	private String caption;

}
