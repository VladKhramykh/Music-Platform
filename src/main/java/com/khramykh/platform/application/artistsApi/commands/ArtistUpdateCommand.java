package com.khramykh.platform.application.artistsApi.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistUpdateCommand {
    private int id;
    private String name;
    private String description;
    private boolean deleted;
    private String createdDate;
    private MultipartFile photoFile;
}
