package com.yrgo.dataaccess;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.yrgo.domain.Action;

@Repository
public class ActionDaoJpaImpl implements ActionDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Action newAction) {
        em.persist(newAction);
    }

    @Override
    public List<Action> getIncompleteActions(String userId) {    
        return em.createQuery("select action from Action as action where action.owning_user = :id and action.complete = false").getResultList();
    }

    @Override
    public void update(Action actionToUpdate) throws RecordNotFoundException {
        em.merge(actionToUpdate);        
    }

    @Override
    public void delete(Action oldAction) throws RecordNotFoundException {
        em.remove(oldAction);
    }
    
}
