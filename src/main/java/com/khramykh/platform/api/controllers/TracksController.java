package com.khramykh.platform.api.controllers;

import com.khramykh.platform.application.commons.sort.TrackSort;
import com.khramykh.platform.application.tracksApi.TracksService;
import com.khramykh.platform.application.tracksApi.commands.TrackCreateCommand;
import com.khramykh.platform.application.tracksApi.commands.TrackUpdateCommand;
import com.khramykh.platform.domain.entities.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping
//    public ResponseEntity getOneByName(@RequestParam String name, @RequestParam int pageNum, @RequestParam int pageSize) {
//        Page trackPage = tracksService.getTrackByName(name, pageNum, pageSize);
//        return ResponseEntity.ok().body(trackPage);
//    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable int id) {
        tracksService.removeById(id);
        return (ResponseEntity) ResponseEntity.noContent();
    }

    @PutMapping
    public ResponseEntity update(@RequestBody TrackUpdateCommand command) {
        Track updated = tracksService.update(command);
        return ResponseEntity.ok().body(updated);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TrackCreateCommand command) {
        Track created = tracksService.create(command);
        return ResponseEntity.ok().body(created);
    }
}
