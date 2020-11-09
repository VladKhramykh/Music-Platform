package com.khramykh.platform.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;
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
    @ManyToOne
    private Set<Category> categories;
    @OneToOne
    private Album album;
    @ManyToOne
    private Set<Artist> artists;
}
