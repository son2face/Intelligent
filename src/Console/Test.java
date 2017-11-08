package Console;

import Manager.Entity.DatabaseEntity;
import Manager.Interface.IDatabaseControllService;
import Manager.Interface.IDatabaseService;
import Manager.Service.DatabaseControllService;
import Manager.Service.DatabaseService;
import Module.Edge.EdgeEntity;
import Module.Edge.EdgeService;
import Module.File.FileService;
import Module.Point.PointService;
import Module.Problem.ProblemEntity;
import Module.Problem.ProblemService;
import Module.Shape.ShapeEntity;
import Module.Shape.ShapeService;
import Module.User.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rits.cloning.Cloner;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Test {
    IDatabaseService databaseService;
    IDatabaseControllService databaseControllService;
    EdgeService edgeService;
    FileService fileService;
    ShapeService shapeService;
    UserService userService;
    PointService pointService;
    ProblemService problemService;

    Test() {
        DatabaseEntity.setFileDir("H:\\apache-tomcat-9.0.1\\bin\\database.txt");
        DatabaseEntity.loadData();
        databaseService = new DatabaseService();
        databaseControllService = new DatabaseControllService();
        edgeService = new EdgeService();
        fileService = new FileService();
        shapeService = new ShapeService();
        userService = new UserService();
        pointService = new PointService();
        problemService = new ProblemService(fileService, shapeService, edgeService);
    }

    public static void main(String[] args) {
        Test test = new Test();
        EdgeEntity edgeEntityA = new EdgeEntity(0, Double.valueOf(39), Double.valueOf(7), Double.valueOf(39), Double.valueOf(12), Integer.valueOf(1));
        EdgeEntity edgeEntityB = new EdgeEntity(0, Double.valueOf(39), Double.valueOf(17), Double.valueOf(39), Double.valueOf(12), Integer.valueOf(1));
//        test.findIntersectPoint(edgeEntityA, edgeEntityB);
        //        test.findShapeToCompare();
//        test.Print();
//        test.PrintShape(67);
//        test.PrintShape(70);
//        test.combineShape();
        test.Process(1);
// print as WKT
    }

    void Process(int problemId) {
        ObjectMapper mapper = new ObjectMapper();
        ProblemEntity problemEntity = problemService.get(problemId);
        int shapeSize = problemEntity.shapeEntities.size();
        for (int i = 0; i < shapeSize; i++) {
            ShapeEntity shapeEntity = problemEntity.shapeEntities.get(i);
            problemEntity.shapeEntities.get(i).edgeEntities = shapeService.get(shapeEntity.shapeId).edgeEntities;
        }
        for (int i = 0; i < shapeSize - 1; i++) {
            for (int j = i + 1; j < shapeSize; j++) {
                ShapeEntity shapeA = problemEntity.shapeEntities.get(i);
                ShapeEntity shapeB = problemEntity.shapeEntities.get(j);
                List<ShapeEntity> shapeEntities = combineShape(shapeA, shapeB);
                try {
                    System.out.println(mapper.writeValueAsString(shapeEntities));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void Print() {
        ProblemEntity problemEntity = problemService.get(1);
        problemEntity.shapeEntities.forEach(se -> {
            ShapeEntity shapeEntity = shapeService.get(se.shapeId);
            shapeEntity.edgeEntities.forEach(edgeEntity -> {
                System.out.println(
                        Math.sqrt(Math.pow((edgeEntity.endY - edgeEntity.startY), 2) + Math.pow((edgeEntity.endX - edgeEntity.startX), 2))
                );
            });
            System.out.println();
            System.out.println();
            System.out.println();
        });
    }

    void findShapeToCompare() {
        ProblemEntity problemEntity = problemService.get(1);
        int shapeSize = problemEntity.shapeEntities.size();
        for (int i = 0; i < shapeSize; i++) {
            ShapeEntity shapeEntity = problemEntity.shapeEntities.get(i);
            problemEntity.shapeEntities.get(i).edgeEntities = shapeService.get(shapeEntity.shapeId).edgeEntities;
        }
        for (int i = 0; i < shapeSize - 1; i++) {
            for (int j = i + 1; j < shapeSize; j++) {
                ShapeEntity shapeA = problemEntity.shapeEntities.get(i);
                ShapeEntity shapeB = problemEntity.shapeEntities.get(j);
                shapeA.edgeEntities.forEach(edgeEntityA -> {
                    shapeB.edgeEntities.forEach(edgeEntityB -> {
                        if (squareLengthOfEdge(edgeEntityA) == squareLengthOfEdge(edgeEntityB)) {
                            System.out.println(shapeA.shapeId);
                            System.out.println(shapeB.shapeId);
                            System.out.println(edgeEntityA.edgeId);
                            System.out.println(squareLengthOfEdge(edgeEntityA));
                            System.out.println(edgeEntityB.edgeId);
                            System.out.println(squareLengthOfEdge(edgeEntityB));
                            System.out.println();
                            ShapeEntity[] shapeEntities = {shapeA, shapeB};
                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                System.out.println(mapper.writeValueAsString(shapeEntities));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                        }
                    });
                });
            }
        }
    }


    List<ShapeEntity> combineShape(ShapeEntity shapeEntityA, ShapeEntity shapeEntityB) {
        List<ShapeEntity> result = new ArrayList<>();
        shapeEntityA.edgeEntities.forEach(edgeEntityA -> {
            shapeEntityB.edgeEntities.forEach(edgeEntityB -> {
                if (squareLengthOfEdge(edgeEntityA) == squareLengthOfEdge(edgeEntityB)) {
                    ShapeEntity flipXShape = flipX(shapeEntityB, edgeEntityB);
                    ShapeEntity flipYShape = flipY(shapeEntityB, edgeEntityB);
                    ShapeEntity flipXYShape = flipXY(shapeEntityB, edgeEntityB);
                    ShapeEntity mergedFlipXShape = mergeShape(shapeEntityA, flipXShape, edgeEntityA, edgeEntityB);
                    ShapeEntity mergedFlipYShape = mergeShape(shapeEntityA, flipYShape, edgeEntityA, edgeEntityB);
                    ShapeEntity mergedFlipXYShape = mergeShape(shapeEntityA, flipXYShape, edgeEntityA, edgeEntityB);
                    if (mergedFlipXShape != null) result.add(mergedFlipXShape);
                    if (mergedFlipYShape != null) result.add(mergedFlipYShape);
                    if (mergedFlipXYShape != null) result.add(mergedFlipXYShape);
                }
            });
        });
        return result;
    }


    Point findIntersectPoint(EdgeEntity edgeEntityA, EdgeEntity edgeEntityB) {
        double a1 = edgeEntityA.endY - edgeEntityA.startY;
        double b1 = edgeEntityA.startX - edgeEntityA.endX;
        double c1 = a1 * (edgeEntityA.startX) + b1 * (edgeEntityA.startY);

        // Line CD represented as a2x + b2y = c2
        double a2 = edgeEntityB.endY - edgeEntityB.startY;
        double b2 = edgeEntityB.startX - edgeEntityB.endX;
        double c2 = a2 * (edgeEntityB.startX) + b2 * (edgeEntityB.startY);
        double determinant = a1 * b2 - a2 * b1;
        if (determinant == 0) {
            return null;
        } else {
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;
            return new Point(x, y);
        }

    }

    ShapeEntity translate(ShapeEntity shapeEntity, EdgeEntity edgeEntityA, EdgeEntity edgeEntityB) {
        Cloner cloner = new Cloner();
        ShapeEntity clone = cloner.deepClone(shapeEntity);
        if (findIntersectPoint(edgeEntityA, edgeEntityB) != null) {
            System.out.println("Khong dich chuyen duoc");
            return null;
        }
        double x = Math.max(edgeEntityB.startX, edgeEntityB.endX) - Math.max(edgeEntityA.startX, edgeEntityA.endX);
        double y = Math.max(edgeEntityB.startY, edgeEntityB.endY) - Math.max(edgeEntityA.startY, edgeEntityA.endY);
        clone.edgeEntities.forEach(edgeEntity -> {
            edgeEntity.startX -= x;
            edgeEntity.startY -= y;
            edgeEntity.endX -= x;
            edgeEntity.endY -= y;
        });
        return clone;
    }

    ShapeEntity mergeShape(ShapeEntity shapeEntityA, ShapeEntity shapeEntityB, EdgeEntity edgeEntityA, EdgeEntity edgeEntityB) {
        ShapeEntity newShapeEntityB = translate(shapeEntityB, edgeEntityA, edgeEntityB);
        Cloner cloner = new Cloner();
        if (newShapeEntityB != null) {
            ShapeEntity mergedShape = cloner.deepClone(shapeEntityA);
            List<EdgeEntity> edgeEntityListA = shapeEntityA.edgeEntities;
            List<EdgeEntity> edgeEntityListB = newShapeEntityB.edgeEntities;
            List<Coordinate> coordinateListA = edgeEntityListA.parallelStream().map(edgeEntity ->
                    new Coordinate(edgeEntity.startX, edgeEntity.startY)
            ).collect(Collectors.toList());
            coordinateListA.add(new Coordinate(edgeEntityListA.get(0).startX, edgeEntityListA.get(0).startY));
            List<Coordinate> coordinateListB = edgeEntityListB.parallelStream().map(edgeEntity ->
                    new Coordinate(edgeEntity.startX, edgeEntity.startY)
            ).collect(Collectors.toList());
            coordinateListB.add(new Coordinate(edgeEntityListB.get(0).startX, edgeEntityListB.get(0).startY));
            Polygon p1 = new GeometryFactory().createPolygon(coordinateListA.toArray(new Coordinate[coordinateListA.size()]));
            Polygon p2 = new GeometryFactory().createPolygon(coordinateListB.toArray(new Coordinate[coordinateListB.size()]));
            com.vividsolutions.jts.geom.Point point1;
            Geometry union = p1.union(p2);
            Coordinate[] newCoordinates = union.getCoordinates();
            int count = 1;
            mergedShape.edgeEntities = new ArrayList<EdgeEntity>();
            for (int i = 0; i < newCoordinates.length - 1; i++) {
                mergedShape.edgeEntities.add(new EdgeEntity(count++, newCoordinates[i].x, newCoordinates[i].y, newCoordinates[i + 1].x, newCoordinates[i + 1].y, 1));
            }
            mergedShape.edgeEntities.add(new EdgeEntity(count++, newCoordinates[newCoordinates.length - 1].x, newCoordinates[newCoordinates.length - 1].y, newCoordinates[0].x, newCoordinates[0].y, 1));
            if (getShapeArea(shapeEntityA) + getShapeArea(newShapeEntityB) == getShapeArea(mergedShape)) {
//                ObjectMapper mapper = new ObjectMapper();
//                ShapeEntity[] shapeEntities = {shapeEntityA, newShapeEntityB, mergedShape};
//                try {
//                    System.out.println(mapper.writeValueAsString(shapeEntities));
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
                return mergedShape;
            }
        }
        return null;
    }

    Point mirror(Point p, double x0, double y0, double x1, double y1) {
        double dx, dy, a, b;
        double x2, y2;
        Point p1; //reflected point to be returned
        dx = x1 - x0;
        dy = y1 - y0;
        a = (dx * dx - dy * dy) / (dx * dx + dy * dy);
        b = 2 * dx * dy / (dx * dx + dy * dy);
        x2 = a * (p.x - x0) + b * (p.y - y0) + x0;
        y2 = b * (p.x - x0) - a * (p.y - y0) + y0;
        p1 = new Point(x2, y2);
        return p1;
    }

    ShapeEntity flipX(ShapeEntity shapeEntity, EdgeEntity edgeEntity) {
        ShapeEntity result = flipShape(shapeEntity, edgeEntity.startX, edgeEntity.startY, edgeEntity.endX, edgeEntity.endY);
        return result;
    }

    ShapeEntity flipY(ShapeEntity shapeEntity, EdgeEntity edgeEntity) {
        double centerX = (edgeEntity.startX + edgeEntity.endX) / 2;
        double centerY = (edgeEntity.startY + edgeEntity.endY) / 2;
        double a = -(edgeEntity.startY - edgeEntity.endY);
        double b = edgeEntity.startX - edgeEntity.endX;
        double x1 = centerX + a;
        double y1 = centerY + b;
        ShapeEntity result = flipShape(shapeEntity, centerX, centerY, x1, y1);
        return result;
    }

    ShapeEntity flipXY(ShapeEntity shapeEntity, EdgeEntity edgeEntity) {
        ShapeEntity result = flipX(shapeEntity, edgeEntity);
        result = flipY(shapeEntity, edgeEntity);
        return result;
    }

    ShapeEntity flipShape(ShapeEntity shape, double x0, double y0, double x1, double y1) {
        Cloner cloner = new Cloner();
        ShapeEntity shapeEntity = cloner.deepClone(shape);
        for (EdgeEntity edgeEntity : shapeEntity.edgeEntities) {
            Point p = new Point(edgeEntity.startX, edgeEntity.startY);
            Point mirror = mirror(p, x0, y0, x1, y1);
            edgeEntity.startX = mirror.x;
            edgeEntity.startY = mirror.y;
            Point p1 = new Point(edgeEntity.endX, edgeEntity.endY);
            p1 = mirror(p1, x0, y0, x1, y1);
            edgeEntity.endX = p1.x;
            edgeEntity.endY = p1.y;
        }
        return shapeEntity;
    }

    double getShapeArea(ShapeEntity shapeEntity) {
        List<EdgeEntity> edgeEntityListA = shapeEntity.edgeEntities;
        List<Coordinate> coordinateListA = edgeEntityListA.parallelStream().map(edgeEntity ->
                new Coordinate(edgeEntity.startX, edgeEntity.startY)
        ).collect(Collectors.toList());
        coordinateListA.add(new Coordinate(edgeEntityListA.get(0).startX, edgeEntityListA.get(0).startY));
        Polygon p1 = new GeometryFactory().createPolygon(coordinateListA.toArray(new Coordinate[coordinateListA.size()]));
        return p1.getArea();
// calculate union
    }

    boolean isSameEdge(EdgeEntity edgeEntityA, EdgeEntity edgeEntityB) {
        if (edgeEntityA.startX == edgeEntityB.startX && edgeEntityA.startY == edgeEntityB.startY) {
            if (edgeEntityA.endX == edgeEntityB.endX && edgeEntityA.endY == edgeEntityB.endY) {
                return true;
            }
        } else if (edgeEntityA.startX == edgeEntityB.endX && edgeEntityA.startY == edgeEntityB.endY) {
            if (edgeEntityA.endX == edgeEntityB.startX && edgeEntityA.endY == edgeEntityB.startY) {
                return true;
            }
        }
        return false;
    }

//    boolean arePolygonsOverlapped(ShapeEntity shapeEntityA, ShapeEntity shapeEntityB) {
//        for (EdgeEntity edgeEntityA : shapeEntityA.edgeEntities
//                ) {
//            for (EdgeEntity edgeEntityB : shapeEntityB.edgeEntities
//                    ) {
//                Point intersect = findIntersectPoint(edgeEntityA, edgeEntityB);
//                if (intersect != null) {
//                    if (!(intersect.x == edgeEntityA.startX && intersect.y == edgeEntityA.startY)
//                            && !(intersect.x == edgeEntityB.startX && intersect.y == edgeEntityB.startY)
//                            && !(intersect.x == edgeEntityA.endX && intersect.y == edgeEntityA.endY)
//                            && !(intersect.x == edgeEntityB.endX && intersect.y == edgeEntityB.endY)
//                            && Line2D.linesIntersect(
//                            edgeEntityA.startX.floatValue(), edgeEntityA.startY.floatValue(),
//                            edgeEntityA.endX.floatValue(), edgeEntityA.endY.floatValue(),
//                            edgeEntityB.startX.floatValue(), edgeEntityB.startY.floatValue(),
//                            edgeEntityB.endX.floatValue(), edgeEntityB.endY.floatValue())) {
//                        System.out.println("Cat nhau");
////                    System.out.println(intersect.x);
////                    System.out.println(intersect.y);
////                    System.out.println(edgeEntityA.edgeId);
////                    System.out.println(edgeEntityB.edgeId);
////                    ShapeEntity[] shapeEntities = {shapeEntityA, shapeEntityB};
////                    ObjectMapper mapper = new ObjectMapper();
////                    try {
////                        System.out.println(mapper.writeValueAsString(shapeEntities));
////                    } catch (JsonProcessingException e) {
////                        e.printStackTrace();
////                    }
////                    System.out.println("-------------------");
//                        return false;
//                    }
//                } else {
//
//                }
//            }
//        }
//        return true;
//    }

    EdgeEntity rotate90(EdgeEntity edgeEntity) {
        double temp = edgeEntity.startY;
        edgeEntity.startY = edgeEntity.startX;
        edgeEntity.startX = temp;
        temp = edgeEntity.endY;
        edgeEntity.endY = edgeEntity.endX;
        edgeEntity.endX = temp;
        return edgeEntity;
    }

    double squareLengthOfEdge(EdgeEntity edgeEntity) {
        return (Math.pow((edgeEntity.endY - edgeEntity.startY), 2) + Math.pow((edgeEntity.endX - edgeEntity.startX), 2));
    }

    void PrintShape(int id) {
        ShapeEntity shapeEntity = shapeService.get(id);
        shapeEntity.edgeEntities.forEach(edgeEntity -> {
            System.out.println(
            );
        });
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
