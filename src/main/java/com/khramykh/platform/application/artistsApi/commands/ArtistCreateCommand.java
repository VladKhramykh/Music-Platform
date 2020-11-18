package com.khramykh.platform.application.artistsApi.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistCreateCommand {
    private String name;
    private String description;
}
