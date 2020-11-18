package com.github.paulosalonso.notifier.adapter.api.exceptionhandler;

public interface ExceptionUtils {

	static Throwable getRootCause(Throwable throwable) {
		
		if (throwable.getCause() != null)
			return getRootCause(throwable.getCause());
		
		return throwable;
		
	}
	
}
