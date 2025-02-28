package com.example.multipagelab.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.core.app.NotificationCompat

import com.example.multipagelab.R

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private val CHANNEL_ID = "MusicServiceChannel"

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "MusicService")

        mediaPlayer = MediaPlayer()
        try {
            val assetFileDescriptor = assets.openFd("music.mp3")
            mediaPlayer.setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.length
            )
            mediaPlayer.prepare()
            mediaPlayer.isLooping = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START" -> startMusic()
            "PAUSE" -> pauseMusic()
            "STOP" -> stopMusic()
        }
        return START_STICKY
    }

    private fun startMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            startForeground(1, createMusicNotification(isPlaying = true))
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            startForeground(1, createMusicNotification(isPlaying = false))
        }
    }

    private fun stopMusic() {
        mediaPlayer.stop()
        stopForeground(true)
        stopSelf()
    }

    private fun createMusicNotification(isPlaying: Boolean): Notification {
        val flag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val playIntent = PendingIntent.getService(
            this, 1, Intent(this, MusicService::class.java).apply { action = "START" }, flag
        )
        val pauseIntent = PendingIntent.getService(
            this, 2, Intent(this, MusicService::class.java).apply { action = "PAUSE" }, flag
        )
        val stopIntent = PendingIntent.getService(
            this, 3, Intent(this, MusicService::class.java).apply { action = "STOP" }, flag
        )

        val playPauseAction = if (isPlaying) {
            NotificationCompat.Action(R.drawable.pause, "Pause", pauseIntent)
        } else {
            NotificationCompat.Action(R.drawable.play, "Play", playIntent)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Red Hot Chili Peppers - Show(Hey Oh)")
            .setContentText(if (isPlaying) "Играет" else "На паузе")
            .setSmallIcon(R.drawable.music_note_svgrepo_com)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(MediaStyle()
                .setShowActionsInCompactView(0, 1)
                .setMediaSession(mediaSession.sessionToken)
            )
            .addAction(playPauseAction)
            .addAction(NotificationCompat.Action(R.drawable.stop, "Stop", stopIntent))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(false)
            .setDeleteIntent(stopIntent)
            .build()

    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, "Music Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    override fun onTaskRemoved(rootIntent: Intent?) {
        stopMusic()
        super.onTaskRemoved(rootIntent)
    }
    override fun onBind(intent: Intent?): IBinder? = null
}
