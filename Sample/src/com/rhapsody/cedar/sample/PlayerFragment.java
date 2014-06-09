package com.rhapsody.cedar.sample;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.rhapsody.cedar.Rhapsody;
import com.rhapsody.cedar.RhapsodyError;
import com.rhapsody.cedar.player.PlaybackState;
import com.rhapsody.cedar.player.Player;
import com.rhapsody.cedar.player.PlayerStateListener;
import com.rhapsody.cedar.player.data.Track;
import com.rhapsody.cedar.sample.login.RhapsodyLoginCallback;
import com.rhapsody.cedar.sample.login.RhapsodyLoginDialogFragment;
import com.rhapsody.cedar.sample.metadata.TrackMetadata;
import com.rhapsody.cedar.session.AuthToken;
import com.rhapsody.cedar.session.SessionCallback;
import com.rhapsody.cedar.session.SessionManager;

public class PlayerFragment extends Fragment implements PlayerStateListener, OnClickListener {

	private static final int TRACK_COUNT = 10;
	private static final String LOADING = "...";
	final static int PROGRESS_SMOOTING_FACTOR = 100;
	final static int UPDATE_PROGRESS_INTERVAL_IN_MILIS = 1000 / PROGRESS_SMOOTING_FACTOR;
	
	private Rhapsody rhapsody;
	private Player player;
	private SimplePlayQueue playQueue;
	private SessionManager sessionManager;
	
	private boolean updateSeekBar;
	private TopTenAdapter topTenAdapter;

	private ListView listTopTen;
	private SeekBar seekBar;
	private View btnPlay, btnPause;
	private TextView tvTrackInfo;

	private TrackMetadata metadata;
	
	private boolean isSeeking = false;
	
