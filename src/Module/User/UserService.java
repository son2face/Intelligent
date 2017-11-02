package Module.User;

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
public class UserService {
    private static SessionFactory factory;
    private static int currentActive;

    public UserService(SessionFactory factory) {
        this.factory = factory;
    }

    public UserService() {
        if (factory == null || currentActive != DatabaseEntity.Active) {
            IDatabaseService databaseService = new DatabaseService();
            IDatabaseControllService databaseControllService = new DatabaseControllService();
            factory = databaseControllService.createConfiguration(databaseService.get(DatabaseEntity.Active)).buildSessionFactory();
            currentActive = DatabaseEntity.Active;
        }
    }

    public static void setFactory(SessionFactory factory) {
        UserService.factory = factory;
    }


    public UserEntity get(int id) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> criteria = builder.createQuery(UserModel.class);
        Root<UserModel> userEntities = criteria.from(UserModel.class);
        criteria.where(builder.equal(userEntities.get("userId"), id));
        try {
            UserModel userModel = session.createQuery(criteria).getSingleResult();
            return new UserEntity(userModel);
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    public UserEntity create(int userId, String userName, String passWord) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            UserEntity userEntity = new UserEntity(userId, userName, passWord);
            UserModel userModel = userEntity.toModel();
            Integer.valueOf(String.valueOf(session.save(userModel)));
            tx.commit();
            UserEntity result = new UserEntity(userModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public UserEntity create(UserEntity userEntity) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            UserModel userModel = userEntity.toModel();
            Integer.valueOf(String.valueOf(session.save(userModel)));
            tx.commit();
            UserEntity result = new UserEntity(userModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public UserEntity update(int userId, String userName, String passWord) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            UserEntity userEntity = new UserEntity(userId, userName, passWord);
            session.update(userEntity.toModel());
            tx.commit();
            UserEntity result = get(userId);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public UserEntity update(int userId, UserEntity userEntity) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(userEntity.toModel());
            tx.commit();
            UserEntity result = get(userId);
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
            UserModel userModel = new UserModel();
            userModel.setUserId(id);
            session.delete(userModel);
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

    public List<UserEntity> get(SearchUserModel searchUserModel) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> criteria = builder.createQuery(UserModel.class);
        Root<UserModel> UserEntities = criteria.from(UserModel.class);
        try {
            List<UserModel> userEntities = session.createQuery(criteria).getResultList();
            return userEntities.stream()
                    .map(s -> new UserEntity(s)).collect(Collectors.toList());
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }
}
