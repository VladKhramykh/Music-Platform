package com.khramykh.platform.domain.entities;

import com.khramykh.platform.domain.commons.enums.TrackTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tracks")
public class Track extends BaseEntity{
    private String name;
    @Enumerated(EnumType.STRING)
    private TrackTypes type;
    private String description;
    private String photoUri;
    private String trackText;
    private boolean published;
    @CreatedDate
    private Date releaseDate;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @CollectionTable(name = "category_track", joinColumns = @JoinColumn(name = "track_id"))
    private Set<Category> categories = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;
    @ManyToMany(fetch = FetchType.LAZY)
    @CollectionTable(name = "atrist_track", joinColumns = @JoinColumn(name = "track_id"))
    @JoinColumn(name = "artist_id", nullable = false)
    private Set<Artist> artists = new HashSet<>();
}
