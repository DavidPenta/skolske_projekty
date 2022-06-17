package com.musicverse.client.player;

import lombok.val;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class AudioPlayer {
    private File file;
    private AudioInputStream fileIn;
    private AudioInputStream filteredStream;
    private Clip audioClip;
    private int resumeFrame;
    private boolean playing;
    private float volume = 1;

    public void loadSong(File file) throws MusicPlayerException {
        closeClip();
        playing = false;
        resumeFrame = 0;
        this.file = file;
        openClip();
    }

    public void play() throws MusicPlayerException {
        closeClip();
        resumeFrame = 0;
        openClip();
        audioClip.start();
        playing = true;
    }

    public void pause() {
        closeClip();
        playing = false;
    }

    public void resume() throws MusicPlayerException {
        openClip();
        audioClip.start();
        playing = true;
    }

    public Duration getLength() throws MusicPlayerException {
        if (audioClip == null) openClip();
        return Duration.of(audioClip.getMicrosecondLength(), ChronoUnit.MICROS);
    }

    public Duration getCurrentPlaybackPosition() throws MusicPlayerException {
        if (audioClip == null) return Duration.ZERO;
        return Duration.of(audioClip.getMicrosecondPosition(), ChronoUnit.MICROS);
    }

    public void seekTo(Duration time) throws MusicPlayerException {
        long micros = time.getSeconds() * 1000000 + time.getNano() / 1000;
        boolean wasPlaying = playing;
        playing = false;
        closeClip();
        resumeFrame = 0;
        openClip();
        audioClip.setMicrosecondPosition(micros);
        if (wasPlaying) {
            playing = true;
            audioClip.start();
        }
    }

    public void setVolume(float volume) {
        this.volume = Math.max(Math.min(volume, 1), 0);
        FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(this.volume));
    }

    public float getVolume() {
        return volume;
    }

    private void closeClip() {
        if (audioClip != null) {
            audioClip.stop();
            resumeFrame = audioClip.getFramePosition();
            audioClip.close();
            try {
                filteredStream.close();
                fileIn.close();
            } catch (IOException ignored) {}
        }
        audioClip = null;
        filteredStream = null;
        fileIn = null;
    }

    private void openClip() throws MusicPlayerException {
        try {
            fileIn = AudioSystem.getAudioInputStream(file);
            val format = getOutFormat(fileIn.getFormat());
            audioClip = AudioSystem.getClip();
            filteredStream = AudioSystem.getAudioInputStream(format, fileIn);
            audioClip.open(filteredStream);
            audioClip.setFramePosition(resumeFrame);
            FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new MusicPlayerException(e);
        }
    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();

        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }
}
