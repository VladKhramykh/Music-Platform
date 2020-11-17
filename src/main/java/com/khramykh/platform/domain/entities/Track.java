package com.khramykh.platform.domain.entities;

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
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    private int id;
    private String name;
    private String type;
    private String description;
    private String photoUri;
    private String trackText;
    private boolean published;
    private Date releaseDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Set<Category> categories = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "album_id", nullable = true)
    private Album album;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private Set<Artist> artists = new HashSet<>();
}
