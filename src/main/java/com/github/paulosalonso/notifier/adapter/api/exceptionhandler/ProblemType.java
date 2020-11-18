package com.github.paulosalonso.notifier.adapter.api.exceptionhandler;

import org.springframework.http.HttpStatus;

public enum ProblemType {

	NOT_FOUND("Not found", "/not-found"),
	UNRECOGNIZED_MESSAGE("Unrecognized message", "/unrecognized-message"),
	INVALID_PARAMETER("Invalid parameter", "/invalid-parameter"),
	INVALID_DATA("Invalid data", "/invalid-data"),
	INTERNAL_ERROR("Internal error", "/internal-error"),
	LOCKED("Locked resource", "/locked");
	
	private String title;
	private String uri;
	
	ProblemType(String title, String path) {
		this.title = title;
		this.uri = "https://github.com/paulosalonso/errors".concat(path);
	}

	public String getTitle() {
		return title;
	}

	public String getUri() {
		return uri;
	}

	public static ProblemType getByStatusCode(HttpStatus httpStatus) {
		switch (httpStatus) {
			case BAD_REQUEST: return INVALID_DATA;
			case NOT_FOUND: return NOT_FOUND;
			case LOCKED: return LOCKED;
			default: return INTERNAL_ERROR;
		}
	}
	
}
