package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

	Session session;

	public MeetingService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = this.session.createQuery(hql);
		return query.list();
	}

	public Meeting getById(long id) {
		return session.get(Meeting.class, id);
	}

//	public Meeting getByTitle(String title) {
//		String hql = "FROM Meeting WHERE title = :title";
//		Query query = this.session.createQuery(hql);
//		query.setParameter("title", title);
//		return (Meeting) query.uniqueResult();
//	}

	public void addMeeting(Meeting meeting) {
		session.beginTransaction();
		session.save(meeting);
		session.getTransaction().commit();
	}

	public void deleteMeeting(long id) {
		session.beginTransaction();
		String hql = "FROM Meeting WHERE id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		Meeting m = (Meeting) query.uniqueResult();
		session.delete(m);
		session.getTransaction().commit();
	}

	public void actualizeMeeting(Meeting meeting) {
		session.beginTransaction();
		String hql = "FROM Meeting WHERE id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", meeting.getId());
		Meeting m = (Meeting) query.uniqueResult();

		m.setTitle(meeting.getTitle());
		m.setDescription(meeting.getDescription());
		m.setDate(meeting.getDate());
		session.update(m);
		session.getTransaction().commit();
	}
}
