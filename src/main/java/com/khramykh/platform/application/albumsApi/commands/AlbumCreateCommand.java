package com.khramykh.platform.application.albumsApi.commands;

import com.khramykh.platform.domain.commons.enums.AlbumTypes;
import com.khramykh.platform.domain.entities.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumCreateCommand {
    private String name;
    private String description;
    private AlbumTypes type;
    private String photoUri;
    private Date releaseDate;
    private Set<Artist> artists = new HashSet<>();
}
