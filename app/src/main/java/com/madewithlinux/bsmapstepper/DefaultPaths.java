package com.madewithlinux.bsmapstepper;

import java.io.File;

public final class DefaultPaths {

    private DefaultPaths() {}

    public static final File BEATSABER_INSTALL_FOLDER = new File(
        "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Beat Saber"
    );
    public static final File CUSTOM_LEVELS = new File(BEATSABER_INSTALL_FOLDER, "Beat Saber_Data\\CustomLevels");
    public static final File SONG_HASH_DATA = new File(BEATSABER_INSTALL_FOLDER, "UserData\\SongCore\\SongHashData.dat");
}
