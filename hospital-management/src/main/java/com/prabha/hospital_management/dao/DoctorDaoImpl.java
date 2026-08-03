package com.prabha.hospital_management.dao;

import com.prabha.hospital_management.entity.Doctor;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DoctorDaoImpl implements DoctorDao {

    private final SessionFactory sessionFactory;

    @Override
    public Doctor save(Doctor doctor) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(doctor);
            transaction.commit();
            return doctor;
        } catch (RuntimeException ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    @Override
    public Optional<Doctor> findById(Long doctorId) {
        try (Session session = sessionFactory.openSession()) {
            Doctor doctor = session.get(Doctor.class, doctorId);
            return Optional.ofNullable(doctor);
        }
    }
}
