package com.example.mediaplayer;

import com.example.mediaplayer.AudioPlayerServis.LocalBinder;

import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;

public class PlayerScreen extends Activity implements OnSeekBarChangeListener {
	private AudioPlayerServis audioPlayerService;
	private boolean audioPlayerServisBound = false;
	private statusPlayer statusAudioPlayer;
	private TextView statusAudioPlayerText;
	private Button playButton;
	private Intent intentAudioPlayer;
	private BroadcastReceiver broadcastResiverServis;
	public final static String BROADCAST_ACTION = "musicstopped";
	private final int maxVolume = 15;
	private AudioManager audioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_screen);

		statusAudioPlayerText = (TextView) findViewById(R.id.statusAudioPlayerText);
		playButton = (Button) findViewById(R.id.PlayButton);
		SeekBar seekBarVolume = (SeekBar) findViewById(R.id.seekBarVolume);
		seekBarVolume.setOnSeekBarChangeListener(this);

		intentAudioPlayer = new Intent(this, AudioPlayerServis.class);
		startService(intentAudioPlayer);
		bindService(intentAudioPlayer, mConnection, Context.BIND_AUTO_CREATE);

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);

		broadcastResiverServis = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {
				statusAudioPlayer = statusPlayer.IDLE;
				changeTitle(statusAudioPlayer);
			}

		};
		IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
		registerReceiver(broadcastResiverServis, intFilt);

	}

	public void pressButton(View v) {

		if (audioPlayerServisBound) {
			audioPlayerService.pressButtonPlay();
			getStatus();
		}
	}
	
	private void getStatus() {
		statusAudioPlayer = audioPlayerService.getStatusPlayer();
		changeTitle(statusAudioPlayer);
	}

	private void changeTitle(statusPlayer statusAudioPlayer) {
		switch (statusAudioPlayer) {
		case IDLE:

			statusAudioPlayerText.setText(R.string.status_idle);
			playButton.setText(R.string.button_play);

			break;

		case PLAYING:

			statusAudioPlayerText.setText(R.string.status_play);
			playButton.setText(R.string.button_paused);

			break;

		case PAUSED:

			statusAudioPlayerText.setText(R.string.status_paused);
			playButton.setText(R.string.button_play);

			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mConnection != null) {
			unbindService(mConnection);
			mConnection = null;
		}
	};

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {

			LocalBinder binder = (LocalBinder) service;
			audioPlayerService = binder.getService();
			audioPlayerServisBound = true;
			getStatus();
		}

		public void onServiceDisconnected(ComponentName arg0) {
			audioPlayerServisBound = false;
		}
	};

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		int setVolume = (int) (progress * maxVolume / 100);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, setVolume, 0);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}