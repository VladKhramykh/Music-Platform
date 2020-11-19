package com.khramykh.platform.api.controllers;

import com.khramykh.platform.application.albumsApi.AlbumsService;
import com.khramykh.platform.application.albumsApi.commands.AlbumCreateCommand;
import com.khramykh.platform.application.albumsApi.commands.AlbumUpdateCommand;
import com.khramykh.platform.application.commons.sort.AlbumSort;
import com.khramykh.platform.domain.entities.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/albums")
public class AlbumsController {
    @Autowired
    AlbumsService albumsService;

    @GetMapping
    public ResponseEntity getPage(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam AlbumSort albumSort) {
        Page<Album> albumsPage = albumsService.getAlbumsByPage(pageNum, pageSize, albumSort);
        return ResponseEntity.ok().body(albumsPage);
    }

    @GetMapping("{id}")
    public ResponseEntity getOneById(@PathVariable int id) {
        Album albums = albumsService.getAlbumById(id);
        return ResponseEntity.ok().body(albums);
    }

//    @GetMapping
//    public ResponseEntity getPageByName(@RequestParam String name, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam AlbumSort albumSort) {
//        Page albumPage = albumsService.getAlbumByNameContaining(name, pageNum, pageSize, albumSort);
//        return ResponseEntity.ok().body(albumPage);
//    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable int id) {
        albumsService.removeById(id);
        return (ResponseEntity) ResponseEntity.noContent();
    }

    @PutMapping
    public ResponseEntity update(@RequestBody AlbumUpdateCommand command) {
        Album updated = albumsService.update(command);
        return ResponseEntity.ok().body(updated);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody AlbumCreateCommand command) {
        Album created = albumsService.create(command);
        return ResponseEntity.ok().body(created);
    }
}
