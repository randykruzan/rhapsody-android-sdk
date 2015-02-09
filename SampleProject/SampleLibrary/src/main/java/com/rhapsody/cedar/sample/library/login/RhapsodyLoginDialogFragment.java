package com.rhapsody.cedar.sample.library.login;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

import com.rhapsody.cedar.sample.library.AppInfo;

public class RhapsodyLoginDialogFragment extends DialogFragment {

	private static String BUNDLE_LOGIN_URL = "login_url";
    private static String BUNDLE_APP_INFO = "app_info";
	private RhapsodyAuthWebView webView;
	private RhapsodyLoginCallback loginCallback;

	public RhapsodyLoginDialogFragment() {
	}

	public static RhapsodyLoginDialogFragment newInstance(String loginUrl, AppInfo appInfo) {
		RhapsodyLoginDialogFragment f = new RhapsodyLoginDialogFragment();
		Bundle args = new Bundle();
		args.putString(BUNDLE_LOGIN_URL, loginUrl);
        args.putSerializable(BUNDLE_APP_INFO, appInfo);
		f.setArguments(args);
		return f;
	}

	private String getLoginUrl() {
		return getArguments().getString(BUNDLE_LOGIN_URL, "");
	}

    private AppInfo getAppInfo() {
        return (AppInfo) getArguments().getSerializable(BUNDLE_APP_INFO);
    }

	public void setLoginCallback(RhapsodyLoginCallback loginCallback) {
		this.loginCallback = loginCallback;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Window window = getDialog().getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
		RelativeLayout root = new RelativeLayout(getActivity());
		root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		webView = new RhapsodyAuthWebView(getActivity());
        webView.setAppInfo(getAppInfo());
		webView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		root.addView(webView);
		return root;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setupWebView();
	}

	public void setupWebView() {
		webView.loadLogin(getLoginUrl(), loginCallback);
	}

}
