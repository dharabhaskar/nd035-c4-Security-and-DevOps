package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateUserRequest {

	@JsonProperty
	private String username;
	private String password;
	private String confirmPassword;
}
