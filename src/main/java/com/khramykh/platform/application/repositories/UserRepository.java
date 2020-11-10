package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.User;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    Optional<User> findByEmailIgnoreCase(String email);
}