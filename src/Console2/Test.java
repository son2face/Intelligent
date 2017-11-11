package Console2;

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
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.AssertionFailedException;

import java.sql.Timestamp;
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
    int minEgde = 100;
    int idShape = 1000;
    int widthFrame = 25;
    int heighFrame = 25;
    TreeShape overTreeShape = new TreeShape(-1, null);
    //    TreeShape currentTree = treeShape;
    boolean isAssert = false;
    boolean isCenterEmpty = false;

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
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        Test test = new Test();
        Timestamp pre = new Timestamp(System.currentTimeMillis());
        test.Process(11);
        Timestamp last = new Timestamp(System.currentTimeMillis());
//        ShapeEntity shapeEntity = test.shapeService.get(212);
//        for(int i = 0; i  < shapeEntity.edgeEntities.size(); i++){
//            System.out.println(test.computeAngle(shapeEntity, i));
//            System.out.println(test.isConvex(shapeEntity,i));
//        }
        System.out.println("Done!");
        System.out.println(pre);
        System.out.println(last);
    }

    void findALlOddAngel() {

    }

    double computeAngle(ShapeEntity shapeEntity, int index) {
        int index1 = (index + 1) % shapeEntity.edgeEntities.size();
        double c = Math.pow((shapeEntity.edgeEntities.get(index1).endX - shapeEntity.edgeEntities.get(index).startX), 2) +
                Math.pow((shapeEntity.edgeEntities.get(index1).endY - shapeEntity.edgeEntities.get(index).startY), 2);
        double b = Math.pow((shapeEntity.edgeEntities.get(index1).endX - shapeEntity.edgeEntities.get(index).endX), 2) +
                Math.pow((shapeEntity.edgeEntities.get(index1).endY - shapeEntity.edgeEntities.get(index).endY), 2);
        double a = Math.pow((shapeEntity.edgeEntities.get(index).startX - shapeEntity.edgeEntities.get(index).endX), 2) +
                Math.pow((shapeEntity.edgeEntities.get(index).startY - shapeEntity.edgeEntities.get(index).endY), 2);
        int degree = (int) (Math.acos((a + b - c) / Math.sqrt(4 * a * b)) * 180 / Math.PI);
        if (isConvex(shapeEntity, index)) {
            return degree;
        }
        return 360 - degree;
    }

    boolean isConvex(ShapeEntity shapeEntity, int index) {
        Cloner cloner = new Cloner();
        ObjectMapper mapper = new ObjectMapper();
        List<EdgeEntity> edgeEntityList = cloner.deepClone(shapeEntity.edgeEntities);
        Point point = new GeometryFactory().createPoint(new Coordinate(edgeEntityList.get(index).endX, edgeEntityList.get(index).endY));
        edgeEntityList.get(index).endX = edgeEntityList.get((index + 1) % edgeEntityList.size()).endX;
        edgeEntityList.get(index).endY = edgeEntityList.get((index + 1) % edgeEntityList.size()).endY;
        edgeEntityList.remove((index + 1) % edgeEntityList.size());
        List<Coordinate> coordinateListB = edgeEntityList.parallelStream().map(edgeEntity ->
                new Coordinate(edgeEntity.startX, edgeEntity.startY)
        ).collect(Collectors.toList());
        coordinateListB.add(new Coordinate(edgeEntityList.get(edgeEntityList.size() - 1).endX, edgeEntityList.get(edgeEntityList.size() - 1).endY));
        coordinateListB.add(new Coordinate(edgeEntityList.get(0).startX, edgeEntityList.get(0).startY));
        Polygon p2 = new GeometryFactory().createPolygon(coordinateListB.toArray(new Coordinate[coordinateListB.size()]));
        return !p2.contains(point);
    }

    List<ShapeEntity> Smooth(List<ShapeEntity> shapeEntities) {
        Cloner cloner = new Cloner();
        List<ShapeEntity> result = null;
//        shapeEntities.
        return cloner.deepClone(result);

    }


