package com.khramykh.platform.application.tracksApi;

import com.khramykh.platform.application.commons.sort.TrackSort;
import com.khramykh.platform.application.exceptions.ResourceNotFoundException;
import com.khramykh.platform.application.repositories.*;
import com.khramykh.platform.application.tracksApi.commands.TrackCreateCommand;
import com.khramykh.platform.application.tracksApi.commands.TrackUpdateCommand;
import com.khramykh.platform.domain.commons.enums.TrackTypes;
import com.khramykh.platform.domain.entities.Artist;
import com.khramykh.platform.domain.entities.Track;
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

    @Value("${upload.path}")
    private String uploadPath;

    public Track getTrackById(int id) {
        return tracksRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Page<Track> getTrackByName(String name, int pageNum, int pageSize, TrackSort trackSort) {
        return tracksRepository.findByNameContaining(name, PageRequest.of(pageNum, pageSize, getSortType(trackSort)));
    }

    public Page<Track> getTrackByArtist(int id, int pageNum, int pageSize, TrackSort trackSort) {
        Artist artist = artistsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException((id)));
        return tracksRepository.findByArtists(artist, PageRequest.of(pageNum, pageSize, getSortType(trackSort)));
    }

    public Page<Track> getTracksByPage(int pageNum, int pageSize, TrackSort trackSort) {
        return tracksRepository.findAll(PageRequest.of(pageNum, pageSize, getSortType(trackSort)));
    }

    public List<Track> getTracksByAlbum(int albumID, TrackSort trackSort) {
        return tracksRepository.findByAlbum_Id(albumID);
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

    public Track update(TrackUpdateCommand command) throws IOException {
        Track oldTrack = tracksRepository.findById(command.getId()).orElseThrow(() -> new ResourceNotFoundException((command.getId())));
        Track updated = tracksRepository.save(convertTrackUpdateCommandToTrack(oldTrack, command));
        return updated;
    }

    public Track create(TrackCreateCommand command) throws IOException, ParseException {
        Track track = new Track();
        track.setName(command.getName());
        track.setDescription(command.getDescription());
        track.setReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(command.getReleaseDate()));

        albumsRepository.findById(command.getAlbum()).ifPresent(track::setAlbum);

        Arrays.stream(command.getArtists()).forEach(id -> {
            track.getArtists().add(artistsRepository.getOne(id));
        });
        Arrays.stream(command.getCategories()).forEach(id -> {
            track.getCategories().add(categoryRepository.getOne(id));
        });

        track.setPhotoUri(savePhoto(command.getPhotoFile()));
        track.setTrackUri(saveTrack(command.getTrackFile()));
        // track.setTrackText(command.getTrackText());
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

    private Track convertTrackUpdateCommandToTrack(Track oldTrack, TrackUpdateCommand command) throws IOException {
        oldTrack.setName(command.getName());
        oldTrack.setDescription(command.getDescription());
        if (command.getAlbum() != null)
            oldTrack.setAlbum(command.getAlbum());
        oldTrack.setArtists(command.getArtists());
        oldTrack.setCategories(command.getCategories());
        oldTrack.setPhotoUri(savePhoto(command.getPhotoFile()));
        oldTrack.setTrackUri(saveTrack(command.getTrackFile()));
        oldTrack.setTrackText(command.getTrackText());
        oldTrack.setType(TrackTypes.valueOf(command.getType()));
        return oldTrack;
    }

    public String savePhoto(MultipartFile file) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            System.out.println(uploadPath);
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile;

            file.transferTo(new File(uploadPath + "/images/tracks/" + resultFilename));

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
            String resultFilename = uuidFile;

            file.transferTo(new File(uploadPath + "/images/tracks/" + resultFilename));

            return resultFilename;
        } else {
            return Strings.EMPTY;
        }
    }

    public String saveTrack(MultipartFile file) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            System.out.println(uploadPath);
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile;

            file.transferTo(new File(uploadPath + "/tracks/" + resultFilename));

            return resultFilename;
        } else {
            return Strings.EMPTY;
        }
    }

    public String updateTrack(int id, MultipartFile file) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            System.out.println(uploadPath);
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile;

            file.transferTo(new File(uploadPath + "/tracks/" + resultFilename));

            return resultFilename;
        } else {
            return Strings.EMPTY;
        }
    }
}
