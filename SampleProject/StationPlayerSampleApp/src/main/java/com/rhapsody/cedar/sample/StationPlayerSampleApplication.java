package com.rhapsody.cedar.sample;

import android.content.Intent;

import com.rhapsody.cedar.player.notification.NotificationAction;
import com.rhapsody.cedar.sample.library.AppInfo;
import com.rhapsody.cedar.sample.library.RhapsodySampleApplication;
import com.rhapsody.cedar.sample.list.StationListActivity;
import com.rhapsody.cedar.station.StationPlayer;

public class StationPlayerSampleApplication extends RhapsodySampleApplication {

    StationPlayer stationPlayer;

    public StationPlayer getStationPlayer() {
        return stationPlayer;
    }

    public void setStationPlayer(StationPlayer newStationPlayer) {
        boolean isStationPlayerSet = stationPlayer != null;
        if(isStationPlayerSet && isTheSameStationPlayer(stationPlayer, newStationPlayer)) {
            return;
        }
        if(isStationPlayerSet) {
            stationPlayer.getPlayer().stop();
        }
        stationPlayer = newStationPlayer;
    }

    private boolean isTheSameStationPlayer(StationPlayer first, StationPlayer second) {
        return first.getStationId() == second.getStationId();
    }

    @Override
    public void onNotificationAction(NotificationAction action) {
        switch (action) {
            case OPEN_PLAYER:
                Intent i = new Intent(this, StationListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case NEXT:
                stationPlayer.skipForward();
                break;
            case PREVIOUS:
                stationPlayer.skipBackward();
                break;
            default:
                break;
        }
    }

    @Override
    public AppInfo getAppInfo() {
        return stationPlayerAppInfo;
    }

    private AppInfo stationPlayerAppInfo = new AppInfo() {
        @Override
        public String getApiKey() {
            return "YOUR-API-KEY";
        }

        @Override
        public String getSecret() {
            return "YOUR-SECRET";
        }

        @Override
        public String getRedirectUrl() {
            return "sample://authorize";
        }
    };

}
