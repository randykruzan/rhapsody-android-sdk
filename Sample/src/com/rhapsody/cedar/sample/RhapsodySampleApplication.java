package com.rhapsody.cedar.sample;

import static com.rhapsody.cedar.sample.Constants.API_KEY;
import static com.rhapsody.cedar.sample.Constants.SECRET;
import android.app.Application;
import android.content.Intent;

import com.rhapsody.cedar.Rhapsody;
import com.rhapsody.cedar.RhapsodyError;
import com.rhapsody.cedar.player.PlaybackState;
import com.rhapsody.cedar.player.Player;
import com.rhapsody.cedar.player.PlayerStateListener;
import com.rhapsody.cedar.player.notification.NotificationAction;
import com.rhapsody.cedar.player.notification.NotificationActionListener;

public class RhapsodySampleApplication extends Application implements NotificationActionListener, PlayerStateListener {

	private Rhapsody rhapsody;
	private SimplePlayQueue playQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			rhapsody = Rhapsody.register(this, API_KEY, SECRET);
		} catch (IllegalStateException e) {
		}
		Player player = rhapsody.getPlayer();
		player.setNotificationProperties(new NotificationProperties());
		player.registerNotificationActionListener(this);
		player.addStateListener(this);
		playQueue = new SimplePlayQueue(this);
	}

	public Rhapsody getRhapsody() {
		return rhapsody;
	}

	public SimplePlayQueue getPlayQueue() {
		return playQueue;
	}

	@Override
	public void onError(RhapsodyError error) {
	}

	@Override
	public void onStateChange(PlaybackState state) {
		switch (state) {
		case COMPLETE:
			playQueue.nextTrack();
			rhapsody.getPlayer().play(playQueue.getCurrentTrack());
			break;
		default:
			break;
		}
	}

	@Override
	public void onNotificationAction(NotificationAction action) {
		switch (action) {
		case OPEN_PLAYER:
			Intent i = new Intent(this, PlayerActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			break;
		case NEXT:
			playQueue.nextTrack();
			rhapsody.getPlayer().play(playQueue.getCurrentTrack());
			break;
		case PREVIOUS:
			playQueue.previousTrack();
			rhapsody.getPlayer().play(playQueue.getCurrentTrack());
			break;
		default:
			break;
		}
	}

}
