package com.example.newton;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private AudioTrack track;
    private volatile boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnStop = findViewById(R.id.btnStop);
        statusText = findViewById(R.id.statusText);

        btnPlay.setOnClickListener(v -> {
            if (!isPlaying) {
                new Thread(this::playSynth).start();
            }
        });

        btnStop.setOnClickListener(v -> stopSynth());
    }

    private void playSynth() {
        isPlaying = true;
        runOnUiThread(() -> statusText.setText("Status: Playing Synthesized Music..."));

        int sampleRate = 44100;
        double[] notes = {261.6, 293.7, 329.6, 349.2, 392.0, 440.0, 493.9, 523.3};
        String[] noteNames = {"C4 (261.6 Hz)", "D4 (293.7 Hz)", "E4 (329.6 Hz)", "F4 (349.2 Hz)",
                "G4 (392.0 Hz)", "A4 (440.0 Hz)", "B4 (493.9 Hz)", "C5 (523.3 Hz)"};

        int minBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        );

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        AudioFormat audioFormat = new AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build();

        track = new AudioTrack.Builder()
                .setAudioAttributes(audioAttributes)
                .setAudioFormat(audioFormat)
                .setBufferSizeInBytes(Math.max(minBufferSize, sampleRate * 2))
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build();

        try {
            track.play();
            for (int noteIdx = 0; noteIdx < notes.length && isPlaying; noteIdx++) {
                double f = notes[noteIdx];
                final String currentNote = noteNames[noteIdx];

                runOnUiThread(() -> statusText.setText("Playing Note: " + currentNote));

                short[] buf = new short[sampleRate / 2];
                for (int i = 0; i < buf.length; i++) {
                    buf[i] = (short) (Math.sin(2 * Math.PI * f * i / sampleRate) * 12000);
                }
                track.write(buf, 0, buf.length);
            }
        } catch (Exception ignored) {
        } finally {
            stopSynthTrackOnly();
            isPlaying = false;
            runOnUiThread(() -> statusText.setText("Status: Playback Finished"));
        }
    }

    private synchronized void stopSynthTrackOnly() {
        if (track != null) {
            try {
                if (track.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                    track.stop();
                }
            } catch (Exception ignored) {}
            track.release();
            track = null;
        }
    }

    private void stopSynth() {
        isPlaying = false;
        stopSynthTrackOnly();
        statusText.setText("Status: Stopped");
    }

    @Override
    protected void onDestroy() {
        stopSynth();
        super.onDestroy();
    }
}
