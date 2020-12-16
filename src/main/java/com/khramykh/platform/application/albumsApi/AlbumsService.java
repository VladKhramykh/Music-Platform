package com.khramykh.platform.application.albumsApi;

import com.khramykh.platform.application.albumsApi.commands.AlbumCreateCommand;
import com.khramykh.platform.application.albumsApi.commands.AlbumUpdateCommand;
import com.khramykh.platform.application.commons.sort.AlbumSort;
import com.khramykh.platform.application.exceptions.ResourceNotFoundException;
import com.khramykh.platform.application.exceptions.UserNotFoundException;
import com.khramykh.platform.application.repositories.AlbumsRepository;
import com.khramykh.platform.application.repositories.ArtistsRepository;
import com.khramykh.platform.application.repositories.UsersRepository;
import com.khramykh.platform.domain.commons.enums.AlbumTypes;
import com.khramykh.platform.domain.entities.Album;
import com.khramykh.platform.domain.entities.Artist;
import com.khramykh.platform.domain.entities.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AlbumsService {
    @Autowired
    AlbumsRepository albumsRepository;
    @Autowired
    ArtistsRepository artistsRepository;
    @Autowired
    UsersRepository usersRepository;
    @Value("${upload.path}")
    private String uploadPath;

    public Album getAlbumById(int id) {
        return albumsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Page<Album> getAlbumByNameContaining(String name, int pageNum, int pageSize, AlbumSort albumSort) {
        return albumsRepository.findAlbumByNameContaining(name, PageRequest.of(pageNum, pageSize, getSortType(albumSort)));
    }

    public Page<Album> getAlbumsByPage(int pageNum, int pageSize, AlbumSort albumSort) {
        return albumsRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(albumSort)));
    }

    public List<Album> getAlbumsByArtist(int artistId, AlbumSort albumSort) {
        Artist artist = artistsRepository.findById(artistId).orElseThrow(() -> new ResourceNotFoundException(artistId));
        return albumsRepository.findAlbumsByArtistsContaining(artist, getSortType(albumSort));
    }

    // TODO need to realize finding top-10 albums
    public Page<Album> getMostPopularAlbumsByPage(int pageNum, int pageSize, AlbumSort albumSort) {
        return albumsRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(albumSort)));
    }

    public void removeById(int id) {
        if (!albumsRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        albumsRepository.deleteById(id);
    }

    public Album update(AlbumUpdateCommand command) throws ParseException, IOException {
        Album oldAlbum = albumsRepository.findById(command.getId()).orElseThrow(() -> new ResourceNotFoundException((command.getId())));
        Album updated = albumsRepository.save(convertAlbumUpdateCommandToAlbum(oldAlbum, command));
        return updated;
    }

    private Album convertAlbumUpdateCommandToAlbum(Album oldAlbum, AlbumUpdateCommand command) throws ParseException, IOException {
        oldAlbum.setName(command.getName());
        oldAlbum.setType(AlbumTypes.valueOf(command.getType()));
        oldAlbum.getArtists().clear();
        Arrays.stream(command.getArtists()).forEach(id -> {
            oldAlbum.getArtists().add(artistsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
        });
        oldAlbum.setPhotoUri(saveFile(command.getFile()));
        oldAlbum.setReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(command.getReleaseDate()));
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

    public Album create(AlbumCreateCommand command) throws ParseException, IOException {
        Album album = new Album();
        album.setName(command.getName());
        album.setType(AlbumTypes.valueOf(command.getType()));
        Arrays.stream(command.getArtists()).forEach(id -> {
            album.getArtists().add(artistsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
        });
        album.setPhotoUri(saveFile(command.getFile()));
        album.setReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(command.getReleaseDate()));
        album.setDescription(command.getDescription());
        albumsRepository.save(album);
        return album;
    }

    public void like(int albumId, int userId) {
        albumsRepository.findById(albumId).map(album -> {
            usersRepository.findById(userId).map(user -> album.getLikes().add(user));
            return albumsRepository.save(album);
        });
    }

    public void dislike(int albumId, int userId) {
        albumsRepository.findById(albumId).map(album -> {
            usersRepository.findById(userId).map(user -> album.getLikes().remove(user));
            return albumsRepository.save(album);
        });
    }

    public String saveFile(MultipartFile file) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            System.out.println(uploadPath);
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename().replace(' ', '_');

            file.transferTo(new File(uploadPath + "/images/albums/" + resultFilename));

            return resultFilename;
        } else {
            return Strings.EMPTY;
        }
    }

    public String updatePhoto(int id, MultipartFile file) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            System.out.println(uploadPath);
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename().replace(' ', '_');

            file.transferTo(new File(uploadPath + "/images/albums/" + resultFilename));

            return resultFilename;
        } else {
            return Strings.EMPTY;
        }
    }

    public Page<Album> getLastReleases(int pageNum, int pageSize) {
        return albumsRepository.findByOrderByReleaseDateDesc(PageRequest.of(pageNum, pageSize));
    }

    public Page<Album> getLastReleasesByArtist(Integer artistId, int pageNum, int pageSize) {
        Artist artist = this.artistsRepository.findById(artistId).orElseThrow(ResourceNotFoundException::new);
        return albumsRepository.findAlbumsByArtistsContainingOrderByReleaseDateDesc(artist, PageRequest.of(pageNum, pageSize));
    }

    public Page<Album> getFavouriteAlbumsByUser(String username, int pageNum, int pageSize, AlbumSort albumSort) {
        User user = usersRepository.findByEmailIgnoreCase(username).orElseThrow(UserNotFoundException::new);
        return this.albumsRepository.findAllByLikesContains(user, PageRequest.of(pageNum, pageSize, getSortType(albumSort)));
    }
}
