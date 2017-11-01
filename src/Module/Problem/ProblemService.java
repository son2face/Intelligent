package Module.Problem;

import Manager.Entity.DatabaseEntity;
import Manager.Interface.IDatabaseControllService;
import Manager.Interface.IDatabaseService;
import Manager.Service.DatabaseControllService;
import Manager.Service.DatabaseService;
import Module.Edge.EdgeEntity;
import Module.Edge.EdgeService;
import Module.File.FileEntity;
import Module.File.FileService;
import Module.Shape.ShapeEntity;
import Module.Shape.ShapeService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by Son on 6/15/2017.
 */
public class ProblemService {
    private static SessionFactory factory;
    private static int currentActive;

    private FileService fileService = null;
    private ShapeService shapeService = null;
    private EdgeService edgeService = null;

    public ProblemService(SessionFactory factory) {
        this.factory = factory;
    }

    public ProblemService(FileService fileService, ShapeService shapeService, EdgeService edgeService) {
        if (factory == null || currentActive != DatabaseEntity.Active) {
            IDatabaseService databaseService = new DatabaseService();
            IDatabaseControllService databaseControllService = new DatabaseControllService();
            factory = databaseControllService.createConfiguration(databaseService.get(DatabaseEntity.Active)).buildSessionFactory();
            currentActive = DatabaseEntity.Active;
        }
        this.fileService = fileService;
        this.shapeService = shapeService;
        this.edgeService = edgeService;
    }


    public static void setFactory(SessionFactory factory) {
        ProblemService.factory = factory;
    }

    public List<ProblemEntity> get(SearchProblemModel searchProblemModel) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProblemModel> criteria = builder.createQuery(ProblemModel.class);
        Root<ProblemModel> ProblemEntities = criteria.from(ProblemModel.class);
        try {
            List<ProblemModel> problemList = session.createQuery(criteria).getResultList();
            List<ProblemEntity> problemEntities = problemList.stream()
                    .map(problemModel ->
                            new ProblemEntity(
                                    problemModel, problemModel.getPointsByProblemId(),
                                    problemModel.getShapesByProblemId(),
                                    problemModel.getUserByUserId()
                            )
                    ).collect(Collectors.toList());
            for (int i = 0; i < problemEntities.size(); i++) {
                ProblemEntity problemEntity = problemEntities.get(i);
                if (problemEntity.fileId != null) {
                    FileEntity fileEntity = fileService.get(problemEntity.fileId);
                    problemEntity.setFileEntity(fileEntity);
                    problemEntities.set(i, problemEntity);
                }
            }
            return problemEntities;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ProblemEntity get(int id) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProblemModel> criteria = builder.createQuery(ProblemModel.class);
        Root<ProblemModel> problemEntities = criteria.from(ProblemModel.class);
        criteria.where(builder.equal(problemEntities.get("problemId"), id));
        try {
            ProblemModel problemModel = session.createQuery(criteria).getSingleResult();
            return new ProblemEntity(
                    problemModel, problemModel.getPointsByProblemId(),
                    problemModel.getShapesByProblemId(),
                    problemModel.getUserByUserId()
            );
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
            ProblemModel problemModel = problemEntity.toModel();
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
            ProblemModel problemModel = problemEntity.toModel();
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


    public int process(int id) {
        Session session = factory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ProblemModel> criteria = builder.createQuery(ProblemModel.class);
        Root<ProblemModel> problemEntities = criteria.from(ProblemModel.class);
        criteria.where(builder.equal(problemEntities.get("problemId"), id));
        try {
            ProblemModel problemModel = session.createQuery(criteria).getSingleResult();
            ProblemEntity problemEntity = new ProblemEntity(problemModel);
            if (problemEntity.fileId == null) return 0;
            FileEntity fileEntity = fileService.download(problemEntity.fileId);
            ByteArrayInputStream bis = new ByteArrayInputStream(fileEntity.toModel().getData());
            Scanner scanner = new Scanner(bis);
            int totalShape = scanner.nextInt();
            for (int i = 0; i < totalShape; i++) {
                int shapeCode = scanner.nextInt();
                ShapeEntity shapeEntity = new ShapeEntity(0, id, 1, null, shapeCode);
                shapeEntity = shapeService.create(shapeEntity);
                int numberOfShape = scanner.nextInt();
                if (numberOfShape < 3) return 0;
                int x0 = scanner.nextInt();
                int y0 = scanner.nextInt();
                for (int j = 1; j < numberOfShape; j++) {
                    try {
                        int x = scanner.nextInt();
                        int y = scanner.nextInt();
                        EdgeEntity edgeEntity = new EdgeEntity(0, Double.valueOf(x0), Double.valueOf(y0), Double.valueOf(x), Double.valueOf(y), shapeEntity.shapeId);
                        edgeService.create(edgeEntity);
                        x0 = x;
                        y0 = y;
                    } catch (Exception e) {

                    }
                }
            }
            return totalShape;
        } catch (NoResultException e) {
//            return null;
        }
        return 10;
    }

}
