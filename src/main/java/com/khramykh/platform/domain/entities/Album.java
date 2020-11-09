package com.khramykh.platform.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    private int id;
    private String name;
    private String description;
    private String type;
    private String photoUri;
    private Date releaseDate;
    @ManyToOne
    private Artist artist;
}
