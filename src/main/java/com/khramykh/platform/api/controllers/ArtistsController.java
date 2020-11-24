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

@RestController
@RequestMapping(value = "/api/artists")
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

//    @GetMapping
//    public ResponseEntity getAllByName(@RequestParam String name, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam ArtistSort artistSort) {
//        Page artistsPage = artistsService.getArtistByName(name, pageNum, pageSize, artistSort);
//        return ResponseEntity.ok().body(artistsPage);
//    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable int id) {
        artistsService.removeById(id);
        return (ResponseEntity) ResponseEntity.noContent();
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
