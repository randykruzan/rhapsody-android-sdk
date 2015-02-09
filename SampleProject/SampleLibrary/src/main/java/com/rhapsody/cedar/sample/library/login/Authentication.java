package com.rhapsody.cedar.sample.library.login;

import com.rhapsody.cedar.sample.library.AppInfo;
import com.rhapsody.cedar.sample.library.Constants;
import com.rhapsody.cedar.session.AuthToken;

import retrofit.Callback;
import retrofit.RestAdapter;

public class Authentication {

	AuthenticationService authenticationService;
    AppInfo appInfo;

	public Authentication(AppInfo appInfo) {
		RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.ENDPOINT_HTTPS).build();
		authenticationService = adapter.create(AuthenticationService.class);
        this.appInfo = appInfo;
	}

	public void swapCodeForToken(String code, Callback<AuthToken> callback) {
		authenticationService.authenticate(
                appInfo.getApiKey(), appInfo.getSecret(),
				AuthenticationService.RESPONSE_TYPE_CODE, AuthenticationService.GRANT_TYPE_CODE,
                appInfo.getRedirectUrl(), code, callback);
	}

}