//    boolean call(List<ShapeEntity> shapeEntities, List<PairShape> pairShapes) {
//        ObjectMapper mapper = new ObjectMapper();
//        if (shapeEntities.size() == 1) {
//            try {
////                List<Coordinate> coordinateListB = shapeEntities.get(0).edgeEntities.parallelStream().map(edgeEntity ->
////                        new Coordinate(edgeEntity.startX, edgeEntity.startY)
////                ).collect(Collectors.toList());
////                coordinateListB.add(new Coordinate(shapeEntities.get(0).edgeEntities.get(shapeEntities.get(0).edgeEntities.size() - 1).endX, shapeEntities.get(0).edgeEntities.get(shapeEntities.get(0).edgeEntities.size() - 1).endY));
////                coordinateListB.add(new Coordinate(shapeEntities.get(0).edgeEntities.get(0).startX, shapeEntities.get(0).edgeEntities.get(0).startY));
////                Polygon p2 = new GeometryFactory().createPolygon(coordinateListB.toArray(new Coordinate[coordinateListB.size()]));
////                if (p2.isRectangle()) {
////                    System.out.println(mapper.writeValueAsString(shapeEntities));
////                    System.out.println();
////                    System.out.println();
////                    return true;
//                System.out.println(mapper.writeValueAsString(shapeEntities));
//                System.out.println();
//                System.out.println();
//                return true;
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//        Cloner cloner = new Cloner();
//        int shapeSize = shapeEntities.size();
////        List<ShapeEntity> clone = cloner.deepClone(shapeEntities);
//        List<ShapeEntity> clone = new ArrayList<>();
//        for (int i = 0; i < shapeSize - 1; i++) {
//            for (int j = i + 1; j < shapeSize; j++) {
//                ShapeEntity shapeA = shapeEntities.get(i);
//                ShapeEntity shapeB = shapeEntities.get(j);
//                List<ShapeEntity> shapeListT = combineShape(shapeA, shapeB);
//                clone.addAll(shapeListT.parallelStream().filter(shapeEntity -> {
//                    if (isOutOfRange(shapeEntity)) {
//                        return false;
//                    } else {
//                        return true;
//                    }
//                }).collect(Collectors.toList()));
//            }
//        }
//        clone = sort(clone);
//        List<PairShape> newPairShapes = new ArrayList<>();
//        for (int i = clone.size() - 1; i >= 0; i--) {
//            ShapeEntity shapeEntity = clone.get(i);
//            List<ShapeEntity> list = cloner.deepClone(shapeEntities);
//            boolean check = !pairShapes.parallelStream().anyMatch(pairShape -> {
//                if (isSameShape(pairShape.a, shapeEntity.combineA) && isSameShape(pairShape.b, shapeEntity.combineB)) {
//                    return true;
//                }
//                if (isSameShape(pairShape.a, shapeEntity.combineB) && isSameShape(pairShape.b, shapeEntity.combineA)) {
//                    return true;
//                }
//                return false;
//            });
//            if (check) {
//                list = list.parallelStream().filter(shapeEntity1 -> {
//                    if (isSameShape(shapeEntity1, shapeEntity.combineA)) {
//                        return false;
//                    } else if (isSameShape(shapeEntity1, shapeEntity.combineB)) {
//                        return false;
//                    }
//                    return true;
//                }).collect(Collectors.toList());
//                list.add(shapeEntity);
//                if (call(list, newPairShapes) == true) {
//                    return true;
//                }
//                newPairShapes.add(new PairShape(shapeEntity.combineA, shapeEntity.combineB));
//            }
//        }
//        return false;
//    }

    void Process(int problemId) {
        ObjectMapper mapper = new ObjectMapper();
        Cloner cloner = new Cloner();
        ProblemEntity problemEntity = problemService.get(problemId);
        int shapeSize = problemEntity.shapeEntities.size();
        problemEntity.shapeEntities.parallelStream().forEach(shapeEntity -> {
            shapeEntity.edgeEntities = shapeService.get(shapeEntity.shapeId).edgeEntities;
        });
        for (int i = 0; i < shapeSize - 1; i++) {
            List<PairShape> clone = new ArrayList<>();
            for (int j = i + 1; j < shapeSize; j++) {
                ShapeEntity shapeA = problemEntity.shapeEntities.get(i);
                ShapeEntity shapeB = problemEntity.shapeEntities.get(j);
                List<PairShape> pairShapes = combineShape(shapeA, shapeB);
                clone.addAll(pairShapes.parallelStream().filter(pairShape -> {
                    if (isOutOfRange(pairShape.ab)) {
                        return false;
                    } else {
                        return true;
                    }
                }).collect(Collectors.toList()));
            }
            clone = sort(clone);
            for (int j = clone.size() - 1; j >= 0; j--) {
                List<ShapeEntity> list = cloner.deepClone(problemEntity.shapeEntities);
                PairShape pairShape = clone.get(j);
                list = list.parallelStream().filter(shapeEntity1 -> {
                    if (isSameShape(shapeEntity1, pairShape.a)) {
                        return false;
                    } else if (isSameShape(shapeEntity1, pairShape.b)) {
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());
                pairShape.ListShape = list;
                TreeShape tree = new TreeShape(pairShape.a.shapeId, overTreeShape);
                if (call2(pairShape, tree)) {
                    break;
                }
                if (isCenterEmpty || isAssert) {
                    isCenterEmpty = false;
                    isAssert = false;
                } else {
                    overTreeShape.childs.put(pairShape.a.shapeId, tree);
                }
            }
        }
    }

    boolean call2(PairShape pairShape, TreeShape treeShape) {
        Cloner cloner = new Cloner();
        List<ShapeEntity> shapeEntities = pairShape.ListShape;
        ObjectMapper mapper = new ObjectMapper();
        if (shapeEntities.size() == 0) {
            try {
                System.out.println(mapper.writeValueAsString(pairShape.ab));
                System.out.println();
                System.out.println();
                return true;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        TreeShape temp = treeShape;
        while (temp.parent != null) {
            temp = temp.parent;
            if (temp.childs.containsKey(pairShape.b.shapeId)) {
                return false;
            }
        }
        int shapeSize = shapeEntities.size();
        List<PairShape> clone = new ArrayList<>();
        ShapeEntity shapeCompare = pairShape.ab;
        for (int i = 0; i < shapeSize; i++) {
            ShapeEntity shapeB = shapeEntities.get(i);
            List<PairShape> pairShapes = combineShape(shapeCompare, shapeB);
            clone.addAll(pairShapes.parallelStream().filter(pair -> {
                if (isOutOfRange(pair.ab)) {
                    return false;
                } else {
                    return true;
                }
            }).collect(Collectors.toList()));
            clone = sort(clone);
            for (int j = clone.size() - 1; j >= 0; j--) {
                List<ShapeEntity> list = cloner.deepClone(shapeEntities);
                PairShape pair = clone.get(j);
                list = list.parallelStream().filter(shapeEntity1 -> {
                    if (isSameShape(shapeEntity1, pair.b)) {
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());
                pair.ListShape = list;
                TreeShape tree = new TreeShape(pair.b.shapeId, treeShape);
                if (call2(pair, tree)) {
                    return true;
                }
                if (isCenterEmpty || isAssert) {
                    isCenterEmpty = false;
                    isAssert = false;
                } else {
                    treeShape.childs.put(pair.b.shapeId, tree);
                }
            }
        }
//        treeShape.childs.put(pairShape.b.shapeId, new TreeShape(pairShape.b.shapeId, treeShape));
        return false;
    }

    boolean isOutOfRange(ShapeEntity shapeEntity) {
        double minX = 9999;
        double minY = 9999;
        double maxX = 0;
        double maxY = 0;
        for (EdgeEntity edgeEntity : shapeEntity.edgeEntities) {
            minX = Math.min(minX, edgeEntity.startX);
            maxX = Math.max(maxX, edgeEntity.startX);
            minY = Math.min(minY, edgeEntity.startY);
            maxY = Math.max(maxY, edgeEntity.startY);
        }
        double width = maxX - minX;
        double height = maxY - minY;
        double maxFrame = Math.max(widthFrame, heighFrame);
        double minFrame = Math.min(widthFrame, heighFrame);
        if (width <= maxFrame && height <= maxFrame) {
            if (width > minFrame && height > minFrame) {
                return true;
            } else return false;
        }
        return true;
    }

    List<PairShape> combineShape(ShapeEntity shapeEntityA, ShapeEntity shapeEntityB) {
        ObjectMapper mapper = new ObjectMapper();
        Cloner cloner = new Cloner();
        List<PairShape> result = new ArrayList<>();
        List<List<PairShape>> listList =
                (shapeEntityA.edgeEntities.parallelStream().map(edgeEntityA -> {
                    List<PairShape> res = new ArrayList<>();
                    shapeEntityB.edgeEntities.forEach(edgeEntityBB -> {
//                        ShapeEntity shapeEntity = edgeEntityB;
                        if (squareLengthOfEdge(edgeEntityA) == squareLengthOfEdge(edgeEntityBB)) {
                            ShapeEntity newShapeEntityB = cloner.deepClone(shapeEntityB);
                            ShapeEntity flipXShape = null;
                            ShapeEntity flipYShape = null;
                            ShapeEntity flipXYShape = null;
                            EdgeEntity edgeEntityBBB = cloner.deepClone(edgeEntityBB);
                            if (findIntersectPoint(edgeEntityA, edgeEntityBBB) != null) {
                                edgeEntityBBB = rotateEdge90(edgeEntityBBB);
                                if (findIntersectPoint(edgeEntityA, edgeEntityBBB) != null) return;
                                else {
                                    newShapeEntityB = rotateShape90(newShapeEntityB);
                                    flipXShape = flipX(newShapeEntityB, edgeEntityBBB);
                                    flipYShape = flipY(newShapeEntityB, edgeEntityBBB);
                                    flipXYShape = flipXY(newShapeEntityB, edgeEntityBBB);
//                                    ShapeEntity[] shapeEntities = {shapeEntityB, newShapeEntityB, flipXShape, flipYShape, flipXYShape};
//                                    try {
//                                        System.out.println(mapper.writeValueAsString(shapeEntities));
//                                    } catch (JsonProcessingException e) {
//                                        e.printStackTrace();
//                                    }
                                }
                            } else {
                                flipXShape = flipX(newShapeEntityB, edgeEntityBBB);
                                flipYShape = flipY(newShapeEntityB, edgeEntityBBB);
                                flipXYShape = flipXY(newShapeEntityB, edgeEntityBBB);
                            }

                            ShapeEntity newShape = mergeShape(shapeEntityA, newShapeEntityB, edgeEntityA, edgeEntityBBB);
                            ShapeEntity mergedFlipXShape = mergeShape(shapeEntityA, flipXShape, edgeEntityA, edgeEntityBBB);
                            ShapeEntity mergedFlipYShape = mergeShape(shapeEntityA, flipYShape, edgeEntityA, edgeEntityBBB);
                            ShapeEntity mergedFlipXYShape = mergeShape(shapeEntityA, flipXYShape, edgeEntityA, edgeEntityBBB);
                            if (newShape != null) {
                                newShape = (joinShapeLines(newShape));
                                PairShape pairShape = new PairShape(shapeEntityA, newShapeEntityB, newShape);
                                res.add(pairShape);
                            }
                            if (mergedFlipXShape != null) {
                                mergedFlipXShape = (joinShapeLines(mergedFlipXShape));
                                if (!isShapeInList(res, mergedFlipXShape)) {
                                    PairShape pairShape = new PairShape(shapeEntityA, newShapeEntityB, mergedFlipXShape);
                                    res.add(pairShape);
                                }
                            }
                            if (mergedFlipYShape != null) {
                                mergedFlipYShape = (joinShapeLines(mergedFlipYShape));
                                if (!isShapeInList(res, mergedFlipYShape)) {
                                    PairShape pairShape = new PairShape(shapeEntityA, newShapeEntityB, mergedFlipYShape);
                                    res.add(pairShape);
                                }
                            }
                            if (mergedFlipXYShape != null) {
                                mergedFlipXYShape = (joinShapeLines(mergedFlipXYShape));
                                if (!isShapeInList(res, mergedFlipXYShape)) {
                                    PairShape pairShape = new PairShape(shapeEntityA, newShapeEntityB, mergedFlipXYShape);
                                    res.add(pairShape);
                                }
                            }

                        }
                    });
                    return res;
                }).collect(Collectors.toList()));
        for (int i = 0; i < listList.size(); i++) {
            result.addAll(listList.get(i));
        }
        return result;
    }

    boolean isShapeInList(List<PairShape> pairShapes, ShapeEntity shape) {
        return pairShapes.parallelStream().anyMatch(pairShape -> {
            try {
                if (isSameShape(pairShape.ab, shape)) return true;
            } catch (TopologyException e) {
                return false;
            }
            ;
            return false;
        });
    }

    boolean isSameShape(ShapeEntity shapeA, ShapeEntity shapeB) {
        List<Coordinate> coordinateListA = shapeA.edgeEntities.parallelStream().map(edgeEntity ->
                new Coordinate(edgeEntity.startX, edgeEntity.startY)
        ).collect(Collectors.toList());
        coordinateListA.add(new Coordinate(shapeA.edgeEntities.get(shapeA.edgeEntities.size() - 1).endX, shapeA.edgeEntities.get(shapeA.edgeEntities.size() - 1).endY));
        coordinateListA.add(new Coordinate(shapeA.edgeEntities.get(0).startX, shapeA.edgeEntities.get(0).startY));
        List<Coordinate> coordinateListB = shapeB.edgeEntities.parallelStream().map(edgeEntity ->
                new Coordinate(edgeEntity.startX, edgeEntity.startY)
        ).collect(Collectors.toList());
        coordinateListB.add(new Coordinate(shapeB.edgeEntities.get(shapeB.edgeEntities.size() - 1).endX, shapeB.edgeEntities.get(shapeB.edgeEntities.size() - 1).endY));
        coordinateListB.add(new Coordinate(shapeB.edgeEntities.get(0).startX, shapeB.edgeEntities.get(0).startY));
        Polygon p1 = new GeometryFactory().createPolygon(coordinateListA.toArray(new Coordinate[coordinateListA.size()]));
        Polygon p2 = new GeometryFactory().createPolygon(coordinateListB.toArray(new Coordinate[coordinateListB.size()]));
        boolean result = false;
        try {
            result = p1.equals(p2);
        } catch (Exception e) {
//            ObjectMapper mapper = new ObjectMapper();
//            ShapeEntity[] list = {shapeA,shapeB};
//            try {
//                System.out.println(mapper.writeValueAsString(list));
//            } catch (JsonProcessingException e1) {
//                e1.printStackTrace();
//            }
//            e.printStackTrace();
            result = true;
        }
        return result;
    }

    ShapeEntity joinShapeLines(ShapeEntity shapeEntity) {
        Cloner cloner = new Cloner();
        ShapeEntity clone = cloner.deepClone(shapeEntity);
        clone.edgeEntities = new ArrayList<>();
        Integer startPoint = null;
        int size = shapeEntity.edgeEntities.size();
        for (int i = 0; i < size; i++) {
            if (findIntersectPoint(shapeEntity.edgeEntities.get(i), shapeEntity.edgeEntities.get((i + 1) % size)) != null) {
                startPoint = (i + 1) % size;
                break;
            }
        }
        if (startPoint != null) {
            for (int i = 0; i < size; i++) {
                int k = i + startPoint;
                if (findIntersectPoint(shapeEntity.edgeEntities.get(k % size), shapeEntity.edgeEntities.get((k + 1) % size)) == null) {
                    for (int j = 0; j < size; j++) {
                        if (findIntersectPoint(shapeEntity.edgeEntities.get((k + j + 1) % size), shapeEntity.edgeEntities.get((k + j + 2) % size)) != null) {
                            EdgeEntity edgeToClone = shapeEntity.edgeEntities.get(k % size);
                            EdgeEntity edgeEntity = cloner.deepClone(edgeToClone);
//                            edgeEntity.startX = edgeEntity.endX;
//                            edgeEntity.startY = edgeEntity.endY;
                            edgeEntity.endX = shapeEntity.edgeEntities.get((k + 1 + j) % size).endX;
                            edgeEntity.endY = shapeEntity.edgeEntities.get((k + 1 + j) % size).endY;
                            clone.edgeEntities.add(edgeEntity);
                            i += (j + 1);
                            break;
                        }
                    }
                } else {
                    clone.edgeEntities.add(cloner.deepClone(shapeEntity.edgeEntities.get(k % size)));
                }
            }
            startPoint--;
            int endPoint = (startPoint + 2 * size) % size;
            startPoint--;
            boolean check = false;
            EdgeEntity edgeEndPointEntity = null;
            for (int i = 0; i < size; i++) {
                int k = startPoint - i + 2 * size;
                if (findIntersectPoint(shapeEntity.edgeEntities.get(endPoint), shapeEntity.edgeEntities.get(k % size)) == null) {
                    edgeEndPointEntity = shapeEntity.edgeEntities.get(k % size);
                    check = true;
                } else {
                    break;
                }
            }
            if (check) {
                EdgeEntity edgeEntity = cloner.deepClone(shapeEntity.edgeEntities.get(endPoint));
                clone.edgeEntities.remove(clone.edgeEntities.size() - 1);
                edgeEndPointEntity = cloner.deepClone(edgeEndPointEntity);
                EdgeEntity edgeEntity1 = clone.edgeEntities.get(clone.edgeEntities.size() - 1);
                edgeEntity.startX = edgeEndPointEntity.startX;
                edgeEntity.startY = edgeEndPointEntity.startY;
                edgeEndPointEntity.startX = edgeEntity1.endX;
                edgeEndPointEntity.startY = edgeEntity1.endY;
                clone.edgeEntities.add(edgeEndPointEntity);
            }

        }
        return clone;
    }

    Console.Point findIntersectPoint(EdgeEntity edgeEntityA, EdgeEntity edgeEntityB) {
        double a1 = edgeEntityA.endY - edgeEntityA.startY;
        double b1 = edgeEntityA.startX - edgeEntityA.endX;
        double c1 = a1 * (edgeEntityA.startX) + b1 * (edgeEntityA.startY);
        double a2 = edgeEntityB.endY - edgeEntityB.startY;
        double b2 = edgeEntityB.startX - edgeEntityB.endX;
        double c2 = a2 * (edgeEntityB.startX) + b2 * (edgeEntityB.startY);
        double determinant = a1 * b2 - a2 * b1;
        if (determinant == 0) {
            return null;
        } else {
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;
            return new Console.Point(x, y);
        }

    }

    boolean isOnTheSameLine(EdgeEntity edgeEntityA, EdgeEntity edgeEntityB) {
        double a1 = edgeEntityA.endY - edgeEntityA.startY;
        double b1 = edgeEntityA.startX - edgeEntityA.endX;
        double c1 = a1 * (edgeEntityA.startX) + b1 * (edgeEntityA.startY);
        double a2 = edgeEntityB.endY - edgeEntityB.startY;
        double b2 = edgeEntityB.startX - edgeEntityB.endX;
        double c2 = a2 * (edgeEntityB.startX) + b2 * (edgeEntityB.startY);
        double determinant = a1 * b2 - a2 * b1;
        if (determinant == 0) {
            return c1 == c2;
        }
        return false;
    }

    ShapeEntity translate(ShapeEntity shapeEntity, EdgeEntity edgeEntityA, EdgeEntity edgeEntityB) {
        Cloner cloner = new Cloner();
        ShapeEntity clone = cloner.deepClone(shapeEntity);
        if (findIntersectPoint(edgeEntityA, edgeEntityB) != null) {
//            System.out.println("Khong dich chuyen duoc");
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
        ObjectMapper mapper = new ObjectMapper();
        if (newShapeEntityB != null) {
            ShapeEntity mergedShape = cloner.deepClone(shapeEntityA);
            boolean check = true;
            for (int i = 0; i < shapeEntityA.edgeEntities.size(); i++) {
                EdgeEntity aEdgeEntity = shapeEntityA.edgeEntities.get(i);
                if (isSameEdge(aEdgeEntity, edgeEntityA)) {
                    int positionEdgeEntityA = i;
                    for (i = 0; i < newShapeEntityB.edgeEntities.size(); i++) {
                        EdgeEntity bEdgeEntity = newShapeEntityB.edgeEntities.get(i);
                        if (isSameEdge(bEdgeEntity, edgeEntityA)) {
                            int positionEdgeEntityB = i;
                            int sizeA = shapeEntityA.edgeEntities.size();
                            int sizeB = newShapeEntityB.edgeEntities.size();
                            int pre = 0;
                            int next = 0;
                            for (i = 0; i < sizeA; i++) {
                                aEdgeEntity = shapeEntityA.edgeEntities.get((i + positionEdgeEntityA + 1) % sizeA);
                                bEdgeEntity = newShapeEntityB.edgeEntities.get((i + positionEdgeEntityB + 1) % sizeB);
                                if (isSameEdge(aEdgeEntity, bEdgeEntity)) {
                                    next++;
                                } else {
                                    if (findIntersectPoint(aEdgeEntity, bEdgeEntity) == null) {
                                        next++;
                                    }
                                    break;
                                }
                            }
                            for (i = 0; i < sizeA; i++) {
                                aEdgeEntity = shapeEntityA.edgeEntities.get((positionEdgeEntityA - i - 1) % sizeA);
                                bEdgeEntity = newShapeEntityB.edgeEntities.get((positionEdgeEntityB - i - 1) % sizeB);
                                if (isSameEdge(aEdgeEntity, bEdgeEntity)) {
                                    pre++;
                                } else {
                                    if (findIntersectPoint(aEdgeEntity, bEdgeEntity) == null) {
                                        pre++;
                                    }
                                    break;
                                }
                            }
                            int weight = next + pre + 1;
                            for (i = 0; i < sizeA - weight; i++) {
                                aEdgeEntity = shapeEntityA.edgeEntities.get((i + next + positionEdgeEntityA + 1) % sizeA);
                                for (int j = 0; j < sizeB - weight; j++) {
                                    bEdgeEntity = newShapeEntityB.edgeEntities.get((j + next + positionEdgeEntityB + 1) % sizeB);
                                    if (isOnTheSameLine(aEdgeEntity, bEdgeEntity)) {
                                        return null;
                                    }
                                }
                            }
                            mergedShape.weight = weight;
                            break;
                        }
                    }
                    break;
                }
            }
            if (check) {
                List<EdgeEntity> edgeEntityListA = shapeEntityA.edgeEntities;
                List<EdgeEntity> edgeEntityListB = newShapeEntityB.edgeEntities;
                List<Coordinate> coordinateListA = edgeEntityListA.parallelStream().map(edgeEntity ->
                        new Coordinate(edgeEntity.startX, edgeEntity.startY)
                ).collect(Collectors.toList());
                coordinateListA.add(new Coordinate(edgeEntityListA.get(edgeEntityListA.size() - 1).endX, edgeEntityListA.get(edgeEntityListA.size() - 1).endY));
                coordinateListA.add(new Coordinate(edgeEntityListA.get(0).startX, edgeEntityListA.get(0).startY));
                List<Coordinate> coordinateListB = edgeEntityListB.parallelStream().map(edgeEntity ->
                        new Coordinate(edgeEntity.startX, edgeEntity.startY)
                ).collect(Collectors.toList());
                coordinateListB.add(new Coordinate(edgeEntityListB.get(edgeEntityListB.size() - 1).endX, edgeEntityListB.get(edgeEntityListB.size() - 1).endY));
                coordinateListB.add(new Coordinate(edgeEntityListB.get(0).startX, edgeEntityListB.get(0).startY));
                Polygon p1 = new GeometryFactory().createPolygon(coordinateListA.toArray(new Coordinate[coordinateListA.size()]));
                Polygon p2 = new GeometryFactory().createPolygon(coordinateListB.toArray(new Coordinate[coordinateListB.size()]));
                try {
                    Geometry union = p1.union(p2);
                    Coordinate[] newCoordinates = union.getCoordinates();
                    int count = 1;
                    mergedShape.edgeEntities = new ArrayList<EdgeEntity>();
                    for (int i = 0; i < newCoordinates.length - 1; i++) {
                        mergedShape.edgeEntities.add(new EdgeEntity(count++, newCoordinates[i].x, newCoordinates[i].y, newCoordinates[i + 1].x, newCoordinates[i + 1].y, 1));
                    }
//                mergedShape.edgeEntities.add(new EdgeEntity(count++, newCoordinates[newCoordinates.length - 1].x, newCoordinates[newCoordinates.length - 1].y, newCoordinates[0].x, newCoordinates[0].y, 1));
//                if (getShapeArea(mergedShape) != union.getArea()) {
//                    System.out.println("Out!");
//                }
                    if (getShapeArea(shapeEntityA) + getShapeArea(newShapeEntityB) == getShapeArea(mergedShape)) {
//                ObjectMapper mapper = new ObjectMapper();
//                ShapeEntity[] shapeEntities = {shapeEntityA, newShapeEntityB, mergedShape};
//                try {
//                    System.out.println(mapper.writeValueAsString(shapeEntities));
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
                        mergedShape.shapeId = idShape++;
                        return mergedShape;
                    } else {
                        isCenterEmpty = true;
                        return null;
                    }
                } catch (TopologyException e) {
                    return null;
                } catch (AssertionFailedException e) {
                    System.out.println("Assert Test");
                    isAssert = true;
//                    ShapeEntity[] s = {shapeEntityA,shapeEntityB};
//                    try {
//                        System.out.println(mapper.writeValueAsString(s));
//                        e.printStackTrace();
//                    } catch (JsonProcessingException e1) {
//                        e1.printStackTrace();
//                    }
                    return null;
                }
            }
        }
        return null;
    }

    Console.Point mirror(Console.Point p, double x0, double y0, double x1, double y1) {
        double dx, dy, a, b;
        double x2, y2;
        Console.Point p1; //reflected point to be returned
        dx = x1 - x0;
        dy = y1 - y0;
        a = (dx * dx - dy * dy) / (dx * dx + dy * dy);
        b = 2 * dx * dy / (dx * dx + dy * dy);
        x2 = a * (p.x - x0) + b * (p.y - y0) + x0;
        y2 = b * (p.x - x0) - a * (p.y - y0) + y0;
        p1 = new Console.Point(x2, y2);
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
        result = flipY(result, edgeEntity);
        return result;
    }

    ShapeEntity flipShape(ShapeEntity shape, double x0, double y0, double x1, double y1) {
        Cloner cloner = new Cloner();
        ShapeEntity shapeEntity = cloner.deepClone(shape);
        shapeEntity.edgeEntities.parallelStream().forEach(edgeEntity -> {
            Console.Point p = new Console.Point(edgeEntity.startX, edgeEntity.startY);
            Console.Point mirror = mirror(p, x0, y0, x1, y1);
            edgeEntity.startX = mirror.x;
            edgeEntity.startY = mirror.y;
            Console.Point p1 = new Console.Point(edgeEntity.endX, edgeEntity.endY);
            p1 = mirror(p1, x0, y0, x1, y1);
            edgeEntity.endX = p1.x;
            edgeEntity.endY = p1.y;
        });
        return shapeEntity;
    }

    double getShapeArea(ShapeEntity shapeEntity) {
        List<EdgeEntity> edgeEntityListA = shapeEntity.edgeEntities;
        List<Coordinate> coordinateListA = edgeEntityListA.parallelStream().map(edgeEntity ->
                new Coordinate(edgeEntity.startX, edgeEntity.startY)
        ).collect(Collectors.toList());
        coordinateListA.add(new Coordinate(edgeEntityListA.get(edgeEntityListA.size() - 1).endX, edgeEntityListA.get(edgeEntityListA.size() - 1).endY));
        coordinateListA.add(new Coordinate(edgeEntityListA.get(0).startX, edgeEntityListA.get(0).startY));
        Polygon p1 = new GeometryFactory().createPolygon(coordinateListA.toArray(new Coordinate[coordinateListA.size()]));
        return p1.getArea();
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

    double squareLengthOfEdge(EdgeEntity edgeEntity) {
        return (Math.pow((edgeEntity.endY - edgeEntity.startY), 2) + Math.pow((edgeEntity.endX - edgeEntity.startX), 2));
    }

    List<PairShape> sort(List<PairShape> pairShapes) {
        if (pairShapes == null || pairShapes.size() == 0) {
            return pairShapes;
        }
        int number = pairShapes.size();
        quicksort(pairShapes, 0, number - 1);
        return new ArrayList<PairShape>(pairShapes);
    }

    void quicksort(List<PairShape> pairShapes, int low, int high) {
        int i = low, j = high;
        PairShape pivot = pairShapes.get(low + (high - low) / 2);
        while (i <= j) {
            while (pairShapes.get(i).ab.weight > pivot.ab.weight) {
                i++;
            }
            while (pairShapes.get(j).ab.weight < pivot.ab.weight) {
                j--;
            }
            if (i <= j) {
                PairShape temp = pairShapes.get(i);
                pairShapes.set(i, pairShapes.get(j));
                pairShapes.set(j, temp);
                i++;
                j--;
            }
        }
        if (low < j)
            quicksort(pairShapes, low, j);
        if (i < high)
            quicksort(pairShapes, i, high);
    }

    ShapeEntity rotateShape90(ShapeEntity shapeEntity) {
        Cloner cloner = new Cloner();
        shapeEntity.edgeEntities = shapeEntity.edgeEntities.parallelStream().map(edgeEntity -> {
            edgeEntity = cloner.deepClone(edgeEntity);
            return rotateEdge90(edgeEntity);
        }).collect(Collectors.toList());
        return shapeEntity;
    }

    EdgeEntity rotateEdge90(EdgeEntity edgeEntity) {
        edgeEntity.startX = edgeEntity.startX + 1000;
        edgeEntity.startY = edgeEntity.startY + 1000;
        edgeEntity.endX = edgeEntity.endX + 1000;
        edgeEntity.endY = edgeEntity.endY + 1000;
        double temp = edgeEntity.startX;
        edgeEntity.startX = -edgeEntity.startY;
        edgeEntity.startY = temp;
        temp = edgeEntity.endX;
        edgeEntity.endX = -edgeEntity.endY;
        edgeEntity.endY = temp;
        edgeEntity.startX = edgeEntity.startX + 1000;
        edgeEntity.startY = edgeEntity.startY - 1000;
        edgeEntity.endX = edgeEntity.endX + 1000;
        edgeEntity.endY = edgeEntity.endY - 1000;
        return edgeEntity;
    }
}
