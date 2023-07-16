package com.madewithlinux.bsmapstepper;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;

public class BsMapStepper {

    private final Path beatsaberInstallPath;
    private final Path customLevelsPath;
    private final Path songHashDataPath;
    private final ObjectMapper mapper = new ObjectMapper();

    public BsMapStepper(Path beatsaberInstallPath, Path customLevelsPath, Path songHashDataPath) {
        this.beatsaberInstallPath = beatsaberInstallPath;
        this.customLevelsPath = customLevelsPath;
        this.songHashDataPath = songHashDataPath;
    }

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers
            .newFor("BsMapStepper")
            .build()
            .defaultHelp(true)
            .description("convert beatsaber map to stepmania chart");

        parser
            .addArgument("--beatsaberInstallFolder")
            .type(Arguments.fileType().verifyIsDirectory())
            .setDefault(DefaultPaths.BEATSABER_INSTALL_FOLDER)
            .help("beatsaber install folder");
        parser
            .addArgument("--customLevels")
            .type(Arguments.fileType().verifyIsDirectory())
            .setDefault(DefaultPaths.CUSTOM_LEVELS)
            .help("beatsaber CustomLevels folder");
        parser
            .addArgument("--songHashData")
            .type(Arguments.fileType().verifyIsFile())
            .setDefault(DefaultPaths.SONG_HASH_DATA)
            .help("beatsaber SongHashData.dat path");

        MutuallyExclusiveGroup input = parser.addMutuallyExclusiveGroup("input").required(true);
        input.addArgument("-b", "--bsr").type(String.class).help("!bsr code");
        input.addArgument("-i", "--songHash").type(String.class).help("song hash or level id");
        input
            .addArgument("-s", "--songDir")
            .type(Arguments.fileType().verifyIsDirectory())
            .help("beatsaber song folder");

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        Path beatsaberInstallPath = ((File) ns.get("beatsaberInstallFolder")).toPath();
        Path songHashDataPath = ((File) ns.get("songHashData")).toPath();
        Path customLevelsPath = ((File) ns.get("customLevels")).toPath();
        try {
            BsMapStepper self = new BsMapStepper(beatsaberInstallPath, customLevelsPath, songHashDataPath);
            Path songDir = null;
            if (ns.get("songDir") != null) {
                songDir = ((File) ns.get("songDir")).toPath();
            } else if (ns.get("bsr") != null) {
                songDir = self.findSongDirByBsr(ns.get("bsr"));
            } else if (ns.get("songHash") != null) {
                songDir = self.findSongDirBySongHash(ns.get("songHash"));
            }
            self.go(songDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Path findSongDirByBsr(String bsr) {
        String bsrSpace = bsr + " ";
        String foldername = Arrays
            .stream(customLevelsPath.toFile().list((dir, name) -> name.startsWith(bsrSpace)))
            .findAny()
            .orElseThrow(() -> new RuntimeException("no song dir found with bsr code " + bsr));
        return customLevelsPath.resolve(foldername);
    }

    public Path findSongDirBySongHash(String songHash) throws StreamReadException, DatabindException, IOException {
        final String songHash2 = songHash.startsWith("custom_level_") ? songHash.substring(13, 53) : songHash;

        Map<String, SongHashDataEntry> songHashData = mapper.readValue(
            songHashDataPath.toFile(),
            new TypeReference<Map<String, SongHashDataEntry>>() {}
        );
        String relativePath = songHashData
            .entrySet()
            .stream()
            .filter(e -> e.getValue().songHash().equals(songHash2))
            .map(e -> e.getKey())
            .findAny()
            .orElseThrow(() -> new RuntimeException("song hash not found"));

        return beatsaberInstallPath.resolve(relativePath);
    }

    public void go(Path songDir) throws StreamReadException, DatabindException, IOException {
        System.out.println("songDir: " + songDir);
        Path infoDatPath = songDir.resolve("info.dat");
        InfoDat infoDat = mapper.readValue(infoDatPath.toFile(), InfoDat.class);
        System.out.println("infoDat: " + infoDat);
    }
}
