package tux.pt.radio.helper;

import com.mpatric.mp3agic.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ID3v2Helper {


    public static List<File> getContaining(File file) {
        List<File> allFiles = new ArrayList<>();
        File[] containing = file.listFiles();
        assert containing != null;
        for (File value : containing) {
            if (value.isFile()) allFiles.add(value);
            else if (value.isDirectory()) allFiles.addAll(getContaining(value));
        }
        return allFiles;
    }

    public static void rename(String host, String filename) {
        try {
            Mp3File mp3file = new Mp3File("radioNL/" + host + "/" + filename);
            ID3v2 id3v2Tag;
            if (mp3file.hasId3v2Tag()) {
                id3v2Tag = mp3file.getId3v2Tag();
            } else {
                id3v2Tag = new ID3v24Tag();
                mp3file.setId3v2Tag(id3v2Tag);
            }
            id3v2Tag.setArtist("H");
            id3v2Tag.setTitle("song from: " + host);
            id3v2Tag.setAlbum("The Album");
            id3v2Tag.setYear("2022");
            id3v2Tag.setGenre(8);
            id3v2Tag.setComment("RadioNL created by H.");
            id3v2Tag.setComposer("The Composer");
            id3v2Tag.setPublisher("H");
            id3v2Tag.setOriginalArtist("Unknown artist.");
            id3v2Tag.setAlbumArtist("Unknown artist.");
            id3v2Tag.setUrl("https://github.com/H/");

            mp3file.save("radioNL/" + host + "/" + "radio-" + filename);
        } catch (InvalidDataException | UnsupportedTagException | IOException | NotSupportedException ex) {
            ex.printStackTrace();
    } catch (Exception e) {
            System.out.println("Error: " + e);

        }
    }
}