package Module.Edge;

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
import java.util.stream.Collectors;

/**
 * Created by Son on 6/15/2017.
 */
public class EdgeService {
    private static SessionFactory factory;
    private static int currentActive;

    public EdgeService(SessionFactory factory) {
        this.factory = factory;
    }

    public EdgeService() {
        if (factory == null || currentActive != DatabaseEntity.Active) {
            IDatabaseService databaseService = new DatabaseService();
            IDatabaseControllService databaseControllService = new DatabaseControllService();
            factory = databaseControllService.createConfiguration(databaseService.get(DatabaseEntity.Active)).buildSessionFactory();
            currentActive = DatabaseEntity.Active;
        }
    }

    public static void setFactory(SessionFactory factory) {
        EdgeService.factory = factory;
    }


    public EdgeEntity get(int id) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<EdgeModel> criteria = builder.createQuery(EdgeModel.class);
        Root<EdgeModel> EdgeEntities = criteria.from(EdgeModel.class);
        criteria.where(builder.equal(EdgeEntities.get("edgeId"), id));
        try {
            EdgeModel edgeModel = session.createQuery(criteria).getSingleResult();
            return new EdgeEntity(edgeModel);
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    public EdgeEntity create(int edgeId, Double startX, Double startY, Double endX, Double endY, Integer shapeId) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            EdgeEntity edgeEntity = new EdgeEntity(edgeId, startX, startY, endX, endY, shapeId);
            EdgeModel edgeModel = edgeEntity.toModel();
            Integer.valueOf(String.valueOf(session.save(edgeModel)));
            tx.commit();
            EdgeEntity result = new EdgeEntity(edgeModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return null;
    }

    public EdgeEntity create(EdgeEntity edgeEntity) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            EdgeModel edgeModel = edgeEntity.toModel();
            Integer.valueOf(String.valueOf(session.save(edgeModel)));
            tx.commit();
            EdgeEntity result = new EdgeEntity(edgeModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return null;
    }

    public EdgeEntity update(int edgeId, Double startX, Double startY, Double endX, Double endY, Integer shapeId) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            EdgeEntity edgeEntity = new EdgeEntity(edgeId, startX, startY, endX, endY, shapeId);
            session.update(edgeEntity.toModel());
            tx.commit();
            EdgeEntity result = get(edgeId);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return null;
    }

    public EdgeEntity update(int edgeId, EdgeEntity edgeEntity) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.update(edgeEntity.toModel());
            tx.commit();
            EdgeEntity result = get(edgeId);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(int id) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            EdgeModel edgeModel = new EdgeModel();
            edgeModel.setEdgeId(id);
            session.delete(edgeModel);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    public List<EdgeEntity> get(SearchEdgeModel searchEdgeModel) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<EdgeModel> criteria = builder.createQuery(EdgeModel.class);
        Root<EdgeModel> EdgeEntities = criteria.from(EdgeModel.class);
        try {
            List<EdgeModel> edgeList = session.createQuery(criteria).getResultList();
            return edgeList.stream()
                    .map(s -> new EdgeEntity(s)).collect(Collectors.toList());
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }
}
