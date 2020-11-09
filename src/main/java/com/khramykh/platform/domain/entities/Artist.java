package com.khramykh.platform.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
    private int id;
    private String name;
    private String description;
    @OneToOne
    private User user;
}