	RhapsodyLoginDialogFragment loginDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RhapsodySampleApplication app = (RhapsodySampleApplication) getActivity().getApplication();
		rhapsody = app.getRhapsody();
		playQueue = app.getPlayQueue();
		player = rhapsody.getPlayer();
		sessionManager = rhapsody.getSessionManager();
		metadata = new TrackMetadata();
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_player, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setupList((ListView) view.findViewById(R.id.top_ten));
		setupButtons(view);
		setupSeekBar((SeekBar) view.findViewById(R.id.seekBar));
	}

	@Override
	public void onResume() {
		super.onResume();
		player.addStateListener(this);
		if (rhapsody.getPlayer().getPlaybackState() == PlaybackState.STOPPED) {
			playQueue.setTrackIndex(0);
		} else {
			
		}
		onStateChange(player.getPlaybackState());
	}

	@Override
	public void onPause() {
		super.onPause();
		player.removeStateListener(this);
		if(loginDialog != null) {
			loginDialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.play) {
			if (player.getCurrentTrackId() == null || player.getPlaybackState() == PlaybackState.STOPPED) {
				playTrack(0);
			} else {
				player.resume();
			}
		} else if (id == R.id.pause) {
			player.pause();
		} else if (id == R.id.next_track) {
			playNextTrack();
		} else if (id == R.id.prev_track) {
			playPreviousTrack();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.login_menu, menu);
		if (sessionManager.isSessionOpen()) {
			menu.findItem(R.id.menu_item_login).setVisible(false);
			menu.findItem(R.id.menu_item_logout).setVisible(true);
		} else {
			menu.findItem(R.id.menu_item_login).setVisible(true);
			menu.findItem(R.id.menu_item_logout).setVisible(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_item_login) {
			String loginUrl = rhapsody.getLoginUrl(Constants.REDIRECT_URI);
			loginDialog = RhapsodyLoginDialogFragment.newInstance(loginUrl);
			RhapsodyLoginCallback loginCallback = new RhapsodyLoginCallback() {

				@Override
				public void onLoginSuccess(AuthToken authToken) {
					sessionManager.openSession(authToken, new SessionCallback() {
						
						@Override
						public void onSuccess() {
							loginDialog.dismiss();
							getActivity().invalidateOptionsMenu();
						}
						
						@Override
						public void onError(RhapsodyError error) {
							loginDialog.dismiss();
							getActivity().invalidateOptionsMenu();
						}
					});
				}

				@Override
				public void onLoginError(String url, Throwable e) {
					if(e != null) {
						Toast.makeText(getActivity(), "Login error!", Toast.LENGTH_LONG).show();
					}
					loginDialog.dismiss();
				}
			};
			loginDialog.setLoginCallback(loginCallback);

			loginDialog.show(getActivity().getSupportFragmentManager(), "login");
			return true;
		} else if (id == R.id.menu_item_logout) {
			sessionManager.closeSession();
			getActivity().invalidateOptionsMenu();
			return true;
		}
		return false;
	}

	@Override
	public void onError(RhapsodyError error) {
		if(error == RhapsodyError.TOKEN_EXPIRED || error == RhapsodyError.TOKEN_INVALID){
			Toast.makeText(getActivity(), "LOGIN REQUIRED", Toast.LENGTH_LONG).show();
			getActivity().invalidateOptionsMenu();
		} else{
			Toast.makeText(getActivity(), "PLAYBACK ERROR", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onStateChange(PlaybackState state) {
		switch (state) {
		case SEEKING:
			return;
		case PLAYING:
			onPlaybackStarted();
			break;
		case STOPPED:
			updateSeekBar = false;
			break;
		default:
			break;
		}
		if(!isSeeking){
			updatePlayPauseButtons(state == PlaybackState.PLAYING);
		}
	}

	private void updateTrackInfo() {
		String trackId = player.getCurrentTrackId();
		if (trackId != null) {
			metadata.getTrackDetail(trackId, new Callback<Track>() {

				@Override
				public void failure(RetrofitError error) {
				}

				@Override
				public void success(Track track, Response response) {
					tvTrackInfo.setText(track.name);
				}
			});
		}
	}

	private void updatePlayPauseButtons(boolean isPlaying) {
		if (isPlaying) {
			btnPause.setVisibility(View.VISIBLE);
			btnPlay.setVisibility(View.GONE);
		} else {
			btnPause.setVisibility(View.GONE);
			btnPlay.setVisibility(View.VISIBLE);
		}
	}

	private void onPlaybackStarted() {
		updateTrackInfo();
		seekBar.setMax(player.getCurrentTrackDuration() * PROGRESS_SMOOTING_FACTOR);
		updateSeekBar = true;
		
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				seekBar.setProgress(player.getPlayheadPosition() * PROGRESS_SMOOTING_FACTOR);
				if (updateSeekBar) {
					handler.postDelayed(this, UPDATE_PROGRESS_INTERVAL_IN_MILIS);
				}
			}
		};
		handler.post(runnable);
	}

	private void loadTracks() {
		metadata.getTopTracks(TRACK_COUNT, 1, new Callback<List<Track>>() {

			@Override
			public void success(List<Track> tracks, Response response) {
				topTenAdapter = new TopTenAdapter(getActivity(), tracks);
				listTopTen.setAdapter(topTenAdapter);
				ArrayList<String> trackIds = new ArrayList<String>();
				for(Track track : tracks){
					trackIds.add(track.id);
				}
				playQueue.setTracks(trackIds);
			}

			@Override
			public void failure(RetrofitError error) {
			}
		});
	}

	private void playTrack(int trackIndex) {
		playQueue.setTrackIndex(trackIndex);
		playCurrentTrack();

	}

	private void playCurrentTrack(){
		tvTrackInfo.setText(LOADING);
		String trackId = playQueue.getCurrentTrack();
		if(trackId != null){
			player.play(trackId);
		} else{
			Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void playNextTrack() {
		playQueue.nextTrack();
		playCurrentTrack();
	}

	private void playPreviousTrack() {
		playQueue.previousTrack();
		playCurrentTrack();
	}

	private void setupList(ListView listTopTen) {
		this.listTopTen = listTopTen;
		listTopTen.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				playTrack(pos);
			}
		});
		loadTracks();
	}

	private void setupButtons(View view) {
		tvTrackInfo = (TextView) view.findViewById(R.id.track_info);
		btnPause = view.findViewById(R.id.pause);
		btnPause.setOnClickListener(this);
		btnPlay = view.findViewById(R.id.play);
		btnPlay.setOnClickListener(this);
		view.findViewById(R.id.next_track).setOnClickListener(this);
		view.findViewById(R.id.prev_track).setOnClickListener(this);
	}

	private void setupSeekBar(SeekBar seekBar) {
		this.seekBar = seekBar;
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			private boolean playAfterSeek;
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				isSeeking = false;
				player.seek(seekBar.getProgress() / PROGRESS_SMOOTING_FACTOR, playAfterSeek);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				isSeeking = true;
				playAfterSeek = player.getPlaybackState() == PlaybackState.PLAYING;
				updateSeekBar = false;
				player.pause();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});
	}

}
