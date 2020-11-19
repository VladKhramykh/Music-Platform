package com.khramykh.platform.application.tracksApi;

import com.khramykh.platform.application.commons.sort.TrackSort;
import com.khramykh.platform.application.exceptions.ResourceNotFoundException;
import com.khramykh.platform.application.repositories.TrackRepository;
import com.khramykh.platform.application.tracksApi.commands.TrackCreateCommand;
import com.khramykh.platform.application.tracksApi.commands.TrackUpdateCommand;
import com.khramykh.platform.domain.entities.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TracksService {
    @Autowired
    TrackRepository trackRepository;

    public Track getTrackById(int id) {
        return trackRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Page<Track> getTrackByName(String name, int pageNum, int pageSize, TrackSort trackSort) {
        return trackRepository.findByNameContaining(name, PageRequest.of(pageNum, pageSize, getSortType(trackSort)));
    }

    public Page<Track> getTracksByPage(int pageNum, int pageSize, TrackSort trackSort) {
        return trackRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(trackSort)));
    }

    // TODO need to realize finding top-10 categories
    public Page<Track> getMostPopularTracksByPage(int pageNum, int pageSize, TrackSort trackSort) {
        return trackRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(trackSort)));
    }

    public void removeById(int id) {
        if (!trackRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        trackRepository.deleteById(id);
    }

    public Track update(TrackUpdateCommand command) {
        Track oldTrack = trackRepository.findById(command.getId()).orElseThrow(() -> new ResourceNotFoundException((command.getId())));
        Track updated = trackRepository.save(convertTrackUpdateCommandToTrack(oldTrack, command));
        return updated;
    }

    public Track create(TrackCreateCommand command) {
        Track track = new Track();
        track.setName(command.getName());
        track.setDescription(command.getDescription());
        trackRepository.save(track);
        return track;
    }

    private Sort getSortType(TrackSort trackSort) {
        switch (trackSort) {
            case NAME_DESC:
                return Sort.by("name").descending();
            case ID_DESC:
                return Sort.by("id").descending();
            case ID_ASC:
                return Sort.by("id").ascending();
            case RELEASE_DATE_DESC:
                return Sort.by("releaseDate").descending();
            case RELEASE_DATE_ASC:
                return Sort.by("releaseDate").ascending();
            default:
                return Sort.by("name").ascending();
        }
    }

    private Track convertTrackUpdateCommandToTrack(Track oldTrack, TrackUpdateCommand command) {
        oldTrack.setName(command.getName());
        oldTrack.setDescription(command.getDescription());
        return oldTrack;
    }
}
