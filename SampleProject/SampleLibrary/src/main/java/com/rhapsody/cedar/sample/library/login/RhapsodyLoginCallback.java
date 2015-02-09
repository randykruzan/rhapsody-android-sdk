package com.rhapsody.cedar.sample.library.login;

import com.rhapsody.cedar.session.AuthToken;

public interface RhapsodyLoginCallback{

	public void onLoginSuccess(AuthToken authToken);

	public void onLoginError(String url, Throwable e);

}
