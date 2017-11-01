package Module.Problem;

import Manager.Entity.DatabaseEntity;
import Manager.Interface.IDatabaseControllService;
import Manager.Interface.IDatabaseService;
import Manager.Service.DatabaseControllService;
import Manager.Service.DatabaseService;
import com.google.common.collect.Lists;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Son on 6/15/2017.
 */
public class ProblemService {
    private static SessionFactory factory;
    private static int currentActive;

    public ProblemService(SessionFactory factory) {
        this.factory = factory;
    }

    public ProblemService() {
        if (factory == null || currentActive != DatabaseEntity.Active) {
            IDatabaseService databaseService = new DatabaseService();
            IDatabaseControllService databaseControllService = new DatabaseControllService();
            factory = databaseControllService.createConfiguration(databaseService.get(DatabaseEntity.Active)).buildSessionFactory();
            currentActive = DatabaseEntity.Active;
        }
    }

    public static void setFactory(SessionFactory factory) {
        ProblemService.factory = factory;
    }


    public ProblemEntity get(int id) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProblemModel> criteria = builder.createQuery(ProblemModel.class);
        Root<ProblemModel> problemEntities = criteria.from(ProblemModel.class);
        criteria.where(builder.equal(problemEntities.get("problemId"), id));
        try {
            ProblemModel problemModel = session.createQuery(criteria).getSingleResult();
            return new ProblemEntity(problemModel);
        } catch (NoResultException e) {
            return null;
        }
    }

    public ProblemEntity create(int problemId, String status, Integer fileId, Integer userId) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ProblemEntity problemEntity = new ProblemEntity(problemId, status, fileId, userId);
            ProblemModel problemModel= problemEntity.toModel();
            Integer.valueOf(String.valueOf(session.save(problemModel)));
            tx.commit();
            ProblemEntity result = new ProblemEntity(problemModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public ProblemEntity create(ProblemEntity problemEntity) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            problemEntity.status = Status.toString(Status.CREATE);
            ProblemModel problemModel= problemEntity.toModel();
            Integer.valueOf(String.valueOf(session.save(problemModel)));
            tx.commit();
            ProblemEntity result = new ProblemEntity(problemModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public ProblemEntity update(int problemId, String status, Integer fileId, Integer userId) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ProblemEntity problemEntity = new ProblemEntity(problemId, status, fileId, userId);
            session.update(problemEntity.toModel());
            tx.commit();
            ProblemEntity result = get(problemId);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public ProblemEntity update(int problemId, ProblemEntity problemEntity) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(problemEntity.toModel());
            tx.commit();
            ProblemEntity result = get(problemId);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public boolean delete(int id) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ProblemModel problemModel = new ProblemModel();
            problemModel.setProblemId(id);
            session.delete(problemModel);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return false;
    }

    public List<ProblemEntity> get(SearchProblemModel searchProblemModel) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProblemModel> criteria = builder.createQuery(ProblemModel.class);
        Root<ProblemModel> ProblemEntities = criteria.from(ProblemModel.class);
        try {
            List<ProblemModel> problemEntities = session.createQuery(criteria).getResultList();
            return Lists.transform(problemEntities, problemEntity -> new ProblemEntity(problemEntity,problemEntity.getPointsByProblemId(),problemEntity.getShapesByProblemId()));
        } catch (NoResultException e) {
            return null;
        }
    }
}
