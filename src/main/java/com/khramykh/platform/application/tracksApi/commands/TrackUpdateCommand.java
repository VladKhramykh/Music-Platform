package com.khramykh.platform.application.tracksApi.commands;

import com.khramykh.platform.domain.commons.enums.TrackTypes;
import com.khramykh.platform.domain.entities.Album;
import com.khramykh.platform.domain.entities.Artist;
import com.khramykh.platform.domain.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackUpdateCommand {
    private int id;
    private String name;
    private TrackTypes type;
    private String description;
    private String photoUri;
    private String trackText;
    private boolean published;
    private Set<Category> categories = new HashSet<>();
    private Album album;
    private Set<Artist> artists = new HashSet<>();
}
