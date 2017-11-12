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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
    int idEdge = 10000;
    int widthFrame = 30;
    int heighFrame = 25;
    TreeShape overTreeShape = new TreeShape(null);
    //    TreeShape currentTree = treeShape;
    boolean isAssert = false;
    boolean isCenterEmpty = false;
    ObjectMapper mapper = new ObjectMapper();
    Cloner cloner = new Cloner();

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
        test.Process(7);
        Timestamp last = new Timestamp(System.currentTimeMillis());
        System.out.println("Done!");
        System.out.println(pre);
        System.out.println(last);
    }

    void loadAngles(ShapeEntity shapeEntity) {
        shapeEntity.angles = new ArrayList<>();
        shapeEntity.angles = shapeEntity.edgeEntities.parallelStream().map((edgeEntity -> {
            int index = shapeEntity.edgeEntities.indexOf(edgeEntity);
            return computeAngle(shapeEntity, index);
        })).collect(Collectors.toList());
    }

    boolean isShape90(ShapeEntity shapeEntity) {
        return shapeEntity.angles.parallelStream().allMatch(integer -> integer % 90 == 0);
    }


    int computeAngle(ShapeEntity shapeEntity, int index) {
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

    void Process(int problemId) {
        ProblemEntity problemEntity = problemService.get(problemId);
        widthFrame = problemEntity.width;
        heighFrame = problemEntity.height;
        int shapeSize = problemEntity.shapeEntities.size();
        List<ShapeEntity> shape90Angle = new ArrayList<>();
        List<ShapeEntity> shapeNot90Angle = new ArrayList<>();
        HashMap<Integer, List<PairAngleShape>> hashMap = new HashMap<>();
        problemEntity.shapeEntities.parallelStream().forEach(shapeEntity -> {
            shapeEntity.edgeEntities = shapeService.get(shapeEntity.shapeId).edgeEntities;
            loadAngles(shapeEntity);
            if (isShape90(shapeEntity)) {
                shape90Angle.add(shapeEntity);
            } else {
                shapeNot90Angle.add(shapeEntity);
            }
        });
        edgeBaseProcess(problemEntity.shapeEntities);
    }

    void angleBaseProcess() {

    }

    void edgeBaseProcess(List<ShapeEntity> shapeEntities) {
        int shapeSize = shapeEntities.size();
        for (int i = 0; i < shapeSize - 1; i++) {
            List<PairShape> pairShapeList = new ArrayList<>();
            for (int j = i + 1; j < shapeSize; j++) {
                ShapeEntity shapeA = shapeEntities.get(i);
                ShapeEntity shapeB = shapeEntities.get(j);
                List<PairShape> pairShapes = combineShape(shapeA, shapeB);
                pairShapes = pairShapes.parallelStream().filter(pairShape -> {
                    if (isOutOfRange(pairShape.shapeEntityAB)) {
                        return false;
                    } else {
                        return true;
                    }
                }).collect(Collectors.toList());
                pairShapeList.addAll(pairShapes);
            }
            pairShapeList = sort(pairShapeList);
            for (int j = pairShapeList.size() - 1; j >= 0; j--) {
                List<ShapeEntity> list = cloner.deepClone(shapeEntities);
                PairShape pairShape = pairShapeList.get(j);
                list = list.parallelStream().filter(shapeEntity1 -> {
                    if (isSameShape(shapeEntity1, pairShape.shapeEntityA)) {
                        return false;
                    } else if (isSameShape(shapeEntity1, pairShape.primitiveShapeB)) {
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());
                pairShape.ListShape = list;
                TreeShape tree = new TreeShape(overTreeShape);
                if (call2(pairShape, tree)) {
                    try {
                        System.out.println("," + mapper.writeValueAsString(pairShape.shapeEntityA));
                        System.out.println("," + mapper.writeValueAsString(pairShape.shapeEntityB));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                if (isAssert || isCenterEmpty) {
                    isCenterEmpty = false;
                    isAssert = false;
                } else {
                    overTreeShape.childs.put(pairShape, tree);
                }
            }
        }
    }

    boolean call2(PairShape pairShape, TreeShape treeShape) {
        List<ShapeEntity> shapeEntities = pairShape.ListShape;
        if (shapeEntities.size() == 0) {
            try {
                System.out.println();
                System.out.println();
                System.out.println(mapper.writeValueAsString(pairShape.shapeEntityAB));
                return true;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        TreeShape temp = treeShape;
        while (temp.parent != null) {
            temp = temp.parent;
            if (temp.childs.containsKey(pairShape)) {
                return false;
            }
        }
        int shapeSize = shapeEntities.size();
        List<PairShape> clone = new ArrayList<>();
        ShapeEntity shapeCompare = pairShape.shapeEntityAB;
        for (int i = 0; i < shapeSize; i++) {
            ShapeEntity shapeB = shapeEntities.get(i);
            List<PairShape> pairShapes = combineShape(shapeCompare, shapeB);
            clone.addAll(pairShapes.parallelStream().filter(pair -> {
                if (isOutOfRange(pair.shapeEntityAB)) {
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
                    if (isSameShape(shapeEntity1, pair.primitiveShapeB)) {
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());
                pair.ListShape = list;
                TreeShape tree = new TreeShape(treeShape);
                if (call2(pair, tree)) {
                    try {
                        System.out.println("," + mapper.writeValueAsString(pair.shapeEntityA));
                        System.out.println("," + mapper.writeValueAsString(pair.shapeEntityB));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                if (isAssert || isCenterEmpty) {
                    isAssert = false;
                    isCenterEmpty = false;
                } else {
                    treeShape.childs.put(pair, tree);
                }
            }
        }
//        treeShape.childs.put(pairShape.shapeEntityB.shapeId, new TreeShape(pairShape.shapeEntityB.shapeId, treeShape));
        return false;
    }

    boolean isOutOfRange(ShapeEntity shapeEntity) {
        double minX = 9999;
        double minY = 9999;
        double maxX = -9999;
        double maxY = -9999;
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
        List<PairShape> result = new ArrayList<>();
        List<List<PairShape>> listList =
                (shapeEntityA.edgeEntities.parallelStream().map(edgeEntityA -> {
                    List<PairShape> res = new ArrayList<>();
                    shapeEntityB.edgeEntities.forEach(edgeEntityBB -> {
//                        ShapeEntity shapeEntityA = edgeEntityB;
                        if (squareLengthOfEdge(edgeEntityA) == squareLengthOfEdge(edgeEntityBB)) {
                            ShapeEntity newShapeEntityB = cloner.deepClone(shapeEntityB);
                            ShapeEntity flipXShape = null;
                            ShapeEntity flipYShape = null;
                            ShapeEntity flipXYShape = null;
                            EdgeEntity edgeEntityB90 = cloner.deepClone(edgeEntityBB);
                            if (findIntersectPoint(edgeEntityA, edgeEntityB90) != null) {
                                edgeEntityB90 = rotateEdge90(edgeEntityB90);
                                if (findIntersectPoint(edgeEntityA, edgeEntityB90) != null) return;
                                else {
                                    newShapeEntityB = rotateShape90(newShapeEntityB);
                                    flipXShape = flipX(newShapeEntityB, edgeEntityB90);
                                    flipYShape = flipY(newShapeEntityB, edgeEntityB90);
                                    flipXYShape = flipXY(newShapeEntityB, edgeEntityB90);
                                }
                            } else {
                                flipXShape = flipX(newShapeEntityB, edgeEntityB90);
                                flipYShape = flipY(newShapeEntityB, edgeEntityB90);
                                flipXYShape = flipXY(newShapeEntityB, edgeEntityB90);
                            }

                            newShapeEntityB = translate(newShapeEntityB, edgeEntityA, edgeEntityB90);
                            flipXShape = translate(flipXShape, edgeEntityA, edgeEntityB90);
                            flipYShape = translate(flipYShape, edgeEntityA, edgeEntityB90);
                            flipXYShape = translate(flipXYShape, edgeEntityA, edgeEntityB90);

                            ShapeEntity newShape = mergeShape(shapeEntityA, newShapeEntityB, edgeEntityA);
                            ShapeEntity mergedFlipXShape = mergeShape(shapeEntityA, flipXShape, edgeEntityA);
                            ShapeEntity mergedFlipYShape = mergeShape(shapeEntityA, flipYShape, edgeEntityA);
                            ShapeEntity mergedFlipXYShape = mergeShape(shapeEntityA, flipXYShape, edgeEntityA);
                            if (newShape != null) {
                                newShape = (joinShapeLines(newShape));
                                PairShape pairShape = new PairShape(shapeEntityA, newShapeEntityB, newShape);
                                pairShape.edgeEntityA = cloner.deepClone(edgeEntityA);
                                pairShape.edgeEntityB = cloner.deepClone(edgeEntityBB);
                                pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
                                pairShape.type = 1;
                                remarkIdShape(pairShape);
                                res.add(pairShape);
                            }
                            if (mergedFlipXShape != null) {
                                mergedFlipXShape = (joinShapeLines(mergedFlipXShape));
                                if (!isShapeInList(res, mergedFlipXShape)) {
                                    PairShape pairShape = new PairShape(shapeEntityA, flipXShape, mergedFlipXShape);
                                    pairShape.edgeEntityA = cloner.deepClone(edgeEntityA);
                                    pairShape.edgeEntityB = cloner.deepClone(edgeEntityBB);
                                    pairShape.type = 2;
                                    pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
                                    remarkIdShape(pairShape);
                                    res.add(pairShape);
                                }
                            }
                            if (mergedFlipYShape != null) {
                                mergedFlipYShape = (joinShapeLines(mergedFlipYShape));
                                if (!isShapeInList(res, mergedFlipYShape)) {
                                    PairShape pairShape = new PairShape(shapeEntityA, flipYShape, mergedFlipYShape);
                                    pairShape.edgeEntityA = cloner.deepClone(edgeEntityA);
                                    pairShape.edgeEntityB = cloner.deepClone(edgeEntityBB);
                                    pairShape.type = 3;
                                    pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
                                    remarkIdShape(pairShape);
                                    res.add(pairShape);
                                }
                            }
                            if (mergedFlipXYShape != null) {
                                mergedFlipXYShape = (joinShapeLines(mergedFlipXYShape));
                                if (!isShapeInList(res, mergedFlipXYShape)) {
                                    PairShape pairShape = new PairShape(shapeEntityA, flipXYShape, mergedFlipXYShape);
                                    pairShape.edgeEntityA = cloner.deepClone(edgeEntityA);
                                    pairShape.edgeEntityB = cloner.deepClone(edgeEntityBB);
                                    pairShape.type = 4;
                                    pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
                                    remarkIdShape(pairShape);
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
                if (isSameShape(pairShape.shapeEntityAB, shape)) return true;
            } catch (TopologyException e) {
                System.out.println("isShapeInListError");
                return false;
            }
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
        boolean result;
        try {
            result = p1.equals(p2);
        } catch (Exception e) {
            System.out.println("isShameShapeError");
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

    void remarkIdShape(PairShape pairShape) {
        ShapeEntity shapeEntityA = pairShape.shapeEntityA;
        ShapeEntity shapeEntityB = pairShape.shapeEntityB;
        ShapeEntity shapeEntityAB = pairShape.shapeEntityAB;
        shapeEntityAB.edgeEntities.forEach(edgeEntityAB -> {
            boolean check = true;
            for (int j = 0; j < shapeEntityB.edgeEntities.size(); j++) {
                EdgeEntity edgeEntityB = shapeEntityB.edgeEntities.get(j);
                if (isSameEdge(edgeEntityB, edgeEntityAB)) {
                    edgeEntityAB.edgeId = edgeEntityB.edgeId;
                    check = false;
                    break;
                }
            }
            if (check) edgeEntityAB.edgeId = idEdge++;
        });
        shapeEntityA.edgeEntities.forEach(edgeEntityA -> {
            for (int j = 0; j < shapeEntityAB.edgeEntities.size(); j++) {
                EdgeEntity edgeEntityAB = shapeEntityAB.edgeEntities.get(j);
                if (isSameEdge(edgeEntityA, edgeEntityAB)) {
                    edgeEntityAB.edgeId = edgeEntityA.edgeId;
                    break;
                }
            }
        });
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

    ShapeEntity mergeShape(ShapeEntity shapeEntityA, ShapeEntity newShapeEntityB, EdgeEntity edgeEntityA) {
        if (newShapeEntityB != null) {
            ShapeEntity mergedShape = cloner.deepClone(shapeEntityA);
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
                mergedShape.edgeEntities = new ArrayList<>();
                HashMap<Coordinate, Integer> emptyCenter = new HashMap<>();
                for (int i = 0; i < newCoordinates.length - 1; i++) {
                    if (emptyCenter.containsKey(newCoordinates[i])) {
                        isCenterEmpty = true;
                        return null;
                    } else {

                        emptyCenter.put(newCoordinates[i], 1);
                    }
                    mergedShape.edgeEntities.add(new EdgeEntity(idEdge++, newCoordinates[i].x, newCoordinates[i].y, newCoordinates[i + 1].x, newCoordinates[i + 1].y, 1));
                }
                if (getShapeArea(shapeEntityA) + getShapeArea(newShapeEntityB) == getShapeArea(mergedShape)) {
                    mergedShape.shapeId = idShape++;
                    mergedShape.weight = calWeight(shapeEntityA,newShapeEntityB,edgeEntityA);
                    return mergedShape;
                } else {
                    return null;
                }
            } catch (TopologyException e) {
                System.out.println("Topology  mergeShape");
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

    int calWeight(ShapeEntity shapeEntityA, ShapeEntity newShapeEntityB, EdgeEntity edgeEntityA) {
        int sizeA = shapeEntityA.edgeEntities.size();
        int sizeB = newShapeEntityB.edgeEntities.size();
        for (int positionEdgeEntityA = 0; positionEdgeEntityA < sizeA; positionEdgeEntityA++) {
            EdgeEntity aEdgeEntity = shapeEntityA.edgeEntities.get(positionEdgeEntityA);
            if (isSameEdge(aEdgeEntity, edgeEntityA)) {
                for (int positionEdgeEntityB = 0; positionEdgeEntityB < sizeB; positionEdgeEntityB++) {
                    EdgeEntity bEdgeEntity = newShapeEntityB.edgeEntities.get(positionEdgeEntityB);
                    if (isSameEdge(bEdgeEntity, edgeEntityA)) {
                        int pre = 0;
                        int next = 0;
                        for (int i = 0; i < sizeA; i++) {
                            aEdgeEntity = shapeEntityA.edgeEntities.get((i + positionEdgeEntityA + 1) % sizeA);
                            bEdgeEntity = newShapeEntityB.edgeEntities.get((i + positionEdgeEntityB + 1) % sizeB);
                            if (isSameEdge(aEdgeEntity, bEdgeEntity)) {
                                next++;
                            } else {
                                if (findIntersectPoint(aEdgeEntity, bEdgeEntity) == null) {
                                    Double ax = aEdgeEntity.startX;
                                    Double ay = aEdgeEntity.startY;
                                    Double bx = aEdgeEntity.endX;
                                    Double by = aEdgeEntity.endY;
                                    Double cx = bEdgeEntity.startX;
                                    Double cy = bEdgeEntity.startY;
                                    Double dx = bEdgeEntity.endX;
                                    Double dy = bEdgeEntity.endY;
                                    if (Objects.equals(cx, ax) && Objects.equals(cy, ay)) {
                                        if ((bx - ax) * (dx - cx) >= 0 && (by - ay) * (dy - cy) >= 0) {
                                            next++;
                                        }
                                    } else {
                                        if ((ax - bx) * (cx - dx) >= 0 && (ay - by) * (cy - dy) >= 0) {
                                            next++;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        for (int i = 0; i < sizeA; i++) {
                            aEdgeEntity = shapeEntityA.edgeEntities.get((positionEdgeEntityA - i - 1 + sizeA) % sizeA);
                            bEdgeEntity = newShapeEntityB.edgeEntities.get((positionEdgeEntityB - i - 1 + sizeB) % sizeB);
                            if (isSameEdge(aEdgeEntity, bEdgeEntity)) {
                                pre++;
                            } else {
                                if (findIntersectPoint(aEdgeEntity, bEdgeEntity) == null) {
                                    Double ax = aEdgeEntity.startX;
                                    Double ay = aEdgeEntity.startY;
                                    Double bx = aEdgeEntity.endX;
                                    Double by = aEdgeEntity.endY;
                                    Double cx = bEdgeEntity.startX;
                                    Double cy = bEdgeEntity.startY;
                                    Double dx = bEdgeEntity.endX;
                                    Double dy = bEdgeEntity.endY;
                                    if (Objects.equals(cx, ax) && Objects.equals(cy, ay)) {
                                        if ((bx - ax) * (dx - cx) >= 0 && (by - ay) * (dy - cy) >= 0) {
                                            pre++;
                                        }
                                    } else {
                                        if ((ax - bx) * (cx - dx) >= 0 && (ay - by) * (cy - dy) >= 0) {
                                            pre++;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        int weight = next + pre + 1;
                        return weight;
                    }
                }
                break;
            }
        }
        return 0;
    }

    boolean isPointInEgde(EdgeEntity edgeEntity, Double x, Double y) {
        return ((edgeEntity.startX - x) * (edgeEntity.endX - x) <= 0 && (edgeEntity.startY - y) * (edgeEntity.endY - y) <= 0);
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
        if (Objects.equals(edgeEntityA.startX, edgeEntityB.startX) && Objects.equals(edgeEntityA.startY, edgeEntityB.startY)) {
            if (Objects.equals(edgeEntityA.endX, edgeEntityB.endX) && Objects.equals(edgeEntityA.endY, edgeEntityB.endY)) {
                return true;
            }
        } else if (Objects.equals(edgeEntityA.startX, edgeEntityB.endX) && Objects.equals(edgeEntityA.startY, edgeEntityB.endY)) {
            if (Objects.equals(edgeEntityA.endX, edgeEntityB.startX) && Objects.equals(edgeEntityA.endY, edgeEntityB.startY)) {
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
            while (pairShapes.get(i).shapeEntityAB.weight > pivot.shapeEntityAB.weight) {
                i++;
            }
            while (pairShapes.get(j).shapeEntityAB.weight < pivot.shapeEntityAB.weight) {
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
