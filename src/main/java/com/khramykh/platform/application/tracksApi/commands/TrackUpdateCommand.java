package com.khramykh.platform.application.tracksApi.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackUpdateCommand {
    private int id;
    private String name;
    private String type;
    private String description;
    private String releaseDate;
    private MultipartFile photoFile;
    private MultipartFile trackFile;
    private int[] categories;
    private int album;
    private int[] artists;
}
