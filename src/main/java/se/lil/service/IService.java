package se.lil.service;

import se.lil.domain.User;

import com.google.inject.ImplementedBy;

@ImplementedBy(JpaService.class)
public interface IService {
    public User getUser();
}
