package com.khramykh.platform.application.tracksApi;

import com.khramykh.platform.application.commons.sort.TrackSort;
import com.khramykh.platform.application.exceptions.ResourceNotFoundException;
import com.khramykh.platform.application.repositories.*;
import com.khramykh.platform.application.tracksApi.commands.TrackCreateCommand;
import com.khramykh.platform.application.tracksApi.commands.TrackUpdateCommand;
import com.khramykh.platform.domain.commons.enums.TrackTypes;
import com.khramykh.platform.domain.entities.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TracksService {
    @Autowired
    TracksRepository tracksRepository;
    @Autowired
    AlbumsRepository albumsRepository;
    @Autowired
    ArtistsRepository artistsRepository;
    @Autowired
    CategoriesRepository categoryRepository;
    @Autowired
    UsersRepository usersRepository;

    public Track getTrackById(int id) {
        return tracksRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Page<Track> getTrackByName(String name, int pageNum, int pageSize, TrackSort trackSort) {
        return tracksRepository.findByNameContaining(name, PageRequest.of(pageNum, pageSize, getSortType(trackSort)));
    }

    public Page<Track> getTracksByPage(int pageNum, int pageSize, TrackSort trackSort) {
        return tracksRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(trackSort)));
    }

    // TODO need to realize finding top-10 categories
    public Page<Track> getMostPopularTracksByPage(int pageNum, int pageSize, TrackSort trackSort) {
        return tracksRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(trackSort)));
    }

    public void removeById(int id) {
        if (!tracksRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        tracksRepository.deleteById(id);
    }

    public Track update(TrackUpdateCommand command) {
        Track oldTrack = tracksRepository.findById(command.getId()).orElseThrow(() -> new ResourceNotFoundException((command.getId())));
        Track updated = tracksRepository.save(convertTrackUpdateCommandToTrack(oldTrack, command));
        return updated;
    }

    public Track create(TrackCreateCommand command) {
        Track track = new Track();
        track.setName(command.getName());
        track.setDescription(command.getDescription());
        if (command.getAlbum() != null) {
            albumsRepository.findById(command.getAlbum().getId()).map(item -> {
                track.setAlbum(item);
                return true;
            });
        }

        command.getArtists().forEach(item -> {
            track.getArtists().add(artistsRepository.getOne(item.getId()));
        });
        command.getCategories().forEach(item -> {
            track.getCategories().add(categoryRepository.getOne(item.getId()));
        });

        track.setPhotoUri(command.getPhotoUri());
        track.setTrackText(command.getTrackText());
        track.setType(TrackTypes.valueOf(command.getType()));
        tracksRepository.save(track);
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

    public void like(int trackId, int userId) {
        tracksRepository.findById(trackId).map(track -> {
            usersRepository.findById(userId).map(user -> track.getLikes().add(user));
            return tracksRepository.save(track);
        });
    }

    public void dislike(int trackId, int userId) {
        tracksRepository.findById(trackId).map(track -> {
            usersRepository.findById(userId).map(user -> track.getLikes().remove(user));
            return tracksRepository.save(track);
        });
    }

    private Track convertTrackUpdateCommandToTrack(Track oldTrack, TrackUpdateCommand command) {
        oldTrack.setName(command.getName());
        oldTrack.setDescription(command.getDescription());
        if (command.getAlbum() != null)
            oldTrack.setAlbum(command.getAlbum());
        oldTrack.setArtists(command.getArtists());
        oldTrack.setCategories(command.getCategories());
        oldTrack.setPhotoUri(command.getPhotoUri());
        oldTrack.setTrackText(command.getTrackText());
        oldTrack.setType(TrackTypes.valueOf(command.getType()));
        return oldTrack;
    }
}
