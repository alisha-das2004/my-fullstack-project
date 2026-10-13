package com.example.backend.dto;

public class LoginResponseDTO {   
	private Long id;
	private String fullName;
	private String email;
	private String message;

	public LoginResponseDTO() {

	}

	public LoginResponseDTO(Long id, String fullName, String email, String message) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.message = message;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
