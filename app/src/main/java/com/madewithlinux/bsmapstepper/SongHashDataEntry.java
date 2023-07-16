package com.madewithlinux.bsmapstepper;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SongHashDataEntry(
    @JsonProperty("directoryHash") long directoryHash,
    @JsonProperty("songHash") String songHash
) {}
