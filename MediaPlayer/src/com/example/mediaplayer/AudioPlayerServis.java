package com.example.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;

public class AudioPlayerServis extends Service implements OnCompletionListener {
	statusPlayer status;
	MediaPlayer Player;
	private final IBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {
		AudioPlayerServis getService() {
			return AudioPlayerServis.this;
		}
	}

	@Override
	public void onCreate() {
		status = statusPlayer.IDLE;
		createPlayer();
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void createPlayer() {
		Player = MediaPlayer.create(this, R.raw.music);
		Player.setOnCompletionListener(this);
	}

	public void pressButtonPlay() {

		switch (status) {
		case IDLE:
			
			Player.start();
			status = statusPlayer.PLAYING;

			break;
		case PLAYING:

			Player.pause();
			status = statusPlayer.PAUSED;

			break;
		case PAUSED:

			Player.start();
			status = statusPlayer.PLAYING;

			break;
		}
	}

	public statusPlayer getStatusPlayer() {
		return status;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		status = statusPlayer.IDLE;
		Intent intent = new Intent(PlayerScreen.BROADCAST_ACTION);
		sendBroadcast(intent);
	}

}
