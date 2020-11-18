package com.khramykh.platform.application.albumsApi;

import com.khramykh.platform.application.albumsApi.commands.AlbumCreateCommand;
import com.khramykh.platform.application.albumsApi.commands.AlbumUpdateCommand;
import com.khramykh.platform.application.commons.sort.AlbumSort;
import com.khramykh.platform.application.exceptions.ResourceNotFoundException;
import com.khramykh.platform.application.repositories.AlbumsRepository;
import com.khramykh.platform.domain.entities.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

// TODO !!! IMPORTANT !!! finish this Service, controller, repo
@Service
public class AlbumsService {
    @Autowired
    AlbumsRepository albumsRepository;

    public Album getAlbumById(int id) {
        return albumsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Page<Album> getAlbumByNameContaining(String name, int pageNum, int pageSize, AlbumSort albumSort) {
        return albumsRepository.findAlbumByNameContaining(name, PageRequest.of(pageNum, pageSize), getSortType(albumSort));
    }

    public Page<Album> getAlbumsByPage(int pageNum, int pageSize, AlbumSort albumSort) {
        return albumsRepository.findAll(PageRequest.of(pageNum, pageSize), getSortType(albumSort));
    }

    // TODO need to realize finding top-10 albums
    public Page<Album> getMostPopularAlbumsByPage(int pageNum, int pageSize, AlbumSort albumSort) {
        return albumsRepository.findAll(PageRequest.of(pageNum, pageSize), getSortType(albumSort));
    }

    public void removeById(int id) {
        if (!albumsRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        albumsRepository.deleteById(id);
    }

    public Album update(AlbumUpdateCommand command) {
        Album oldAlbum = albumsRepository.findById(command.getId()).orElseThrow(() -> new ResourceNotFoundException((command.getId())));
        Album updated = albumsRepository.save(convertAlbumUpdateCommandToAlbum(oldAlbum, command));
        return updated;
    }

    private Album convertAlbumUpdateCommandToAlbum(Album oldAlbum, AlbumUpdateCommand command) {
        oldAlbum.setName(command.getName());
        oldAlbum.setType(command.getType());
        oldAlbum.setArtists(command.getArtists());
        oldAlbum.setPhotoUri(command.getPhotoUri());
        oldAlbum.setReleaseDate(command.getReleaseDate());
        oldAlbum.setDescription(command.getDescription());
        return oldAlbum;
    }

    private Sort getSortType(AlbumSort albumSort) {
        switch (albumSort) {
            case NAME_DESC:
                return Sort.by("name").descending();
            case RELEASE_DATE_ASC:
                return Sort.by("releaseDate").ascending();
            case RELEASE_DATE_DESC:
                return Sort.by("releaseDate").descending();
            case ID_ASC:
                return Sort.by("id").ascending();
            case ID_DESC:
                return Sort.by("id").descending();
            default:
                return Sort.by("name").ascending();
        }
    }

    public Album create(AlbumCreateCommand command) {
        Album album = new Album();
        album.setName(command.getName());
        album.setType(command.getType());
        album.setArtists(command.getArtists());
        album.setPhotoUri(command.getPhotoUri());
        album.setReleaseDate(command.getReleaseDate());
        album.setDescription(command.getDescription());
        albumsRepository.save(album);
        return album;
    }
}
