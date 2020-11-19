package com.khramykh.platform.domain.entities;

import com.khramykh.platform.domain.commons.enums.AlbumTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "albums")
public class Album extends BaseEntity{
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private AlbumTypes type;
    private String photoUri;
    @CreatedDate
    private Date releaseDate;
    @CreatedBy
    @ManyToMany(fetch = FetchType.LAZY)
    @CollectionTable(name = "artist_album", joinColumns = @JoinColumn(name = "album_id"))
    @JoinColumn(name = "artist_id", nullable = false)
    private Set<Artist> artists = new HashSet<>();
}
