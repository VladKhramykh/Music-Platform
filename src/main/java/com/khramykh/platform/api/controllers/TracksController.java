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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/favourite")
    public ResponseEntity getAll(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam TrackSort trackSort) {
        Page<Track> trackPage = tracksService.getFavouriteTracksByUser(userDetails.getUsername(), pageNum, pageSize, trackSort);
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
        Page trackPage = tracksService.getTracksByArtist(artistId, pageNum, pageSize, trackSort);
        return ResponseEntity.ok().body(trackPage);
    }

    @GetMapping("/album")
    public ResponseEntity getTracksByAlbumId(@RequestParam int albumId, TrackSort trackSort) {
        List trackPage = tracksService.getTracksByAlbum(albumId, trackSort);
        return ResponseEntity.ok().body(trackPage);
    }

    @GetMapping("/last")
    public ResponseEntity getLastReleases(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam TrackSort trackSort) {
        Page trackPage = tracksService.getLastReleases(pageNum, pageSize, trackSort);
        return ResponseEntity.ok().body(trackPage);
    }

    @GetMapping("/types")
    public ResponseEntity getTrackTypes() {
        return ResponseEntity.ok().body(TrackTypes.values());
    }

    @PostMapping("/photo")
    public ResponseEntity setPhoto(@RequestParam int id, @RequestParam(name = "file") MultipartFile file) throws IOException {
        String photoUrl = tracksService.updatePhoto(id, file);
        return ResponseEntity.ok().body(photoUrl);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable int id) {
        tracksService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity update(
            @RequestParam(name = "id") int id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "type") String type,
            @RequestParam(name = "album") Optional<Integer> album,
            @RequestParam(name = "trackText") String trackText,
            @RequestParam(name = "categories") int[] categories,
            @RequestParam(name = "releaseDate") String releaseDate,
            @RequestParam(name = "artists") int[] artists,
            @RequestParam(name = "photoFile", required = false) MultipartFile photoFile,
            @RequestParam(name = "trackFile", required = false) MultipartFile trackFile
    ) throws IOException, ParseException {
        TrackUpdateCommand command = new TrackUpdateCommand();
        command.setId(id);
        command.setName(name);
        command.setDescription(description);
        command.setType(type);
        album.ifPresent(command::setAlbum);
        command.setTrackText(trackText);
        command.setCategories(categories);
        command.setReleaseDate(releaseDate);
        command.setArtists(artists);
        command.setPhotoFile(photoFile);
        command.setTrackFile(trackFile);
        Track updated = tracksService.update(command);
        return ResponseEntity.ok().body(updated);
    }

    @PostMapping(produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity create(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "type") String type,
            @RequestParam(name = "album", required = false) Optional<Integer> album,
            @RequestParam(name = "trackText") String trackText,
            @RequestParam(name = "categories") int[] categories,
            @RequestParam(name = "releaseDate") String releaseDate,
            @RequestParam(name = "artists") int[] artists,
            @RequestParam(name = "photoFile", required = false) MultipartFile photoFile,
            @RequestParam(name = "trackFile", required = false) MultipartFile trackFile
    ) throws IOException, ParseException {
        TrackCreateCommand command = new TrackCreateCommand();
        command.setName(name);
        command.setDescription(description);
        command.setType(type);
        album.ifPresent(command::setAlbum);
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
