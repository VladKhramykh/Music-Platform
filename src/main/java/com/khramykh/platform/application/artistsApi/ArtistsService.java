package com.khramykh.platform.application.artistsApi;

import com.khramykh.platform.application.artistsApi.commands.ArtistCreateCommand;
import com.khramykh.platform.application.artistsApi.commands.ArtistUpdateCommand;
import com.khramykh.platform.application.commons.sort.ArtistSort;
import com.khramykh.platform.application.exceptions.ResourceNotFoundException;
import com.khramykh.platform.application.repositories.ArtistsRepository;
import com.khramykh.platform.domain.entities.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ArtistsService {
    @Autowired
    ArtistsRepository artistsRepository;

    public Artist getArtistById(int id) {
        return artistsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Page<Artist> getArtistByName(String name, int pageNum, int pageSize, ArtistSort artistSort) {
        return artistsRepository.findByNameContaining(name, PageRequest.of(pageNum, pageSize, getSortType(artistSort)));
    }

    public Page<Artist> getArtistsByPage(int pageNum, int pageSize, ArtistSort artistSort) {
        return artistsRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(artistSort)));
    }

    // TODO need to realize finding top-10 artists
    public Page<Artist> getMostPopularArtistsByPage(int pageNum, int pageSize, ArtistSort artistSort) {
        return artistsRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(artistSort)));
    }

    public void removeById(int id) {
        if (!artistsRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        artistsRepository.deleteById(id);
    }

    public Artist update(ArtistUpdateCommand command) {
        Artist oldArtist = artistsRepository.findById(command.getId()).orElseThrow(() -> new ResourceNotFoundException((command.getId())));
        Artist updated = artistsRepository.save(convertArtistUpdateCommandToArtist(oldArtist, command));
        return updated;
    }

    public Artist create(ArtistCreateCommand command) {
        Artist artist = new Artist();
        artist.setName(command.getName());
        artist.setDescription(command.getDescription());
        artistsRepository.save(artist);
        return artist;
    }

    private Sort getSortType(ArtistSort artistSort) {
        switch (artistSort) {
            case NAME_DESC:
                return Sort.by("name").descending();
            case ID_ASC:
                return Sort.by("id").ascending();
            case ID_DESC:
                return Sort.by("id").descending();
            default:
                return Sort.by("name").ascending();
        }
    }

    private Artist convertArtistUpdateCommandToArtist(Artist oldArtist, ArtistUpdateCommand command) {
        oldArtist.setName(command.getName());
        oldArtist.setDescription(command.getDescription());
        return oldArtist;
    }
}
