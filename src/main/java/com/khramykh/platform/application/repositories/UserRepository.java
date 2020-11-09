package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    User findByEmail(String email);
}