package com.example.newton;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SAMPLE_RATE = 44100;
    private SeekBar freqBar;
    private SeekBar posBar;
    private TextView freqText;
    private TextView posText;
    private TextView statusText;
    private AudioTrack audioTrack;
    private volatile boolean isSimulating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        freqBar = findViewById(R.id.freqBar);
        posBar = findViewById(R.id.posBar);
        freqText = findViewById(R.id.freqText);
        posText = findViewById(R.id.posText);
        statusText = findViewById(R.id.statusText);

        Button btnOriginal = findViewById(R.id.btnOriginal);
        Button btnApproaching = findViewById(R.id.btnApproaching);
        Button btnReceding = findViewById(R.id.btnReceding);
        Button btnSimulate = findViewById(R.id.btnSimulate);
        Button btnStop = findViewById(R.id.btnStop);

        freqBar.setProgress(500);
        posBar.setProgress(50);

        updateLabels();

        freqBar.setOnSeekBarChangeListener(new SimpleSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateLabels();
            }
        });

        posBar.setOnSeekBarChangeListener(new SimpleSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePositionLabel();
            }
        });

        btnOriginal.setOnClickListener(v -> playTone(1.0, 1.0, "Original (Stationary)"));
        btnApproaching.setOnClickListener(v -> playTone(1.2, 1.0, "Approaching (Higher Pitch)"));
        btnReceding.setOnClickListener(v -> playTone(0.8, 0.7, "Receding (Lower Pitch)"));
        btnSimulate.setOnClickListener(v -> startSimulation());
        btnStop.setOnClickListener(v -> stopAudio());
    }

    private void updateLabels() {
        int freq = Math.max(100, freqBar.getProgress());
        freqText.setText("Base Frequency: " + freq + " Hz");
        updatePositionLabel();
    }

    private void updatePositionLabel() {
        int pos = posBar.getProgress();
        if (pos < 45) {
            posText.setText("Position: Approaching (Left)");
        } else if (pos > 55) {
            posText.setText("Position: Receding (Right)");
        } else {
            posText.setText("Position: At Observer (Center)");
        }
    }

    private void startSimulation() {
        if (isSimulating) return;
        isSimulating = true;
        new Thread(() -> {
            for (int pos = 0; pos <= 100 && isSimulating; pos += 2) {
                final int currentPos = pos;
                runOnUiThread(() -> {
                    posBar.setProgress(currentPos);
                    updatePositionLabel();
                    statusText.setText("Vehicle Moving... " + currentPos + "%");
                });

                double freqScale = (currentPos <= 50)
                        ? (1.0 + (50 - currentPos) / 250.0)
                        : (1.0 - (currentPos - 50) / 250.0);

                double loudness = 1.0 - Math.abs(currentPos - 50) / 100.0;

                playShortChunk(freqScale, loudness);

                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    break;
                }
            }
            isSimulating = false;
            runOnUiThread(() -> statusText.setText("Status: Simulation Completed"));
        }).start();
    }

    private void playTone(double freqScale, double loudness, String mode) {
        stopAudio();
        isSimulating = false;
        int baseFreq = Math.max(100, freqBar.getProgress());
        double targetFreq = baseFreq * freqScale;
        statusText.setText("Mode: " + mode + " | Freq: " + (int) targetFreq + " Hz");

        new Thread(() -> {
            int durationSec = 2;
            int numSamples = SAMPLE_RATE * durationSec;
            short[] buffer = new short[numSamples];

            for (int i = 0; i < numSamples; i++) {
                double t = (double) i / SAMPLE_RATE;
                double wave = Math.sin(2.0 * Math.PI * targetFreq * t);
                buffer[i] = (short) (wave * loudness * 32767);
            }
            playBuffer(buffer);
        }).start();
    }

    private void playShortChunk(double freqScale, double loudness) {
        int baseFreq = Math.max(100, freqBar.getProgress());
        double targetFreq = baseFreq * freqScale;
        double durationSec = 0.08;
        int numSamples = (int) (SAMPLE_RATE * durationSec);
        short[] buffer = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / SAMPLE_RATE;
            double wave = Math.sin(2.0 * Math.PI * targetFreq * t);
            buffer[i] = (short) (wave * loudness * 32767);
        }
        playBuffer(buffer);
    }

    private synchronized void playBuffer(short[] buffer) {
        stopAudioTrackOnly();
        int minBufSize = AudioTrack.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        );

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        AudioFormat audioFormat = new AudioFormat.Builder()
                .setSampleRate(SAMPLE_RATE)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build();

        audioTrack = new AudioTrack.Builder()
                .setAudioAttributes(audioAttributes)
                .setAudioFormat(audioFormat)
                .setBufferSizeInBytes(Math.max(minBufSize, buffer.length * 2))
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build();

        audioTrack.write(buffer, 0, buffer.length);
        audioTrack.play();
    }

    private synchronized void stopAudioTrackOnly() {
        if (audioTrack != null) {
            try {
                if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                    audioTrack.stop();
                }
            } catch (Exception ignored) {}
            audioTrack.release();
            audioTrack = null;
        }
    }

    private void stopAudio() {
        isSimulating = false;
        stopAudioTrackOnly();
        statusText.setText("Status: Stopped");
    }

    @Override
    protected void onDestroy() {
        stopAudio();
        super.onDestroy();
    }

    private abstract static class SimpleSeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}
