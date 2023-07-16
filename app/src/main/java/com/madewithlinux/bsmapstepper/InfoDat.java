package com.madewithlinux.bsmapstepper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record InfoDat(
    @JsonProperty("_version") String version,
    @JsonProperty("_songName") String songName,
    @JsonProperty("_songSubName") String songSubName,
    @JsonProperty("_songAuthorName") String songAuthorName,
    @JsonProperty("_levelAuthorName") String levelAuthorName,
    @JsonProperty("_beatsPerMinute") Double beatsPerMinute,
    // @JsonProperty("_shuffle") String shuffle,
    // @JsonProperty("_shufflePeriod") String shufflePeriod,
    // @JsonProperty("_previewStartTime") String previewStartTime,
    // @JsonProperty("_previewDuration") String previewDuration,
    @JsonProperty("_songFilename") String songFilename,
    @JsonProperty("_coverImageFilename") String coverImageFilename,
    // @JsonProperty("_environmentName") String environmentName,
    @JsonProperty("_songTimeOffset") Double songTimeOffset
) {}
