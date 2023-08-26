package io.github.viccch.footprints.ui.find;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.os.Bundle;

import com.bumptech.glide.Glide;

import io.github.viccch.footprints.R;

public class PlayVideoActivity extends AppCompatActivity {
    public static String M_ARG_PARAM_URL = "url";
    PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        setTitle("查看视频");

        playerView = findViewById(R.id.playerView);

        playerView.setPlayer(new ExoPlayer.Builder(this).build());

        playerView.getPlayer().addMediaItem(MediaItem.fromUri(getIntent().getStringExtra(M_ARG_PARAM_URL)));

        playerView.getPlayer().play();
    }
}