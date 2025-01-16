package com.quickride.demo.carrental.security;

import com.quickride.demo.carrental.exceptions.ApplicationException;
import com.quickride.demo.carrental.exceptions.ErrorCode;
import com.quickride.demo.carrental.model.AppUser;
import com.quickride.demo.carrental.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    private AppUser getOrThrow(String id) throws ApplicationException {
        return repository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }


    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        AppUser user = getOrThrow(id);
        return JwtAuthUser.builder()
                .id(user.getId())
                .build();
    }
}