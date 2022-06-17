package com.musicverse.client.player;

/**
 * A boxing exception for all checked exceptions coming from the audio player system.
 * This should be handled in places that interact with the audio player.
 */
public class MusicPlayerException extends Exception {
    public MusicPlayerException() {
        super();
    }

    public MusicPlayerException(String message) {
        super(message);
    }

    public MusicPlayerException(Throwable cause) {
        super(cause);
    }

    public MusicPlayerException(String message, Throwable cause) {
        super(message, cause);
    }

    protected MusicPlayerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
