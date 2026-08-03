package com.prabha.hospital_management.dao;

import com.prabha.hospital_management.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PatientDaoImpl implements PatientDao {

    private final SessionFactory sessionFactory;

    @Override
    public Patient save(Patient patient) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(patient);
            transaction.commit();
            return patient;
        } catch (RuntimeException ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    @Override
    public Optional<Patient> findById(Long patientId) {
        try (Session session = sessionFactory.openSession()) {
            Patient patient = session.get(Patient.class, patientId);
            return Optional.ofNullable(patient);
        }
    }
}
