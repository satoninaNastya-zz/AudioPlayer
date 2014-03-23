package com.example.mediaplayer;

import com.example.mediaplayer.AudioPlayerServis.LocalBinder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public class PlayerScreen extends Activity {
	AudioPlayerServis mService;
	boolean mBound = false;
	statusPlayer status;
	TextView statusSong;
	Button playButton;
	Intent intent;
	BroadcastReceiver broadcastResiverServis;
	public final static String BROADCAST_ACTION = "musicstopped";//"ru.startandroid.develop.p0961servicebackbroadcast";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_screen);

		statusSong = (TextView) findViewById(R.id.TextStatus);
		playButton = (Button) findViewById(R.id.PlayButton);

		intent = new Intent(this, AudioPlayerServis.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		broadcastResiverServis = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {
				status = statusPlayer.IDLE;
				changeTitel(status);
			}
		};
		  IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
		  registerReceiver(broadcastResiverServis, intFilt);

	}
	
	 protected void onDestroy() {
		    super.onDestroy();

		    unregisterReceiver(broadcastResiverServis);
		  }

	public void pressButton(View v) {

		if (mBound) {
			mService.pressButtonPlay();
			status = mService.getStatusPlayer();
			changeTitel(status);
		}
	}

	private void changeTitel(statusPlayer status) {
		switch (status) {
		case IDLE:

			statusSong.setText(R.string.status_idle);
			playButton.setText(R.string.button_play);

			break;

		case PLAYING:

			statusSong.setText(R.string.status_play);
			playButton.setText(R.string.button_paused);

			break;

		case PAUSED:

			statusSong.setText(R.string.status_paused);
			playButton.setText(R.string.button_play);

			break;
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {

			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;
		}

		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};
}