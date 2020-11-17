package com.khramykh.platform.domain.entities;

import com.khramykh.platform.domain.commons.enums.AlbumTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    private int id;
    private String name;
    private String description;
    private AlbumTypes type;
    private String photoUri;
    private Date releaseDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private Set<Artist> artists = new HashSet<>();
}
