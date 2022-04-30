package tux.pt.radio.helper;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.InputStream;
import java.util.Objects;

public class AudioHelper {

    private static Player player;
    private static Thread thread;

    public static void start() {
        Objects.requireNonNull(player);
        thread = new Thread(() -> {
            try {
                player.play();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public static void stop() {
        if (isRunning()) {
            thread.interrupt();
            thread = null;

            if (player != null) {
                player.close();
            }
        }
    }

    public static void setStream(InputStream stream) {
        try {
            player = new Player(stream);
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRunning() {
        return thread != null;
    }


}
