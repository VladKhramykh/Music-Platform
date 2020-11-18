package com.khramykh.platform.api.controllers;

import com.khramykh.platform.application.artistsApi.ArtistsService;
import com.khramykh.platform.application.artistsApi.commands.ArtistCreateCommand;
import com.khramykh.platform.application.artistsApi.commands.ArtistUpdateCommand;
import com.khramykh.platform.application.commons.sort.ArtistSort;
import com.khramykh.platform.domain.entities.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("api/artists")
public class ArtistsController {
    @Autowired
    ArtistsService artistsService;

    @GetMapping
    public ResponseEntity getAll(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam ArtistSort artistSort) {
        Page<Artist> artistsPage = artistsService.getArtistsByPage(pageNum, pageSize, artistSort);
        return ResponseEntity.ok().body(artistsPage);
    }

    @GetMapping("{id}")
    public ResponseEntity getOneById(@PathVariable int id) {
        Artist artist = artistsService.getArtistById(id);
        return ResponseEntity.ok().body(artist);
    }

    @GetMapping
    public ResponseEntity getAllByName(@RequestParam String name, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam ArtistSort artistSort) {
        Page artistsPage = artistsService.getArtistByName(name, pageNum, pageSize, artistSort);
        return ResponseEntity.ok().body(artistsPage);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody ArtistUpdateCommand command) {
        Artist updated = artistsService.update(command);
        return ResponseEntity.ok().body(updated);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody ArtistCreateCommand command) {
        Artist created = artistsService.create(command);
        return ResponseEntity.ok().body(created);
    }
}
