package com.khramykh.platform.api.controllers;

import com.khramykh.platform.application.commons.sort.TrackSort;
import com.khramykh.platform.application.tracksApi.TracksService;
import com.khramykh.platform.application.tracksApi.commands.TrackCreateCommand;
import com.khramykh.platform.application.tracksApi.commands.TrackUpdateCommand;
import com.khramykh.platform.domain.commons.enums.TrackTypes;
import com.khramykh.platform.domain.entities.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/tracks")
public class TracksController {
    @Autowired
    TracksService tracksService;

    @GetMapping
    public ResponseEntity getAll(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam TrackSort trackSort) {
        Page<Track> trackPage = tracksService.getTracksByPage(pageNum, pageSize, trackSort);
        return ResponseEntity.ok().body(trackPage);
    }

    @GetMapping("{id}")
    public ResponseEntity getOneById(@PathVariable int id) {
        Track trackPage = tracksService.getTrackById(id);
        return ResponseEntity.ok().body(trackPage);
    }

    @GetMapping("/like")
    public ResponseEntity like(@RequestParam int trackId, @RequestParam int userId) {
        tracksService.like(trackId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dislike")
    public ResponseEntity dislike(@RequestParam int trackId, @RequestParam int userId) {
        tracksService.dislike(trackId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/artist")
    public ResponseEntity getTracksByArtistId(@RequestParam Integer artistId, @RequestParam int pageNum, @RequestParam int pageSize, TrackSort trackSort) {
        Page trackPage = tracksService.getTrackByArtist(artistId, pageNum, pageSize, trackSort);
        return ResponseEntity.ok().body(trackPage);
    }

    @GetMapping("/album")
    public ResponseEntity getTracksByAlbumId(@RequestParam int albumId, TrackSort trackSort) {
        List trackPage = tracksService.getTracksByAlbum(albumId, trackSort);
        return ResponseEntity.ok().body(trackPage);
    }

    @GetMapping("/types")
    public ResponseEntity getTrackTypes() {
        return ResponseEntity.ok().body(TrackTypes.values());
    }

//    @GetMapping
//    public ResponseEntity getOneByName(@RequestParam String name, @RequestParam int pageNum, @RequestParam int pageSize) {
//        Page trackPage = tracksService.getTrackByName(name, pageNum, pageSize);
//        return ResponseEntity.ok().body(trackPage);
//    }

    @PostMapping("/photo")
    public ResponseEntity setPhoto(@RequestParam int id, @RequestParam(name = "file") MultipartFile file) throws IOException {
        String photoUrl = tracksService.updatePhoto(id, file);
        return ResponseEntity.ok().body(photoUrl);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable int id) {
        tracksService.removeById(id);
        return (ResponseEntity) ResponseEntity.noContent();
    }

    @PutMapping
    public ResponseEntity update(@RequestBody TrackUpdateCommand command) throws IOException {
        Track updated = tracksService.update(command);
        return ResponseEntity.ok().body(updated);
    }

//    @PostMapping(produces = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity create(@RequestBody TrackCreateCommand command) throws IOException, ParseException {
//        Track created = tracksService.create(command);
//        return ResponseEntity.ok().body(created);
//    }

    @PostMapping(produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity create(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "type") String type,
            @RequestParam(name = "album") int album,
            @RequestParam(name = "trackText") String trackText,
            @RequestParam(name = "categories") int[] categories,
            @RequestParam(name = "releaseDate") String releaseDate,
            @RequestParam(name = "artists") int[] artists,
            @RequestParam(name = "photoFile") MultipartFile photoFile,
            @RequestParam(name = "trackFile") MultipartFile trackFile
    ) throws IOException, ParseException {
        TrackCreateCommand command = new TrackCreateCommand();
        command.setName(name);
        command.setDescription(description);
        command.setType(type);
        command.setAlbum(album);
        command.setTrackText(trackText);
        command.setCategories(categories);
        command.setReleaseDate(releaseDate);
        command.setArtists(artists);
        command.setPhotoFile(photoFile);
        command.setTrackFile(trackFile);
        Track created = tracksService.create(command);
        return ResponseEntity.ok().body(created);
    }
}
