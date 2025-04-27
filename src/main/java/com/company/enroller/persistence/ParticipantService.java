package com.company.enroller.persistence;

import java.util.Collection;
import java.util.Objects;

import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

    DatabaseConnector connector;

    public ParticipantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getAll(String order, String filter) {
        Query query = null;

        if (order != null) {
            if (Objects.equals(order, "ASC")) {
                String hql = "FROM Participant ORDER BY login";
                query = connector.getSession().createQuery(hql);
            } else if (Objects.equals(order, "DESC")) {
                String hql = "FROM Participant ORDER BY login DESC";
                query = connector.getSession().createQuery(hql);
            } else {
                String hql = "FROM Participant";
                query = connector.getSession().createQuery(hql);
            }
        }

        if (filter != null) {
            String hql = "FROM Participant WHERE login LIKE '%" + filter + "%'";
            query = connector.getSession().createQuery(hql);
        }

        return query.list();
    }

    public Collection<Participant> filterAll(String param) {
        String hql = "FROM Participant WHERE login LIKE '%" + param + "%'";
        Query query = connector.getSession().createQuery(hql);
        return query.list();

    }

    public Participant findByLogin(String login) {
        String hql = "FROM Participant WHERE login = :login";
        Query query = connector.getSession().createQuery(hql);
        query.setParameter("login", login);
        return (Participant) query.uniqueResult();
    }

    public void addParticipant(Participant participant) {
        connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        connector.getSession().getTransaction().commit();

    }

    public void deleteParticipant(String login) {
        connector.getSession().beginTransaction();
        String hql = "FROM Participant WHERE login = :login";
        Query query = connector.getSession().createQuery(hql);
        query.setParameter("login", login);
        Participant participant = (Participant) query.uniqueResult();
        connector.getSession().delete(participant);
        connector.getSession().getTransaction().commit();

    }

    public void actualizeParticipant(Participant participant) {
        connector.getSession().beginTransaction();
        String hql = "FROM Participant WHERE login = :login";
        Query query = connector.getSession().createQuery(hql);
        query.setParameter("login", participant.getLogin());
        Participant p = (Participant) query.uniqueResult();

        p.setPassword(participant.getPassword());
        connector.getSession().update(p);
        connector.getSession().getTransaction().commit();
    }

}
