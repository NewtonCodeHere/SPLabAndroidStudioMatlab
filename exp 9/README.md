# Experiment 9: Doppler Effect Simulation

## Aim
To implement and demonstrate the Doppler Effect using Android Studio by generating a sinusoidal audio signal and observing the change in frequency and amplitude when a sound source approaches, passes, and moves away from an observer.

## Procedure
1. Create a new Android project and configure the layout with seekbars for base frequency and vehicle position, along with control buttons.
2. In `MainActivity.java`, generate raw PCM 16-bit sinusoidal audio waves using `AudioTrack` and `Math.sin()`.
3. Calculate observed frequency shifts based on the Doppler Effect model ($f' > f_0$ when approaching, $f' < f_0$ when receding).
4. Scale audio amplitude based on distance from the observer to reflect loudness attenuation.
5. Provide controls to play static original/approaching/receding tones as well as an automated motion simulation.

## Output
- **Approaching Sound:** Higher pitch / frequency ($f' > f_0$) with increasing volume.
- **At Observer:** Base frequency ($f' = f_0$) with peak volume.
- **Receding Sound:** Lower pitch / frequency ($f' < f_0$) with decreasing volume.
