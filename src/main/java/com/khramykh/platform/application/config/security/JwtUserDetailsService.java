package com.khramykh.platform.application.config.security;

import com.khramykh.platform.application.commons.behaviors.UserPrincipal;
import com.khramykh.platform.application.repositories.UsersRepository;
import com.khramykh.platform.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = usersRepository.findByEmailIgnoreCase(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("{error.user.byemailnotfound} " + email);
        }
        return new UserPrincipal(user.get());
    }


}
