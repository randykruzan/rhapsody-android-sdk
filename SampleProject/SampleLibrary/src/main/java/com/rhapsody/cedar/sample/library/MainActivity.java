package com.rhapsody.cedar.sample.library;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rhapsody.cedar.Rhapsody;
import com.rhapsody.cedar.RhapsodyError;
import com.rhapsody.cedar.sample.library.login.RhapsodyLoginCallback;
import com.rhapsody.cedar.sample.library.login.RhapsodyLoginDialogFragment;
import com.rhapsody.cedar.session.AuthToken;
import com.rhapsody.cedar.session.SessionCallback;
import com.rhapsody.cedar.session.SessionManager;

public abstract class MainActivity extends ActionBarActivity {

    private Rhapsody rhapsody;
    private SessionManager sessionManager;
    RhapsodyLoginDialogFragment loginDialog;
    AppInfo appInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        RhapsodySampleApplication app = (RhapsodySampleApplication) getApplication();
        rhapsody = app.getRhapsody();
        appInfo = app.getAppInfo();
        sessionManager = app.getSessionManager();
        replaceActiveFragment();
	}

    protected abstract Fragment getFragment();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        menu.findItem(R.id.menu_item_login).setVisible(!sessionManager.isSessionOpen());
        menu.findItem(R.id.menu_item_logout).setVisible(sessionManager.isSessionOpen());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_login) {
            login();
            return true;
        } else if (id == R.id.menu_item_logout) {
            logout();
            invalidateOptionsMenu();
            return true;
        }
        return false;
    }

    private void login() {
        String loginUrl = rhapsody.getLoginUrl(appInfo.getRedirectUrl());
        loginDialog = RhapsodyLoginDialogFragment.newInstance(loginUrl, appInfo);
        loginDialog.setLoginCallback(loginCallback);
        loginDialog.show(getSupportFragmentManager(), "login");
    }

    private void logout() {
        sessionManager.closeSession();
        onLogout();
    }

    private void replaceActiveFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, getFragment()).commit();
    }

    protected void refreshActiveFragment() {
        getSupportFragmentManager().beginTransaction().detach(getFragment()).attach(getFragment()).commit();
    }

    protected void removeActiveFragment() {
        getSupportFragmentManager().beginTransaction().detach(getFragment()).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(loginDialog != null) {
            loginDialog.dismiss();
        }
    }

    protected abstract void onLogin();
    protected abstract void onLogout();

    RhapsodyLoginCallback loginCallback = new RhapsodyLoginCallback() {
        @Override
        public void onLoginSuccess(AuthToken authToken) {
            sessionManager.openSession(authToken, new SessionCallback() {
                @Override
                public void onSuccess() {
                    loginDialog.dismiss();
                    invalidateOptionsMenu();
                    onLogin();
                }

                @Override
                public void onError(RhapsodyError error) {
                    loginDialog.dismiss();
                    invalidateOptionsMenu();
                }
            });
        }

        @Override
        public void onLoginError(String url, Throwable e) {
            Toast.makeText(MainActivity.this, getString(R.string.login_error), Toast.LENGTH_LONG).show();
            loginDialog.dismiss();
        }

    };

}
