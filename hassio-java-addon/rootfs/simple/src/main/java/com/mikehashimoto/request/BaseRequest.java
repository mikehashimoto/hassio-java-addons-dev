package com.mikehashimoto.request;

public class BaseRequest implements Request {

	@Override
	public long getID() {
		return _id;
	}

	protected BaseRequest(long id) {
		_id = id;
	}

	private final long _id;

}