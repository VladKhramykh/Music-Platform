package com.khramykh.platform.application.albumsApi.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumUpdateCommand {
    private int id;
    private String name;
    private String description;
    private String type;
    private MultipartFile file;
    private String releaseDate;
    private int[] artists;
}
