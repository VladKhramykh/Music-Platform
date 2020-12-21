package com.khramykh.platform.application.artistsApi.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistCreateCommand {
    private String name;
    private String description;
    private String createdDate;
    private MultipartFile photoFile;
}
