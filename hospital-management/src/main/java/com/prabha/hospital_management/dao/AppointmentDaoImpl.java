package com.prabha.hospital_management.dao;

import com.prabha.hospital_management.entity.Appointment;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AppointmentDaoImpl implements AppointmentDao {

    private final SessionFactory sessionFactory;

    @Override
    public Appointment save(Appointment appointment) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(appointment);
            transaction.commit();
            return appointment;
        } catch (RuntimeException ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    @Override
    public List<Appointment> findAll() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT DISTINCT a FROM Appointment a "
                    + "JOIN FETCH a.doctor "
                    + "JOIN FETCH a.patient "
                    + "ORDER BY a.appointmentId";
            Query<Appointment> query = session.createQuery(hql, Appointment.class);
            return query.getResultList();
        }
    }

    @Override
    public Optional<Appointment> findById(Long appointmentId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT a FROM Appointment a "
                    + "JOIN FETCH a.doctor "
                    + "JOIN FETCH a.patient "
                    + "WHERE a.appointmentId = :id";
            Query<Appointment> query = session.createQuery(hql, Appointment.class);
            query.setParameter("id", appointmentId);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public void deleteById(Long appointmentId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Appointment appointment = session.get(Appointment.class, appointmentId);
            if (appointment != null) {
                session.remove(appointment);
            }
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    @Override
    public boolean exists(Long doctorId, Long patientId, LocalDate appointmentDate, LocalTime appointmentTime) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(a) FROM Appointment a "
                    + "WHERE a.doctor.doctorId = :doctorId "
                    + "AND a.patient.patientId = :patientId "
                    + "AND a.appointmentDate = :appointmentDate "
                    + "AND a.appointmentTime = :appointmentTime";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("doctorId", doctorId);
            query.setParameter("patientId", patientId);
            query.setParameter("appointmentDate", appointmentDate);
            query.setParameter("appointmentTime", appointmentTime);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }
}
