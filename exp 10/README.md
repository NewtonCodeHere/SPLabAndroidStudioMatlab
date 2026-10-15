# Experiment 10: Electronic Music Synthesis

## Aim
To implement and demonstrate the synthesis of electronic music using Android Studio by generating sinusoidal audio signals at different musical frequencies and playing them sequentially through the Android device speaker using the AudioTrack class.

## Procedure
1. Create an Android application with control buttons to trigger electronic music synthesis.
2. Define musical frequencies corresponding to notes in a C major scale ($C_4, D_4, E_4, F_4, G_4, A_4, B_4, C_5$).
3. Use the formula $y[i] = \text{Amplitude} \times \sin(2\pi \cdot f \cdot t)$ to generate 16-bit PCM audio samples for each musical note.
4. Stream audio samples sequentially to the device speaker using `AudioTrack` in `MODE_STREAM`.
5. Display live feedback indicating which note is actively playing.

## Output
- **Audio Output:** Sequential playback of C major musical scale notes ($261.6\text{ Hz}$ to $523.3\text{ Hz}$).
- **Status Display:** Shows "Playing Note: C4", "D4", "E4", ..., "C5" step-by-step.
