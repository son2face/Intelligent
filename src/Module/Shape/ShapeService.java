package Module.Shape;

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
public class ShapeService {
    private static SessionFactory factory;
    private static int currentActive;

    public ShapeService(SessionFactory factory) {
        this.factory = factory;
    }

    public ShapeService() {
        if (factory == null || currentActive != DatabaseEntity.Active) {
            IDatabaseService databaseService = new DatabaseService();
            IDatabaseControllService databaseControllService = new DatabaseControllService();
            factory = databaseControllService.createConfiguration(databaseService.get(DatabaseEntity.Active)).buildSessionFactory();
            currentActive = DatabaseEntity.Active;
        }
    }

    public static void setFactory(SessionFactory factory) {
        ShapeService.factory = factory;
    }


    public ShapeEntity get(int id) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ShapeModel> criteria = builder.createQuery(ShapeModel.class);
        Root<ShapeModel> shapeEntities = criteria.from(ShapeModel.class);
        criteria.where(builder.equal(shapeEntities.get("shapeId"), id));
        try {
            ShapeModel shapeModel = session.createQuery(criteria).getSingleResult();
            return new ShapeEntity(shapeModel);
        } catch (NoResultException e) {
            return null;
        }
    }

    public ShapeEntity create(int shapeId, Integer problemId, Integer level, Integer userId) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ShapeEntity shapeEntity = new ShapeEntity(shapeId, problemId, level, userId);
            ShapeModel shapeModel =shapeEntity.toModel();
            Integer.valueOf(String.valueOf(session.save(shapeModel)));
            tx.commit();
            ShapeEntity result = new ShapeEntity(shapeModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public ShapeEntity create(ShapeEntity shapeEntity) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ShapeModel shapeModel =shapeEntity.toModel();
            Integer.valueOf(String.valueOf(session.save(shapeModel)));
            tx.commit();
            ShapeEntity result = new ShapeEntity(shapeModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public ShapeEntity update(int shapeId, Integer problemId, Integer level, Integer userId) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ShapeEntity shapeEntity = new ShapeEntity(shapeId, problemId, level, userId);
            session.update(shapeEntity.toModel());
            tx.commit();
            ShapeEntity result = get(shapeId);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public ShapeEntity update(int shapeId, ShapeEntity shapeEntity) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(shapeEntity.toModel());
            tx.commit();
            ShapeEntity result = get(shapeId);
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
            ShapeModel shapeModel = new ShapeModel();
            shapeModel.setShapeId(id);
            session.delete(shapeModel);
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

    public List<ShapeEntity> get(SearchShapeModel searchShapeModel) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ShapeModel> criteria = builder.createQuery(ShapeModel.class);
        Root<ShapeModel> ShapeEntities = criteria.from(ShapeModel.class);
        try {
            List<ShapeModel> shapeEntities = session.createQuery(criteria).getResultList();
            return Lists.transform(shapeEntities, shapeEntity -> new ShapeEntity(shapeEntity));
        } catch (NoResultException e) {
            return null;
        }
    }
}
