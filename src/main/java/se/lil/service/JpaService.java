package se.lil.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import se.lil.domain.User;

import com.google.inject.persist.Transactional;

public class JpaService implements IService {
    @Inject
    private EntityManager em;

    @Override
    @Transactional
    public User getUser() {
        Query q = em.createQuery("FROM User");
        q.setMaxResults(1);
        User u = (User) q.getSingleResult();
        System.out.println("HEJHEJHJEJHEJHEJHEHJ");
        return u;
    }
}
