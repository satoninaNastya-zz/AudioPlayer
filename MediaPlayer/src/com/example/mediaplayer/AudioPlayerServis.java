package com.example.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;


public class AudioPlayerServis extends Service implements OnCompletionListener {
	private statusPlayer statusAudioPlayer;
	private MediaPlayer audioPlayer;
	private final IBinder audioPlayerServisBinder = new LocalBinder();
	
	public class LocalBinder extends Binder {
		AudioPlayerServis getService() {
			return AudioPlayerServis.this;
		}
	}

	@Override
	public void onCreate() {		
		statusAudioPlayer = statusPlayer.IDLE;
		createPlayer();
	};

	@Override
	public IBinder onBind(Intent intent) {
		return audioPlayerServisBinder;
	}

	private void createPlayer() {
		audioPlayer = MediaPlayer.create(this, R.raw.music);
		audioPlayer.setOnCompletionListener(this);
	}

	public void pressButtonPlay() {

		switch (statusAudioPlayer) {
		case IDLE:
		case PAUSED:
			audioPlayer.start();
			statusAudioPlayer = statusPlayer.PLAYING;

			break;
		case PLAYING:
			audioPlayer.pause();
			statusAudioPlayer = statusPlayer.PAUSED;

			break;
		}
	}

	public statusPlayer getStatusPlayer() {
		return statusAudioPlayer;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		statusAudioPlayer = statusPlayer.IDLE;
		Intent intent = new Intent(PlayerScreen.BROADCAST_ACTION);
		sendBroadcast(intent);
	}
}
