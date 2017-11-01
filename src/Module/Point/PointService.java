package Module.Point;

import Manager.Entity.DatabaseEntity;
import Manager.Interface.IDatabaseControllService;
import Manager.Interface.IDatabaseService;
import Manager.Service.DatabaseControllService;
import Manager.Service.DatabaseService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Son on 6/15/2017.
 */
public class PointService {
    private static SessionFactory factory;
    private static int currentActive;

    public PointService(SessionFactory factory) {
        this.factory = factory;
    }

    public PointService() {
        if (factory == null || currentActive != DatabaseEntity.Active) {
            IDatabaseService databaseService = new DatabaseService();
            IDatabaseControllService databaseControllService = new DatabaseControllService();
            factory = databaseControllService.createConfiguration(databaseService.get(DatabaseEntity.Active)).buildSessionFactory();
            currentActive = DatabaseEntity.Active;
        }
    }

    public static void setFactory(SessionFactory factory) {
        PointService.factory = factory;
    }

    public PointEntity get(int id) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PointModel> criteria = builder.createQuery(PointModel.class);
        Root<PointModel> pointEntities = criteria.from(PointModel.class);
        criteria.where(builder.equal(pointEntities.get("pointId"), id));
        try {
            PointModel pointModel = session.createQuery(criteria).getSingleResult();
            return new PointEntity(pointModel);
        } catch (NoResultException e) {
            return null;
        }
    }

    public PointEntity create(PointEntity pointEntity) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            PointModel pointModel = pointEntity.toModel();
            Object o = session.save(pointModel);
            tx.commit();
            PointEntity result = new PointEntity(pointModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public PointEntity create(int pointId, Integer x, Integer y, Integer problemId) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            PointEntity pointEntity = new PointEntity(pointId, x, y, problemId);
            PointModel pointModel = pointEntity.toModel();
            Integer.valueOf(String.valueOf(session.save(pointModel)));
            tx.commit();
            PointEntity result = new PointEntity(pointModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public PointEntity update(int pointId, Integer x, Integer y, Integer problemId) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            PointEntity pointEntity = new PointEntity(pointId, x, y, problemId);
            session.update(pointEntity.toModel());
            tx.commit();
            PointEntity result = get(pointId);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public PointEntity update(int pointId, PointEntity pointEntity) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(pointEntity.toModel());
            tx.commit();
            PointEntity result = get(pointId);
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
            PointModel pointModel = new PointModel();
            pointModel.setPointId(id);
            session.delete(pointModel);
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

    public List<PointEntity> get(SearchPointModel searchPointModel) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PointModel> criteria = builder.createQuery(PointModel.class);
        Root<PointModel> PointEntities = criteria.from(PointModel.class);
        try {
            List<PointModel> pointEntities = session.createQuery(criteria).getResultList();
            return pointEntities.stream()
                    .map(s -> new PointEntity(s)).collect(Collectors.toList());
        } catch (NoResultException e) {
            return null;
        }
    }
}
