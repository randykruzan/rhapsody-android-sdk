package com.rhapsody.cedar.sample.login;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rhapsody.cedar.sample.Constants;
import com.rhapsody.cedar.session.AuthToken;

public class RhapsodyAuthWebView extends WebView {

	@SuppressLint("SetJavaScriptEnabled")
	public RhapsodyAuthWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.getSettings().setJavaScriptEnabled(true);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}

	public RhapsodyAuthWebView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RhapsodyAuthWebView(Context context) {
		this(context, null);
	}

	public void loadLogin(final String loginUrl, final RhapsodyLoginCallback callback) {
		setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, final String url) {
				if (url.startsWith(Constants.REDIRECT_URI)) {
					onRedirectUrl(url, callback);
					return true;
				}
				return false;
			}
		});
		loadUrl(loginUrl);
	}

	private void onRedirectUrl(final String url, final RhapsodyLoginCallback callback) {
		Uri uri = Uri.parse(url);
		String code = uri.getQueryParameter(AuthenticationService.QUERY_CODE);
		if (callback != null && code == null) {
			callback.onLoginError(url, null);
		}

		Authentication auth = new Authentication();
		auth.swapCodeForToken(code, new Callback<AuthToken>() {
			@Override
			public void success(AuthToken authToken, Response response) {
				if (callback != null) {
					callback.onLoginSuccess(authToken);
				}
			}

			@Override
			public void failure(RetrofitError error) {
				if (callback != null) {
					callback.onLoginError(url, error.getCause());
				}
			}
		});
	}

}