package com.khramykh.platform.api.controllers;

import com.khramykh.platform.application.albumsApi.AlbumsService;
import com.khramykh.platform.application.albumsApi.commands.AlbumCreateCommand;
import com.khramykh.platform.application.albumsApi.commands.AlbumUpdateCommand;
import com.khramykh.platform.application.commons.sort.AlbumSort;
import com.khramykh.platform.domain.commons.enums.AlbumTypes;
import com.khramykh.platform.domain.entities.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/albums")
public class AlbumsController {
    @Autowired
    AlbumsService albumsService;

    @GetMapping
    public ResponseEntity getPage(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) String filter,
            @RequestParam AlbumSort albumSort) {
        Page<Album> albumsPage;
        if(filter != null){
            albumsPage = albumsService.getAlbumByNameContaining(filter, pageNum, pageSize, albumSort);
        } else {
            albumsPage = albumsService.getAlbumsByPage(pageNum, pageSize, albumSort);
        }

        return ResponseEntity.ok().body(albumsPage);
    }

    @GetMapping("/types")
    public ResponseEntity getTypes() {
        return ResponseEntity.ok().body(AlbumTypes.values());
    }

    @GetMapping("/artist")
    public ResponseEntity getAlbumsByArtistId(@RequestParam int artistId, @RequestParam AlbumSort albumSort) {
        List<Album> albumsList = albumsService.getAlbumsByArtist(artistId, albumSort);
        return ResponseEntity.ok().body(albumsList);
    }

    @GetMapping("{id}")
    public ResponseEntity getOneById(@PathVariable int id) {
        Album albums = albumsService.getAlbumById(id);
        return ResponseEntity.ok().body(albums);
    }

    @GetMapping("/favourite")
    public ResponseEntity getFavouriteAlbums(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam AlbumSort albumSort) {
        Page<Album> albumPage = albumsService.getFavouriteAlbumsByUser(userDetails.getUsername(), pageNum, pageSize, albumSort);
        return ResponseEntity.ok().body(albumPage);
    }

    @GetMapping("/like")
    public ResponseEntity like(@RequestParam int albumId, @RequestParam int userId) {
        albumsService.like(albumId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dislike")
    public ResponseEntity dislike(@RequestParam int albumId, @RequestParam int userId) {
        albumsService.dislike(albumId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/last")
    public ResponseEntity getLastReleases(@RequestParam(required = false) Integer artistId, @RequestParam int pageNum, @RequestParam int pageSize) {
        Page<Album> page;
        if (artistId != null) {
            page = albumsService.getLastReleasesByArtist(artistId, pageNum, pageSize);
        } else {
            page = albumsService.getLastReleases(pageNum, pageSize);
        }
        return ResponseEntity.ok().body(page);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable int id) {
        albumsService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity update(
            @RequestParam(name = "id") int id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "type") String type,
            @RequestParam(name = "releaseDate") String releaseDate,
            @RequestParam(name = "artists") int[] artists,
            @RequestParam(name = "photoFile", required = false) MultipartFile photoFile
    ) throws ParseException, IOException {
        AlbumUpdateCommand command = new AlbumUpdateCommand();
        command.setId(id);
        command.setName(name);
        command.setDescription(description);
        command.setType(type);
        command.setReleaseDate(releaseDate);
        command.setArtists(artists);
        command.setPhotoFile(photoFile);
        Album updated = albumsService.update(command);
        return ResponseEntity.ok().body(updated);
    }

    @PostMapping
    public ResponseEntity create(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "type") String type,
            @RequestParam(name = "releaseDate") String releaseDate,
            @RequestParam(name = "artists") int[] artists,
            @RequestParam(name = "photoFile", required = false) MultipartFile photoFile
    ) throws ParseException, IOException {
        AlbumCreateCommand command = new AlbumCreateCommand();
        command.setName(name);
        command.setDescription(description);
        command.setType(type);
        command.setReleaseDate(releaseDate);
        command.setArtists(artists);
        command.setPhotoFile(photoFile);
        Album created = albumsService.create(command);
        return ResponseEntity.ok().body(created);
    }
}
