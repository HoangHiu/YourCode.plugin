package com.plug.yourcodeplugin.repository;

import com.plug.yourcodeplugin.models.DailyStats;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

import java.time.LocalDate;

public class CodingStatDAO {
    private static volatile EntityManagerFactory emf;

    private static EntityManagerFactory getEmf() {
        if (emf == null) {
            synchronized (CodingStatDAO.class) {
                if (emf == null) {
                    emf = createEmfSafely();
                }
            }
        }
        return emf;
    }

    private static EntityManagerFactory createEmfSafely() {
        ClassLoader pluginCl = CodingStatDAO.class.getClassLoader();
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(pluginCl);
            try {
                return Persistence.createEntityManagerFactory("codingStatsPU");
            } catch (Throwable t) {
                t.printStackTrace();
                throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    public void save(DailyStats entity) {
        EntityManagerFactory emfLocal = getEmf();
        EntityManager em = emfLocal.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
        }
    }

    public void update(DailyStats stats) {
        EntityManager em = getEmf().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(stats);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public DailyStats findByStatDate(LocalDate date) {
        EntityManagerFactory emfLocal = getEmf();
        try (EntityManager em = emfLocal.createEntityManager()) {
            return em.createQuery(
                            "SELECT d FROM DailyStats d WHERE d.date = :date", DailyStats.class)
                    .setParameter("date", date)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public void close() {
        if (emf != null && emf.isOpen()) emf.close();
    }
}
