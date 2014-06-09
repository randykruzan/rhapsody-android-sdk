package com.rhapsody.cedar.sample.login;

import retrofit.Callback;
import retrofit.RestAdapter;

import com.rhapsody.cedar.sample.Constants;
import com.rhapsody.cedar.session.AuthToken;

public class Authentication {

	AuthenticationService authenticationService;

	public Authentication() {
		RestAdapter adapter = new RestAdapter.Builder().setEndpoint(
				Constants.ENDPOINT_HTTPS).build();
		authenticationService = adapter.create(AuthenticationService.class);
	}

	public void swapCodeForToken(String code, Callback<AuthToken> callback) {
		authenticationService.authenticate(Constants.API_KEY, Constants.SECRET,
				AuthenticationService.RESPONSE_TYPE_CODE,
				AuthenticationService.GRANT_TYPE_CODE, Constants.REDIRECT_URI,
				code, callback);
	}

}
