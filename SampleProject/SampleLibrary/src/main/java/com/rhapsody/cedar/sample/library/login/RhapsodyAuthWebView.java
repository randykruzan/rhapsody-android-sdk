package com.rhapsody.cedar.sample.library.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rhapsody.cedar.sample.library.AppInfo;
import com.rhapsody.cedar.sample.library.Constants;
import com.rhapsody.cedar.session.AuthToken;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RhapsodyAuthWebView extends WebView {

    private AppInfo appInfo;

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

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

	public void loadLogin(final String loginUrl, final RhapsodyLoginCallback callback) {
		setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, final String url) {
                boolean shouldRedirect = url.startsWith(appInfo.getRedirectUrl());
                if(shouldRedirect) {
                    onRedirectUrl(url, callback);
                }
				return shouldRedirect;
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

		Authentication auth = new Authentication(appInfo);
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