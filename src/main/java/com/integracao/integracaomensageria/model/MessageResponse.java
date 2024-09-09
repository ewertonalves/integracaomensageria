package com.integracao.integracaomensageria.model;

public class MessageResponse {

	private String message;

	public MessageResponse(String message) {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessageResponse [message=" + message + "]";
	}

}
