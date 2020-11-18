package com.khramykh.platform.application.artistsApi.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistUpdateCommand {
    private int id;
    private String name;
    private String description;
}
