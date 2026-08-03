package com.prabha.employee_passport_mapping.dao;

import java.util.Optional;
import java.util.function.Function;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import com.prabha.employee_passport_mapping.entity.Employee;
import com.prabha.employee_passport_mapping.entity.Passport;
import com.prabha.employee_passport_mapping.exception.DatabaseException;
import com.prabha.employee_passport_mapping.exception.DuplicatePassportException;
import com.prabha.employee_passport_mapping.exception.EmployeeNotFoundException;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

    private final SessionFactory sessionFactory;

    public EmployeeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Employee save(Employee employee) {
        return execute(session -> {
            session.persist(employee);
            return employee;
        });
    }

    @Override
    public Optional<Employee> findById(Long employeeId) {
        return execute(session -> Optional.ofNullable(session.get(Employee.class, employeeId)));
    }

    @Override
    public Optional<Employee> findByIdWithPassport(Long employeeId) {
        return execute(session -> {
            String hql = "select e from Employee e left join fetch e.passport where e.employeeId = :id";
            Employee employee = session.createQuery(hql, Employee.class)
                    .setParameter("id", employeeId)
                    .uniqueResultOptional()
                    .orElse(null);
            return Optional.ofNullable(employee);
        });
    }

    @Override
    public boolean existsByPassportNumber(String passportNumber) {
        return execute(session -> {
            Long count = session.createQuery(
                            "select count(p) from Passport p where p.passportNumber = :num", Long.class)
                    .setParameter("num", passportNumber)
                    .uniqueResult();
            return count != null && count > 0;
        });
    }

    @Override
    public Employee assignPassport(Long employeeId, Passport passport) {
        return execute(session -> {
            Employee employee = session.get(Employee.class, employeeId);
            if (employee == null) {
                throw new EmployeeNotFoundException(employeeId);
            }
            employee.assignPassport(passport);
            session.persist(passport);
            return employee;
        });
    }

    @Override
    public void delete(Employee employee) {
        execute(session -> {
            // employee may be a detached instance passed in from the service layer;
            // re-attach/merge it into this Session before deleting so cascade works.
            Employee managed = session.get(Employee.class, employee.getEmployeeId());
            if (managed == null) {
                throw new EmployeeNotFoundException(employee.getEmployeeId());
            }
            session.remove(managed); // cascades to Passport (CascadeType.ALL + orphanRemoval on Employee#passport)
            return null;
        });
    }

    /**
     * Small template method that opens a Session, begins a Transaction, runs
     * the given unit of work, commits on success, rolls back on failure, and
     * always closes the Session. This is the "pure Hibernate" equivalent of
     * Spring's declarative @Transactional, written out explicitly as required.
     */
    private <T> T execute(Function<Session, T> work) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                T result = work.apply(session);
                transaction.commit();
                return result;
            } catch (EmployeeNotFoundException e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            } catch (ConstraintViolationException e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DuplicatePassportException("(constraint violation) " + e.getConstraintName());
            } catch (HibernateException e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DatabaseException("Database operation failed: " + e.getMessage(), e);
            }
        }
    }
}
