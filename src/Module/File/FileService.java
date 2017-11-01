package Module.File;

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
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Son on 6/15/2017.
 */
public class FileService {
    private static SessionFactory factory;
    private static int currentActive;

    public FileService(SessionFactory factory) {
        this.factory = factory;
    }

    public FileService() {
        if (factory == null || currentActive != DatabaseEntity.Active) {
            IDatabaseService databaseService = new DatabaseService();
            IDatabaseControllService databaseControllService = new DatabaseControllService();
            factory = databaseControllService.createConfiguration(databaseService.get(DatabaseEntity.Active)).buildSessionFactory();
            currentActive = DatabaseEntity.Active;
        }
    }

    public static void setFactory(SessionFactory factory) {
        FileService.factory = factory;
    }


    public FileEntity get(int id) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<FileModel> criteria = builder.createQuery(FileModel.class);
        Root<FileModel> fileEntities = criteria.from(FileModel.class);
        criteria.where(builder.equal(fileEntities.get("fileId"), id));
        criteria.select(builder.construct(
                FileModel.class,
                fileEntities.get("fileId"), fileEntities.get("name"),
                fileEntities.get("createdTime"), fileEntities.get("type"),
                fileEntities.get("expiredTime"), fileEntities.get("userId")
                )
        );
        try {
            FileModel fileModel = session.createQuery(criteria).getSingleResult();
            return new FileEntity(fileModel);
        } catch (NoResultException e) {
            return null;
        }
    }

    public FileEntity download(int id) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<FileModel> criteria = builder.createQuery(FileModel.class);
        Root<FileModel> fileEntities = criteria.from(FileModel.class);
        criteria.where(builder.equal(fileEntities.get("fileId"), id));
        try {
            FileModel fileModel = session.createQuery(criteria).getSingleResult();
            return new FileEntity(fileModel);
        } catch (NoResultException e) {
            return null;
        }
    }

    public FileEntity create(int fileId, String name, byte[] data, Timestamp createdTime, String type, Timestamp expiredTime, Integer userId) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            FileEntity fileEntity = new FileEntity(fileId, name, data, createdTime, type, expiredTime, userId);
            fileEntity.createdTime = new Timestamp(System.currentTimeMillis());
            FileModel fileModel = fileEntity.toModel();
            Integer.valueOf(String.valueOf(session.save(fileModel)));
            tx.commit();
            FileEntity result = new FileEntity(fileModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public FileEntity create(FileEntity fileEntity) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            FileModel fileModel = fileEntity.toModel();
            Integer.valueOf(String.valueOf(session.save(fileModel)));
            tx.commit();
            FileEntity result = new FileEntity(fileModel);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public FileEntity update(int fileId, String name, byte[] data, Timestamp createdTime, String type, Timestamp expiredTime, Integer userId) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            FileEntity fileEntity = new FileEntity(fileId, name, data, createdTime, type, expiredTime, userId);
            session.update(fileEntity.toModel());
            tx.commit();
            FileEntity result = get(fileId);
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public FileEntity update(int fileId, FileEntity fileEntity) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(fileEntity.toModel());
            tx.commit();
            FileEntity result = get(fileId);
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
            FileModel fileModel = new FileModel();
            fileModel.setFileId(id);
            session.delete(fileModel);
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

    public List<FileEntity> get(SearchFileModel searchFileModel) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<FileModel> criteria = builder.createQuery(FileModel.class);
        Root<FileModel> FileEntities = criteria.from(FileModel.class);
        try {
            List<FileModel> fileEntities = session.createQuery(criteria).getResultList();
            return Lists.transform(fileEntities, fileEntity -> new FileEntity(fileEntity));
        } catch (NoResultException e) {
            return null;
        }
    }
}
