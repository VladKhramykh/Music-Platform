package com.khramykh.platform.application.artistsApi;

import com.khramykh.platform.application.artistsApi.commands.ArtistCreateCommand;
import com.khramykh.platform.application.artistsApi.commands.ArtistUpdateCommand;
import com.khramykh.platform.application.commons.sort.ArtistSort;
import com.khramykh.platform.application.commons.utils.FileHelper;
import com.khramykh.platform.application.commons.utils.FileOperations;
import com.khramykh.platform.application.exceptions.ResourceNotFoundException;
import com.khramykh.platform.application.repositories.ArtistsRepository;
import com.khramykh.platform.application.repositories.UsersRepository;
import com.khramykh.platform.domain.entities.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ArtistsService {
    @Autowired
    ArtistsRepository artistsRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    FileHelper fileHelper;

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

    public Artist update(ArtistUpdateCommand command) throws ParseException, IOException {
        Artist oldArtist = artistsRepository.findById(command.getId()).orElseThrow(() -> new ResourceNotFoundException((command.getId())));
        Artist updated = artistsRepository.save(convertArtistUpdateCommandToArtist(oldArtist, command));
        return updated;
    }

    public Artist create(ArtistCreateCommand command) throws ParseException, IOException {
        Artist artist = new Artist();
        artist.setName(command.getName());
        artist.setDescription(command.getDescription());
        if (command.getPhotoFile() != null) {
            artist.setPhotoUri(fileHelper.getNewUri(command.getPhotoFile(), FileOperations.ARTIST_PHOTO));
        }
        artist.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse(command.getCreatedDate()));
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

    private Artist convertArtistUpdateCommandToArtist(Artist oldArtist, ArtistUpdateCommand command) throws ParseException, IOException {
        oldArtist.setName(command.getName());
        oldArtist.setDescription(command.getDescription());
        oldArtist.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse(command.getCreatedDate()));
        oldArtist.setDeleted(command.isDeleted());
        if (command.getPhotoFile() != null) {
            fileHelper.deleteFile(oldArtist.getPhotoUri(), FileOperations.ARTIST_PHOTO);
            oldArtist.setPhotoUri(fileHelper.getNewUri(command.getPhotoFile(), FileOperations.ARTIST_PHOTO));
        }
        return oldArtist;
    }

    public void like(int artistId, int userId) {
        artistsRepository.findById(artistId).map(artist -> {
            usersRepository.findById(userId).map(user -> artist.getLikes().add(user));
            return artistsRepository.save(artist);
        });
    }

    public void dislike(int artistId, int userId) {
        artistsRepository.findById(artistId).map(artist -> {
            usersRepository.findById(userId).map(user -> artist.getLikes().remove(user));
            return artistsRepository.save(artist);
        });
    }
}
