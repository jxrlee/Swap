package com.swap;

public class HTTPDownloadTaskArgument {
	public enum Task {
	    RETRIEVAL,
	    INSERT,
	    UPDATE
	}
	
	public DBAccessDelegate delegate;
	public String url;
	public Task task;
}
