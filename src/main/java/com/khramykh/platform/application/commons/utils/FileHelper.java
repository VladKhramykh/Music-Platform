package com.khramykh.platform.application.commons.utils;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileHelper {

    @Value("${upload.path}")
    private String uploadPath;

    public String getNewUri(MultipartFile file, FileOperations type) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String pathToUpdate = uploadPath;
            switch (type) {
                case TRACK_FILE:
                    pathToUpdate += "/tracks/";
                    break;
                case TRACK_PHOTO:
                    pathToUpdate += "/images/tracks/";
                    break;
                case USER_PHOTO:
                    pathToUpdate += "/images/users/";
                    break;
                case ARTIST_PHOTO:
                    pathToUpdate += "/images/artists/";
                    break;
                case ALBUM_PHOTO:
                    pathToUpdate += "/images/albums/";
                    break;
            }
            File uploadDir = new File(pathToUpdate);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename().trim();

            file.transferTo(new File(uploadDir.getPath() + "/" + resultFilename));

            return resultFilename;
        } else {
            return Strings.EMPTY;
        }
    }

    public void deleteFile(String fileUri, FileOperations type) throws IOException {
        String pathToDelete = uploadPath;
        switch (type) {
            case TRACK_FILE:
                pathToDelete += "/tracks/";
                break;
            case TRACK_PHOTO:
                pathToDelete += "/images/tracks/";
                break;
            case USER_PHOTO:
                pathToDelete += "/images/users/";
                break;
            case ARTIST_PHOTO:
                pathToDelete += "/images/artists/";
                break;
            case ALBUM_PHOTO:
                pathToDelete += "/images/albums/";
                break;
        }
        File uploadDir = new File(pathToDelete + "/" + fileUri);

        if (uploadDir.exists()) {
            uploadDir.delete();
        }
    }
}
