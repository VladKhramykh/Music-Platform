package com.khramykh.platform.api.controllers;

import com.khramykh.platform.application.artistsApi.ArtistsService;
import com.khramykh.platform.application.artistsApi.commands.ArtistCreateCommand;
import com.khramykh.platform.application.artistsApi.commands.ArtistUpdateCommand;
import com.khramykh.platform.application.commons.sort.ArtistSort;
import com.khramykh.platform.domain.entities.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/artists")
public class ArtistsController {
    @Autowired
    ArtistsService artistsService;
    
    @GetMapping
    public ResponseEntity getAll(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) String filter,
            @RequestParam ArtistSort artistSort) {
        Page<Artist> artistsPage;
        if (filter != null) {
            artistsPage = artistsService.getArtistByName(filter, pageNum, pageSize, artistSort);
        } else {
            artistsPage = artistsService.getArtistsByPage(pageNum, pageSize, artistSort);
        }
        return ResponseEntity.ok().body(artistsPage);
    }

    @GetMapping("{id}")
    public ResponseEntity getOneById(@PathVariable int id) {
        Artist artist = artistsService.getArtistById(id);
        return ResponseEntity.ok().body(artist);
    }

    @GetMapping("/like")
    public ResponseEntity like(@RequestParam int trackId, @RequestParam int userId) {
        artistsService.like(trackId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dislike")
    public ResponseEntity dislike(@RequestParam int trackId, @RequestParam int userId) {
        artistsService.dislike(trackId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity delete(@PathVariable int id) {
        artistsService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity update(
            @RequestParam(name = "id") int id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "createdDate") String createdDate,
            @RequestParam(name = "photoFile", required = false) MultipartFile photoFile
    ) throws ParseException, IOException {
        ArtistUpdateCommand command = new ArtistUpdateCommand();
        command.setId(id);
        command.setName(name);
        command.setDescription(description);
        command.setCreatedDate(createdDate);
        command.setPhotoFile(photoFile);
        Artist updated = artistsService.update(command);
        return ResponseEntity.ok().body(updated);
    }

    @PostMapping(produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity create(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "createdDate") String createdDate,
            @RequestParam(name = "photoFile", required = false) MultipartFile photoFile
    ) throws ParseException, IOException {
        ArtistCreateCommand command = new ArtistCreateCommand();
        command.setName(name);
        command.setDescription(description);
        command.setCreatedDate(createdDate);
        command.setPhotoFile(photoFile);
        Artist created = artistsService.create(command);
        return ResponseEntity.ok().body(created);
    }
}
