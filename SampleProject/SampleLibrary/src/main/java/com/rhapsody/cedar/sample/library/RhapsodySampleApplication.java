package com.rhapsody.cedar.sample.library;

import android.app.Application;

import com.rhapsody.cedar.Rhapsody;
import com.rhapsody.cedar.internal.*;
import com.rhapsody.cedar.player.Player;
import com.rhapsody.cedar.player.PlayerStateListener;
import com.rhapsody.cedar.player.notification.NotificationActionListener;
import com.rhapsody.cedar.session.SessionManager;

public abstract class RhapsodySampleApplication extends Application implements NotificationActionListener {

	protected Rhapsody rhapsody;
    protected Player player;
    protected SessionManager sessionManager;

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			rhapsody = Rhapsody.register(this, getAppInfo().getApiKey(), getAppInfo().getSecret());
		} catch (IllegalStateException e) {
            return;
		}
		player = rhapsody.getPlayer();
		player.setNotificationProperties(new NotificationProperties());
		player.registerNotificationActionListener(this);
        sessionManager = rhapsody.getSessionManager();
	}

	public Rhapsody getRhapsody() {
		return rhapsody;
	}

    public Player getPlayer() {
        return player;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public abstract AppInfo getAppInfo();

}
