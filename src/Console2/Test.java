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
import com.google.common.collect.Lists;
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
    int idPoint = 1;
    int idEdge = 10000;
    int widthFrame = 30;
    int heighFrame = 25;
    TreeShape overTreeShape = new TreeShape(null);
    boolean isAssert = false;
    boolean isCenterEmpty = false;
    ObjectMapper mapper = new ObjectMapper();
    Cloner cloner = new Cloner();
    List<ShapeEntity> shape90Angle = new ArrayList<>();

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
//        List<List<Integer>> s = test.findCombine(0, 0, 3, 5);
//        s.forEach(list -> {
//            list.forEach(x -> {
//                System.out.println(x);
//            });
//            System.out.println();
//            System.out.println();
//            System.out.println();
//            System.out.println();
//        });
        Timestamp pre = new Timestamp(System.currentTimeMillis());
        test.Process(9);
        Timestamp last = new Timestamp(System.currentTimeMillis());
        System.out.println("Done!");
        System.out.println(pre);
        System.out.println(last);

    }

    public void loadAngles(ShapeEntity shapeEntity) {
        shapeEntity.angles = new ArrayList<>();
        shapeEntity.angles = shapeEntity.edgeEntities.parallelStream().map((edgeEntity -> {
            int index = shapeEntity.edgeEntities.indexOf(edgeEntity);
            return computeAngle(shapeEntity, index);
        })).collect(Collectors.toList());
    }

    public boolean isShape90(ShapeEntity shapeEntity) {
        return shapeEntity.angles.parallelStream().allMatch(integer -> integer % 90 == 0);
    }

    public void loadAreas(List<ShapeEntity> shapeEntities) {
        shapeEntities.parallelStream().forEach(shapeEntity -> {
            shapeEntity.area = getShapeArea(shapeEntity);
        });
    }

    /**
     * Tính góc, ví dụ vị trí 0 tính góc giữa 2 cạnh 0 1
     *
     * @param shapeEntity
     * @param index
     * @return
     */
    public double computeAngle(ShapeEntity shapeEntity, int index) {
        int index1 = (index + 1) % shapeEntity.edgeEntities.size();
        double c = Math.pow((shapeEntity.edgeEntities.get(index1).endX - shapeEntity.edgeEntities.get(index).startX), 2) +
                Math.pow((shapeEntity.edgeEntities.get(index1).endY - shapeEntity.edgeEntities.get(index).startY), 2);
        double b = Math.pow((shapeEntity.edgeEntities.get(index1).endX - shapeEntity.edgeEntities.get(index).endX), 2) +
                Math.pow((shapeEntity.edgeEntities.get(index1).endY - shapeEntity.edgeEntities.get(index).endY), 2);
        double a = Math.pow((shapeEntity.edgeEntities.get(index).startX - shapeEntity.edgeEntities.get(index).endX), 2) +
                Math.pow((shapeEntity.edgeEntities.get(index).startY - shapeEntity.edgeEntities.get(index).endY), 2);
        double degree = (Math.acos((a + b - c) / Math.sqrt(4 * a * b)) * 180 / Math.PI);
        if (isConvex(shapeEntity, index)) {
            return degree;
        }
        return 360 - degree;
    }

    public boolean isConvex(ShapeEntity shapeEntity, int index) {
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

    public void Process(int problemId) {
        ProblemEntity problemEntity = problemService.get(problemId);
        widthFrame = problemEntity.width;
        heighFrame = problemEntity.height;
        int shapeSize = problemEntity.shapeEntities.size();
//        List<ShapeEntity> shape90Angle = new ArrayList<>();
//        List<ShapeEntity> shapeNot90Angle = new ArrayList<>();
        problemEntity.shapeEntities.parallelStream().forEach(shapeEntity -> {
            shapeEntity.edgeEntities = shapeService.get(shapeEntity.shapeId).edgeEntities;
//            loadAngles(shapeEntity);
//            if (isShape90(shapeEntity)) {
//                shape90Angle.add(shapeEntity);
//            } else {
//                shapeNot90Angle.add(shapeEntity);
//            }
        });
        loadAreas(problemEntity.shapeEntities);
//        List<ShapeEntity> shapeEntities = angleBaseProcess(problemEntity.shapeEntities);
        edgeBaseProcess(problemEntity.shapeEntities);
    }

    public List<ShapeEntity> angleBaseProcess(List<ShapeEntity> shapeEntities) {
        List<ShapeEntity> shapeNot90Angle = new ArrayList<>();
        shapeEntities.forEach(shapeEntity -> {
            loadAngles(shapeEntity);
            if (isShape90(shapeEntity)) {
                shape90Angle.add(shapeEntity);
            } else {
                shapeNot90Angle.add(shapeEntity);
            }
        });
        List<PairAngleShape> pairAngleShapes = findAllArrShapeAngle(shapeNot90Angle, 0, 0);
        List<List<PairAngleShape>> sortPair = SortPair(pairAngleShapes);
        s(shapeNot90Angle, sortPair);
        return shape90Angle;
    }

    public List<List<PairAngleShape>> SortPair(List<PairAngleShape> pairAngleShapes) {
        List<PairAngleShape> result = cloner.deepClone(pairAngleShapes);
        List<List<PairAngleShape>> combine = new ArrayList<>();
        while (result.size() > 0) {
            PairAngleShape pairAngleShapeA = cloner.deepClone(result.get(0));
            List<PairAngleShape> pairAngleShapes1 = new ArrayList<>();
            pairAngleShapes1.add(pairAngleShapeA);
            List<Integer> indexArr = new ArrayList<>();
            for (int i = 1; i < result.size(); i++) {
                PairAngleShape pairAngleShapeB = result.get(i);
                if (pairAngleShapeA.shapeEntities.size() == pairAngleShapeB.shapeEntities.size()) {
                    boolean check = pairAngleShapeA.shapeEntities.parallelStream().allMatch(shapeEntity -> {
                        return pairAngleShapeB.shapeEntities.stream().anyMatch(shapeEntity1 -> shapeEntity1.shapeId == shapeEntity.shapeId);
                    });
                    if (check) {
                        pairAngleShapes1.add(cloner.deepClone(pairAngleShapeB));
                        indexArr.add(i);
                    }
                }
            }
            for (int i = indexArr.size() - 1; i >= 0; i--) {
                result.remove(indexArr.get(i).intValue());
            }
            result.remove(0);
            combine.add(pairAngleShapes1);
        }
//        combine.forEach(pairAngleShapeList -> {
//            pairAngleShapeList.forEach(pairAngleShape -> {
//                for (int i = 0; i < pairAngleShape.shapeEntities.size(); i++) {
//                    System.out.print(pairAngleShape.shapeEntities.get(i).shapeId + "....." + pairAngleShape.shapeEntities.get(i).angles.get(pairAngleShape.position.get(i)));
//                    System.out.println("...." + pairAngleShape.shapeEntities.get(i).edgeEntities.get(pairAngleShape.position.get(i)).edgeId);
//                }
//                System.out.println(pairAngleShape.totalAngle);
//                System.out.println();
//            });
//            System.out.println();
//            System.out.println();
//            System.out.println();
//        });
        return combine;

    }

    public List<List<Integer>> ChinhHop(List<Integer> index) {
        List<List<Integer>> result = new ArrayList<>();
        index.forEach(number -> {
            List<Integer> list = cloner.deepClone(index);
            list.remove(number);
            if (list.size() == 0) {
                List<Integer> a = new ArrayList<>();
                a.add(number);
                result.add(a);
            } else {
                List<List<Integer>> a = ChinhHop(list);
                a.forEach(list1 -> {
                    list1.add(number);
                });
                result.addAll(a);
            }
        });
        return result;
    }

    public List<ShapeEntity> s(List<ShapeEntity> shapeEntities, List<List<PairAngleShape>> sortPair) {
        List<List<PairAngleShape>> pair = cloner.deepClone(sortPair);
        quicksortPair(pair, 0, pair.size() - 1);
        List<ShapeEntity> result = new ArrayList<>();
        // pair chứa list đã nhóm các cặp và đã được sắp xếp
        for (int i = 0; i < pair.size(); i++) {
            List<PairAngleShape> pairAngleShapes = pair.get(i);
            //pairAngleShapes chứa 1 nhóm cặp các cách ghép
            for (int j = pairAngleShapes.size(); j > 0; j--) { // thực hiện tạo các tổ hợp với k từ n -> 1
                List<List<Integer>> combine = findCombine(0, 0, j, pairAngleShapes.size());
                // combine là tổ hợp các cách sắp xếp có thể của 1 cặp
                List<PairShape> temp = new ArrayList<>();
                for (int k = 0; k < combine.size(); k++) {
                    List<PairShape> pairShapes = combineShapeBaseAngle(pairAngleShapes);
                    temp.addAll(pairShapes);
                }
                List<PairShape> after = new ArrayList<>();
                temp.forEach(pairShape -> {
                    boolean check = after.stream().anyMatch(pairShape1 -> {
                        return isSameShape(pairShape.shapeEntityAB, pairShape1.shapeEntityAB);
                    });
                    if (!check) {
                        after.add(pairShape);
                    }
                });
                for (int k = 0; k < after.size(); k++) {
                    PairShape pairShape = after.get(k);
                    List<List<PairAngleShape>> newPair = removePair(sortPair, pairAngleShapes);
                    PairAngleShape pairAngleShape = pairAngleShapes.get(0);
                    List<ShapeEntity> newShapeEntities = new ArrayList<>();
                    loadAngles(pairShape.shapeEntityAB);
                    if (!isShape90(pairShape.shapeEntityAB)) {
                        newShapeEntities.add(pairShape.shapeEntityAB);
                    }
                    shapeEntities.parallelStream().forEach(shapeEntity -> {
                        if (!pairAngleShape.shapeEntities.stream().anyMatch(shapeEntity1 -> shapeEntity.shapeId == shapeEntity1.shapeId)) {
                            newShapeEntities.add(shapeEntity);
                        }
                    });
                    List<PairAngleShape> pairAngleShapesNew = findAllArrShapeAngle(newShapeEntities, 0, 0);
                    if (pairAngleShapesNew.size() == 0) {
                        try {
                            System.out.println(mapper.writeValueAsString(newShapeEntities));
                            System.out.println();
                            newShapeEntities.addAll(shape90Angle);
                            System.out.println(mapper.writeValueAsString(newShapeEntities));
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            edgeBaseProcess(newShapeEntities);
                            return newShapeEntities;
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                    List<List<PairAngleShape>> sortPairNew = SortPair(pairAngleShapesNew);
                    List<ShapeEntity> entities = s(newShapeEntities, sortPairNew);
                    if (entities.size() > 0) {
                        return result;
                    }
                }
//                after.addAll(temp);
//                List<ShapeEntity> shapeEntities = after.parallelStream().map(pairShape -> pairShape.shapeEntityAB).collect(Collectors.toList());

//                try {
//                    System.out.println(mapper.writeValueAsString(shapeEntities));
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
            }
        }
        return result;
    }


    public List<List<PairAngleShape>> removePair(List<List<PairAngleShape>> pair, List<PairAngleShape> pairAngleShapes) {
        return cloner.deepClone(pair.parallelStream().filter(pairAngleShapes1 -> {
            List<ShapeEntity> shapeEntitiesA = pairAngleShapes1.get(0).shapeEntities;
            List<ShapeEntity> shapeEntitiesB = pairAngleShapes.get(0).shapeEntities;
            for (int i = 0; i < shapeEntitiesA.size(); i++) {
                for (int j = 0; j < shapeEntitiesB.size(); j++) {
                    if (shapeEntitiesA.get(i).shapeId == shapeEntitiesB.get(j).shapeId) {
                        return false;
                    }
                }
            }
            return true;
        }).collect(Collectors.toList()));
    }

    public List<PairShape> combineShapeBaseAngle(List<PairAngleShape> pairAngleShapes) {
        List<ShapeEntity> shapeEntities = pairAngleShapes.get(0).shapeEntities;
        List<Integer> position = pairAngleShapes.get(0).position;
        List<PairShape> result = new ArrayList<>();
        List<Integer> arr = new ArrayList<>();
        for (int j = 1; j < position.size(); j++) {
            arr.add(j);
        }
        List<List<Integer>> roundTable = ChinhHop(arr);
        roundTable.forEach(list -> {
            list.add(0);
        });
        for (int i = 0; i < roundTable.size(); i++) {
            List<Integer> indexArr = roundTable.get(i);
            ShapeEntity shapeEntityA = cloner.deepClone(shapeEntities.get(indexArr.get(0)));
            Integer positionA = cloner.deepClone(position.get(indexArr.get(0)));
            double xA = shapeEntityA.edgeEntities.get(positionA).endX;
            double yA = shapeEntityA.edgeEntities.get(positionA).endY;
            List<PairShape> pairShapes = null;
            for (int j = 1; j < shapeEntities.size(); j++) {
                ShapeEntity shapeEntityB = cloner.deepClone(shapeEntities.get(indexArr.get(j)));
                Integer positionB = cloner.deepClone(position.get(indexArr.get(j)));
                double xB = shapeEntityB.edgeEntities.get(positionB).endX;
                double yB = shapeEntityB.edgeEntities.get(positionB).endY;
                if (pairShapes == null) {
                    pairShapes = combineShapeBasePoint(shapeEntityA, shapeEntityB, xA, yA, xB, yB);
                } else {
                    List<PairShape> temp = new ArrayList<>();
                    pairShapes.parallelStream().forEach(pairShape -> {
                        List<PairShape> temp1 = combineShapeBasePoint(pairShape.shapeEntityAB, shapeEntityB, xA, yA, xB, yB);
                        temp.addAll(temp1);
                    });
                    pairShapes = temp;
                }
            }
            if (pairShapes != null) {
                List<PairShape> shapes = pairShapes.parallelStream().filter(pairShape -> {
                    ShapeEntity shapeEntityAB = pairShape.shapeEntityAB;
                    loadAngles(shapeEntityAB);
                    boolean check = true;
                    for (int j = 1; j < pairAngleShapes.size() && check; j++) {
                        PairAngleShape pairAngleShape = pairAngleShapes.get(j);
                        for (int k = 0; k < pairAngleShape.shapeEntities.size(); k++) {
                            ShapeEntity shapeEntity = pairAngleShape.shapeEntities.get(k);
                            if (shapeEntity.shapeId == shapeEntityA.shapeId) {
                                Integer index = pairAngleShape.position.get(k);
                                double x = shapeEntity.edgeEntities.get(index).endX;
                                double y = shapeEntity.edgeEntities.get(index).endY;
                                for (int h = 0; h < shapeEntityAB.edgeEntities.size(); h++) {
                                    EdgeEntity edgeEntity = shapeEntityAB.edgeEntities.get(h);
                                    if (edgeEntity.endX == x && edgeEntity.endY == y) {
                                        if (shapeEntityAB.angles.get(h) % 90 != 0) {
                                            check = false;
                                        }
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    return check;
                }).collect(Collectors.toList());
                result.addAll(shapes);
            }
        }

        return result;
    }

    public List<PairShape> combineShapeBaseEdge(ShapeEntity shapeEntityA, ShapeEntity shapeEntityB) {
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

    public List<List<Integer>> findCombine(int index, int i, int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        if (i + 1 == k) {
            for (int v = index; v < n; v++) {
                List<Integer> a = new ArrayList<>();
                a.add(v);
                result.add(a);
            }
        } else {
            for (int v = index; v < n - k + i + 1; v++) {
                int ind = v;
                List<List<Integer>> a = findCombine(v + 1, i + 1, k, n);
                a.parallelStream().forEach(list -> {
                    list.add(ind);
                });
                result.addAll(a);
            }
        }
        return result;
    }

    public List<List<Integer>> findRoundTable(int i, List<Integer> arr) {
        List<List<Integer>> result = new ArrayList<>();
        if (i == 0) {
            List<Integer> temp = cloner.deepClone(arr);
            arr.remove(i);
            if (arr.size() > 0) {
                List<List<Integer>> a = findRoundTable(1, arr);
                a.parallelStream().forEach(list -> {
                    list.add(temp.get(0));
                });
                result.addAll(a);
            }
        } else {
            if (arr.size() == 1) {
                List<Integer> a = new ArrayList<>();
                a.add(arr.get(0));
                result.add(a);
            } else {
                for (int j = 0; j < arr.size(); j++) {
                    List<Integer> temp = cloner.deepClone(arr);
                    temp.remove(j);
                    int k = j;
                    List<Integer> temp2 = arr;
                    List<List<Integer>> a = findRoundTable(1, temp);
                    a.parallelStream().forEach(list -> {
                        list.add(temp2.get(k));
                    });
                    result.addAll(a);
                }
            }
        }
        return result;
    }

    public List<List<ShapeEntity>> findAllArrShapeArea(List<ShapeEntity> list, int startIndex, double currentSum, double sum) {
        List<List<ShapeEntity>> result = new ArrayList<>();
        for (int i = startIndex; i < list.size(); i++) {
            if (list.get(i).area + currentSum > sum) break;
            List<ShapeEntity> a = new ArrayList<>();
            if (list.get(i).area + currentSum == sum) {
                a.add(list.get(i));
                result.add(a);
                for (int j = i + 1; j < list.size(); j++) {
                    if (Objects.equals(list.get(j), list.get(i))) {
                        List<ShapeEntity> t = new ArrayList<>();
                        t.add(list.get(j));
                        result.add(t);
                    } else {
                        break;
                    }
                }
                break;
            } else {
                double newSum = currentSum + list.get(i).area;
                ShapeEntity num = cloner.deepClone(list.get(i));
                List<List<ShapeEntity>> lists = findAllArrShapeArea(list, i + 1, newSum, sum);
                lists.parallelStream().forEach(list1 -> {
                    list1.add(num);
                });
                result.addAll(lists);
            }
        }
        return result;
    }

    public List<PairAngleShape> findAllArrShapeAngle(List<ShapeEntity> shapeEntities, int startIndex, double sumAngle) {
        List<PairAngleShape> result = new ArrayList<>();
        for (int i = startIndex; i < shapeEntities.size(); i++) {
            ShapeEntity shapeEntityA = shapeEntities.get(i);
            for (int j = 0; j < shapeEntityA.angles.size(); j++) {
                double angleA = shapeEntityA.angles.get(j);
                if (angleA % 90 != 0) {
                    double sum = angleA + sumAngle;
                    if (sum <= 360) {
                        if (sum % 90 == 0) {
                            PairAngleShape pairAngleShape = new PairAngleShape();
                            pairAngleShape.totalAngle = angleA + sumAngle;
                            pairAngleShape.shapeEntities.add(cloner.deepClone(shapeEntityA));
                            pairAngleShape.position.add(j);
                            result.add(pairAngleShape);
                        } else {
                            List<PairAngleShape> pairAngleShapes = findAllArrShapeAngle(shapeEntities, i + 1, sum);
                            int finalJ = j;
                            pairAngleShapes.parallelStream().forEach(pairAngleShape -> {
                                pairAngleShape.position.add(finalJ);
                                pairAngleShape.shapeEntities.add(cloner.deepClone(shapeEntityA));
                            });
                            result.addAll(pairAngleShapes);
                        }
                    }
                }
            }
        }
        return result;
    }

    public void edgeBaseProcess(List<ShapeEntity> shapeEntities) {
        int shapeSize = shapeEntities.size();
        for (int i = 0; i < shapeSize - 1; i++) {
            List<PairShape> pairShapeList = new ArrayList<>();
            for (int j = i + 1; j < shapeSize; j++) {
                ShapeEntity shapeA = shapeEntities.get(i);
                ShapeEntity shapeB = shapeEntities.get(j);
                List<PairShape> pairShapes = combineShapeBaseEdge(shapeA, shapeB);
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
                    tree.isCenterEmpty = true;
                } else {
                    overTreeShape.childs.put(pairShape, tree);
                }
            }
        }
    }

    public boolean call2(PairShape pairShape, TreeShape treeShape) {
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
            List<PairShape> pairShapes = combineShapeBaseEdge(shapeCompare, shapeB);
            clone.addAll(pairShapes.parallelStream().filter(pair -> {
                if (isOutOfRange(pair.shapeEntityAB)) {
                    return false;
                } else {
                    return true;
                }
            }).collect(Collectors.toList()));
            clone = sort(clone);
//            for (int j = clone.size() - 1; j >= 0; j--) {
            for (int j = 0; j < clone.size(); j++) {
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

    public boolean isOutOfRange(ShapeEntity shapeEntity) {
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

    public List<PairShape> combineShapeBasePoint(ShapeEntity shapeEntityA, ShapeEntity shapeEntityB, double xA, double yA, double xB, double yB) {
        List<PairShape> result = new ArrayList<>();
        EdgeEntity edgeEntityB90 = new EdgeEntity(xB, yB - 1, xB, yB + 1);
        EdgeEntity edgeEntityBB = new EdgeEntity(xB, yB, xB + 1, yB);
        ShapeEntity newShapeEntityB = cloner.deepClone(shapeEntityB);
        ShapeEntity flipXShape = flipX(newShapeEntityB, edgeEntityBB);
        ShapeEntity flipYShape = flipY(newShapeEntityB, edgeEntityBB);
        ShapeEntity flipXYShape = flipXY(newShapeEntityB, edgeEntityBB);
        ShapeEntity newShapeEntityB90 = rotateShape90(newShapeEntityB);
        ShapeEntity flipXShape90 = flipX(newShapeEntityB, edgeEntityB90);
        ShapeEntity flipYShape90 = flipY(newShapeEntityB, edgeEntityB90);
        ShapeEntity flipXYShape90 = flipXY(newShapeEntityB, edgeEntityB90);
        newShapeEntityB = translate(newShapeEntityB, xA, yA, xB, yB);
        flipXShape = translate(flipXShape, xA, yA, xB, yB);
        flipYShape = translate(flipYShape, xA, yA, xB, yB);
        flipXYShape = translate(flipXYShape, xA, yA, xB, yB);
        newShapeEntityB90 = translate(newShapeEntityB90, xA, yA, xB, yB);
        flipXShape90 = translate(flipXShape90, xA, yA, xB, yB);
        flipYShape90 = translate(flipYShape90, xA, yA, xB, yB);
        flipXYShape90 = translate(flipXYShape90, xA, yA, xB, yB);
        ShapeEntity newShape = mergeShape(shapeEntityA, newShapeEntityB, null);
        ShapeEntity mergedFlipXShape = mergeShape(shapeEntityA, flipXShape, null);
        ShapeEntity mergedFlipYShape = mergeShape(shapeEntityA, flipYShape, null);
        ShapeEntity mergedFlipXYShape = mergeShape(shapeEntityA, flipXYShape, null);
        ShapeEntity newShape90 = mergeShape(shapeEntityA, newShapeEntityB90, null);
        ShapeEntity mergedFlipXShape90 = mergeShape(shapeEntityA, flipXShape90, null);
        ShapeEntity mergedFlipYShape90 = mergeShape(shapeEntityA, flipYShape90, null);
        ShapeEntity mergedFlipXYShape90 = mergeShape(shapeEntityA, flipXYShape90, null);
        if (newShape != null) {
            newShape = (joinShapeLines(newShape));
            PairShape pairShape = new PairShape(shapeEntityA, newShapeEntityB, newShape);
            pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
            pairShape.type = 1;
            remarkIdShape(pairShape);
            result.add(pairShape);
        }
        if (mergedFlipXShape != null) {
            mergedFlipXShape = (joinShapeLines(mergedFlipXShape));
            if (!isShapeInList(result, mergedFlipXShape)) {
                PairShape pairShape = new PairShape(shapeEntityA, flipXShape, mergedFlipXShape);
                pairShape.type = 2;
                pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
                remarkIdShape(pairShape);
                result.add(pairShape);
            }
        }
        if (mergedFlipYShape != null) {
            mergedFlipYShape = (joinShapeLines(mergedFlipYShape));
            if (!isShapeInList(result, mergedFlipYShape)) {
                PairShape pairShape = new PairShape(shapeEntityA, flipYShape, mergedFlipYShape);
                pairShape.type = 3;
                pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
                remarkIdShape(pairShape);
                result.add(pairShape);
            }
        }
        if (mergedFlipXYShape != null) {
            mergedFlipXYShape = (joinShapeLines(mergedFlipXYShape));
            if (!isShapeInList(result, mergedFlipXYShape)) {
                PairShape pairShape = new PairShape(shapeEntityA, flipXYShape, mergedFlipXYShape);
                pairShape.type = 4;
                pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
                remarkIdShape(pairShape);
                result.add(pairShape);
            }
        }
        if (newShape90 != null) {
            newShape90 = (joinShapeLines(newShape90));
            PairShape pairShape = new PairShape(shapeEntityA, newShapeEntityB, newShape90);
            pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
            pairShape.type = 5;
            remarkIdShape(pairShape);
            result.add(pairShape);
        }
        if (mergedFlipXShape90 != null) {
            mergedFlipXShape90 = (joinShapeLines(mergedFlipXShape90));
            if (!isShapeInList(result, mergedFlipXShape90)) {
                PairShape pairShape = new PairShape(shapeEntityA, flipXShape, mergedFlipXShape90);
                pairShape.type = 6;
                pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
                remarkIdShape(pairShape);
                result.add(pairShape);
            }
        }
        if (mergedFlipYShape90 != null) {
            mergedFlipYShape90 = (joinShapeLines(mergedFlipYShape90));
            if (!isShapeInList(result, mergedFlipYShape90)) {
                PairShape pairShape = new PairShape(shapeEntityA, flipYShape, mergedFlipYShape90);
                pairShape.type = 7;
                pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
                remarkIdShape(pairShape);
                result.add(pairShape);
            }
        }
        if (mergedFlipXYShape90 != null) {
            mergedFlipXYShape90 = (joinShapeLines(mergedFlipXYShape90));
            if (!isShapeInList(result, mergedFlipXYShape90)) {
                PairShape pairShape = new PairShape(shapeEntityA, flipXYShape, mergedFlipXYShape90);
                pairShape.type = 8;
                pairShape.primitiveShapeB = cloner.deepClone(shapeEntityB);
                remarkIdShape(pairShape);
                result.add(pairShape);
            }
        }
        return result;
    }


    public boolean isShapeInList(List<PairShape> pairShapes, ShapeEntity shape) {
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

    public boolean isSameShape(ShapeEntity shapeA, ShapeEntity shapeB) {
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

    public ShapeEntity joinShapeLines(ShapeEntity shapeEntity) {
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

    public void remarkIdShape(PairShape pairShape) {
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

    public Console.Point findIntersectPoint(EdgeEntity edgeEntityA, EdgeEntity edgeEntityB) {
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

    public boolean isOnTheSameLine(EdgeEntity edgeEntityA, EdgeEntity edgeEntityB) {
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

    public ShapeEntity translate(ShapeEntity shapeEntity, EdgeEntity edgeEntityA, EdgeEntity edgeEntityB) {
        Cloner cloner = new Cloner();
        ShapeEntity clone = cloner.deepClone(shapeEntity);
        if (findIntersectPoint(edgeEntityA, edgeEntityB) != null) {
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

    public ShapeEntity translate(ShapeEntity shapeEntity, double xA, double yA, double xB, double yB) {
        Cloner cloner = new Cloner();
        ShapeEntity clone = cloner.deepClone(shapeEntity);
        double x = xB - xA;
        double y = yB - yA;
        clone.edgeEntities.forEach(edgeEntity -> {
            edgeEntity.startX -= x;
            edgeEntity.startY -= y;
            edgeEntity.endX -= x;
            edgeEntity.endY -= y;
        });
        return clone;
    }

    public ShapeEntity mergeShape(ShapeEntity shapeEntityA, ShapeEntity newShapeEntityB, EdgeEntity edgeEntityA) {
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
                if (shapeEntityA.area + newShapeEntityB.area == getShapeArea(mergedShape)) {
                    mergedShape.shapeId = idShape++;
                    if (edgeEntityA != null) {
                        mergedShape.weight = calWeight(shapeEntityA, newShapeEntityB, edgeEntityA);
                    }
                    mergedShape.area = shapeEntityA.area + newShapeEntityB.area;
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
//                    ShapeEntity[] SortPair = {shapeEntityA,shapeEntityB};
//                    try {
//                        System.out.println(mapper.writeValueAsString(SortPair));
//                        e.printStackTrace();
//                    } catch (JsonProcessingException e1) {
//                        e1.printStackTrace();
//                    }
                return null;
            }
        }
        return null;
    }

    public Console.Point mirror(Console.Point p, double x0, double y0, double x1, double y1) {
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

    public int calWeight(ShapeEntity shapeEntityA, ShapeEntity newShapeEntityB, EdgeEntity edgeEntityA) {
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

    public boolean isPointInEgde(EdgeEntity edgeEntity, Double x, Double y) {
        return ((edgeEntity.startX - x) * (edgeEntity.endX - x) <= 0 && (edgeEntity.startY - y) * (edgeEntity.endY - y) <= 0);
    }

    public ShapeEntity flipX(ShapeEntity shapeEntity, EdgeEntity edgeEntity) {
        ShapeEntity result = flipShape(shapeEntity, edgeEntity.startX, edgeEntity.startY, edgeEntity.endX, edgeEntity.endY);
        return result;
    }

    public ShapeEntity flipY(ShapeEntity shapeEntity, EdgeEntity edgeEntity) {
        double centerX = (edgeEntity.startX + edgeEntity.endX) / 2;
        double centerY = (edgeEntity.startY + edgeEntity.endY) / 2;
        double a = -(edgeEntity.startY - edgeEntity.endY);
        double b = edgeEntity.startX - edgeEntity.endX;
        double x1 = centerX + a;
        double y1 = centerY + b;
        ShapeEntity result = flipShape(shapeEntity, centerX, centerY, x1, y1);
        return result;
    }

    public ShapeEntity flipXY(ShapeEntity shapeEntity, EdgeEntity edgeEntity) {
        ShapeEntity result = flipX(shapeEntity, edgeEntity);
        result = flipY(result, edgeEntity);
        return result;
    }

    public ShapeEntity flipShape(ShapeEntity shape, double x0, double y0, double x1, double y1) {
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

    public double getShapeArea(ShapeEntity shapeEntity) {
        List<EdgeEntity> edgeEntityListA = shapeEntity.edgeEntities;
        List<Coordinate> coordinateListA = edgeEntityListA.parallelStream().map(edgeEntity ->
                new Coordinate(edgeEntity.startX, edgeEntity.startY)
        ).collect(Collectors.toList());
        coordinateListA.add(new Coordinate(edgeEntityListA.get(edgeEntityListA.size() - 1).endX, edgeEntityListA.get(edgeEntityListA.size() - 1).endY));
        coordinateListA.add(new Coordinate(edgeEntityListA.get(0).startX, edgeEntityListA.get(0).startY));
        Polygon p1 = new GeometryFactory().createPolygon(coordinateListA.toArray(new Coordinate[coordinateListA.size()]));
        return p1.getArea();
    }

    public boolean isSameEdge(EdgeEntity edgeEntityA, EdgeEntity edgeEntityB) {
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

    public double squareLengthOfEdge(EdgeEntity edgeEntity) {
        return (Math.pow((edgeEntity.endY - edgeEntity.startY), 2) + Math.pow((edgeEntity.endX - edgeEntity.startX), 2));
    }

    public List<PairShape> sort(List<PairShape> pairShapes) {
        if (pairShapes == null || pairShapes.size() == 0) {
            return pairShapes;
        }
        int number = pairShapes.size();
        quicksortWeight(pairShapes, 0, number - 1);
        return new ArrayList<>(pairShapes);
    }

    public ShapeEntity findInterShape(ShapeEntity shapeEntity) {
        return null;
    }

    public void quicksortWeight(List<PairShape> pairShapes, int low, int high) {
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
            quicksortWeight(pairShapes, low, j);
        if (i < high)
            quicksortWeight(pairShapes, i, high);
    }


    public List<ShapeEntity> quicksortAscArea(List<ShapeEntity> shapeEntities) {
        quicksortArea(shapeEntities, 0, shapeEntities.size() - 1);
        return Lists.reverse(shapeEntities);
    }

    public void quicksortPair(List<List<PairAngleShape>> sortPair, int low, int high) {
        int i = low, j = high;
        List<PairAngleShape> pivot = sortPair.get(low + (high - low) / 2);
        while (i <= j) {
            while (sortPair.get(i).size() > pivot.size()) {
                i++;
            }
            while (sortPair.get(j).size() < pivot.size()) {
                j--;
            }
            if (i <= j) {
                List<PairAngleShape> temp = sortPair.get(i);
                sortPair.set(i, sortPair.get(j));
                sortPair.set(j, temp);
                i++;
                j--;
            }
        }
        if (low < j)
            quicksortPair(sortPair, low, j);
        if (i < high)
            quicksortPair(sortPair, i, high);
    }

    public void quicksortArea(List<ShapeEntity> shapeEntities, int low, int high) {
        int i = low, j = high;
        ShapeEntity pivot = shapeEntities.get(low + (high - low) / 2);
        while (i <= j) {
            while (shapeEntities.get(i).area > pivot.area) {
                i++;
            }
            while (shapeEntities.get(j).area < pivot.area) {
                j--;
            }
            if (i <= j) {
                ShapeEntity temp = shapeEntities.get(i);
                shapeEntities.set(i, shapeEntities.get(j));
                shapeEntities.set(j, temp);
                i++;
                j--;
            }
        }
        if (low < j)
            quicksortArea(shapeEntities, low, j);
        if (i < high)
            quicksortArea(shapeEntities, i, high);
    }


    public ShapeEntity rotateShape90(ShapeEntity shapeEntity) {
        Cloner cloner = new Cloner();
        shapeEntity.edgeEntities = shapeEntity.edgeEntities.parallelStream().map(edgeEntity -> {
            edgeEntity = cloner.deepClone(edgeEntity);
            return rotateEdge90(edgeEntity);
        }).collect(Collectors.toList());
        return shapeEntity;
    }

    public EdgeEntity rotateEdge90(EdgeEntity edgeEntity) {
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
