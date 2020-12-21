package com.khramykh.platform.application.repositories;

import com.khramykh.platform.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends PagingAndSortingRepository<User, Integer> {
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findUserByActivationCode(String code);

    @Query("select u from User u where concat(u.firstName, u.lastName, u.email) like concat('%', ?1, '%')")
    Page<User> findByFilterContaining(String filter, PageRequest page);
}