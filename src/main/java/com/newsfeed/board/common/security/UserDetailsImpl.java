package com.newsfeed.board.common.security;

import com.newsfeed.board.user.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private final UserEntity user;

    public UserDetailsImpl(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUser(){
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(adminAuthority);

        return authorities;
    }
    public String getId() { return user.getId(); }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
