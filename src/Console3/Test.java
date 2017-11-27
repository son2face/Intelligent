package Console3;

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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Test {
    final int deep = 0;
    final int deepSize = 0;
    public List<List<ShapeEntity>> shapeNot90Findedlist = new ArrayList<>();
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
    ObjectMapper mapper = new ObjectMapper();
    Cloner cloner = new Cloner();
    List<ShapeEntity> shape90Angle = new ArrayList<>();

    public Test() {
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


//        ShapeEntity shapeEntityA = test.shapeService.get(236);
//        ShapeEntity shapeEntityB = test.shapeService.get(237);
//        test.loadAngles(shapeEntityA);
//        test.loadAngles(shapeEntityB);
////        shapeEntityA.area = test.getShapeArea(shapeEntityA);
////        shapeEntityB.area = test.getShapeArea(shapeEntityB);
//////        test.test(shapeEntityB, new EdgeEntity(Double.valueOf(0),Double.valueOf(0),Double.valueOf(0),Double.valueOf(0)));
////        int positionA = 2;
////        int positionB = 1;
////        List<PairShape> pairShapes = test.combineShapeBasePoint(shapeEntityA, shapeEntityB, shapeEntityA.edgeEntities.get(positionA), shapeEntityB.edgeEntities.get(positionB));
//        List<ShapeEntity> shapeEntities = new ArrayList<>();
//        shapeEntities.add(shapeEntityA);
//        shapeEntities.add(shapeEntityB);
//        test.angleBaseProcess(shapeEntities);



//        try {
//            System.out.println(test.mapper.writeValueAsString(shapeEntities));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        Timestamp pre = new Timestamp(System.currentTimeMillis());
        test.Process(10);
        Timestamp last = new Timestamp(System.currentTimeMillis());
        System.out.println("Done!");
        System.out.println(pre);
        System.out.println(last);
    }

    public void test(ShapeEntity shapeEntityB, EdgeEntity edgeEntityB) {
        double xB = edgeEntityB.endX;
        double yB = edgeEntityB.endY;
        EdgeEntity edgeEntityB90 = cloner.deepClone(edgeEntityB);
        edgeEntityB90 = rotateEdge90(edgeEntityB90);
        double xB90 = edgeEntityB90.endX;
        double yB90 = edgeEntityB90.endY;
        List<PairShape> result = new ArrayList<>();
        EdgeEntity edgeEntityBB = new EdgeEntity(xB - 1, yB, xB + 1, yB);
        edgeEntityB90 = new EdgeEntity(xB90 - 1, yB90, xB90 + 1, yB90);
        ShapeEntity newShapeEntityB = cloner.deepClone(shapeEntityB);
        ShapeEntity flipXShape = flipX(newShapeEntityB, edgeEntityBB);
        ShapeEntity flipYShape = flipY(newShapeEntityB, edgeEntityBB);
        ShapeEntity flipXYShape = flipXY(newShapeEntityB, edgeEntityBB);
        ShapeEntity newShapeEntityB90 = rotateShape90(newShapeEntityB);
        ShapeEntity flipXShape90 = flipX(newShapeEntityB90, edgeEntityB90);
        ShapeEntity flipYShape90 = flipY(newShapeEntityB90, edgeEntityB90);
        ShapeEntity flipXYShape90 = flipXY(newShapeEntityB90, edgeEntityB90);
        ShapeEntity[] shapeEntities = {newShapeEntityB, flipXShape, flipYShape, flipXYShape, newShapeEntityB90, flipXShape90, flipYShape90, flipXYShape90};
        try {
            System.out.println(mapper.writeValueAsString(shapeEntities));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public boolean isFindedList(List<ShapeEntity> shapeEntities) {
        List<ShapeEntity> cloneShapeList = cloner.deepClone(shapeEntities);
        cloneShapeList.parallelStream().forEach(this::translateShapeGreat0);
        return shapeNot90Findedlist.parallelStream().anyMatch(shapeEntityList -> {
            return cloneShapeList.stream().allMatch(shapeEntity -> {
                List<Coordinate> coordinateListA = shapeEntity.edgeEntities.parallelStream().map(edgeEntity ->
                        new Coordinate(edgeEntity.startX, edgeEntity.startY)
                ).collect(Collectors.toList());
                coordinateListA.add(new Coordinate(shapeEntity.edgeEntities.get(shapeEntity.edgeEntities.size() - 1).endX, shapeEntity.edgeEntities.get(shapeEntity.edgeEntities.size() - 1).endY));
                coordinateListA.add(new Coordinate(shapeEntity.edgeEntities.get(0).startX, shapeEntity.edgeEntities.get(0).startY));
                return shapeEntityList.stream().anyMatch(shapeEntity1 -> {
                    List<Coordinate> coordinateListB = shapeEntity1.edgeEntities.parallelStream().map(edgeEntity ->
                            new Coordinate(edgeEntity.startX, edgeEntity.startY)
                    ).collect(Collectors.toList());
                    coordinateListB.add(new Coordinate(shapeEntity1.edgeEntities.get(shapeEntity1.edgeEntities.size() - 1).endX, shapeEntity1.edgeEntities.get(shapeEntity1.edgeEntities.size() - 1).endY));
                    coordinateListB.add(new Coordinate(shapeEntity1.edgeEntities.get(0).startX, shapeEntity1.edgeEntities.get(0).startY));
                    Polygon p1 = new GeometryFactory().createPolygon(coordinateListA.toArray(new Coordinate[coordinateListA.size()]));
                    Polygon p2 = new GeometryFactory().createPolygon(coordinateListB.toArray(new Coordinate[coordinateListB.size()]));
                    return p1.equalsTopo(p2);
                });
            });
        });
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
        List<ShapeEntity> shapeEntities = angleBaseProcess(problemEntity.shapeEntities);
//        edgeBaseProcess(problemEntity.shapeEntities);
    }

    public List<ShapeEntity> angleBaseProcess(List<ShapeEntity> shapeEntities) {
//        List<ShapeEntity> shapeNot90Angle = new ArrayList<>();
        // Lọc thành 2 mảng chứa các hình có tất cả các góc là vuông và phần còn lại
        shapeEntities.forEach(shapeEntity -> {
            loadAngles(shapeEntity);
//            if (isShape90(shapeEntity)) {
//                shape90Angle.add(shapeEntity);
//            } else {
//                shapeNot90Angle.add(shapeEntity);
//            }
        });
        // Tìm tất cả các cách có thể ghép dựa trên các góc của nó
        List<PairAngleShape> pairAngleShapes = findAllArrShapeAngle2(shapeEntities);
        //
        List<List<PairAngleShape>> sortPair = groupPair(pairAngleShapes);
        if (sortPair.size() > 0 && deepSize != sortPair.get(0).size()) {
            int threshhold = sortPair.get(0).size() - deepSize;
            sortPair = sortPair.parallelStream().filter(pairAngleShapes1 -> pairAngleShapes1.size() >= threshhold).collect(Collectors.toList());
        }
        s(shapeEntities, sortPair);
        return shape90Angle;
    }

    /**
     * Nhóm các Pair có cùng các hình ghép
     *
     * @param pairAngleShapeList
     * @return
     */
    public List<List<PairAngleShape>> groupPair(List<PairAngleShape> pairAngleShapeList) {
        List<PairAngleShape> clonePairAngleShapeList = cloner.deepClone(pairAngleShapeList);
        List<List<PairAngleShape>> result = new ArrayList<>();
        while (clonePairAngleShapeList.size() > 0) {
            List<List<PairAngleShape>> res = new ArrayList<>();
            PairAngleShape pairAngleShapeA = cloner.deepClone(clonePairAngleShapeList.get(0));
            List<PairAngleShape> oneGroup = new ArrayList<>();
            oneGroup.add(pairAngleShapeA);
            List<Integer> indexArr = new ArrayList<>();
            for (int i = 1; i < clonePairAngleShapeList.size(); i++) {
                PairAngleShape pairAngleShapeB = clonePairAngleShapeList.get(i);
                // Kiểm tra 2 pair có cùng các hình hay không. Yes, thêm vào tập
                if (pairAngleShapeA.shapeEntities.size() == pairAngleShapeB.shapeEntities.size()) {
                    boolean check = pairAngleShapeA.shapeEntities.parallelStream().allMatch(shapeEntity -> {
                        return pairAngleShapeB.shapeEntities.stream().anyMatch(shapeEntity1 -> shapeEntity1.shapeId == shapeEntity.shapeId);
                    });
                    if (check) {
                        oneGroup.add(cloner.deepClone(pairAngleShapeB));
                        indexArr.add(i);
                    }
                }
            }
            for (int i = indexArr.size() - 1; i >= 0; i--) {
                clonePairAngleShapeList.remove(indexArr.get(i).intValue());
            }
            clonePairAngleShapeList.remove(0);
//            List<List<PairAngleShape>> sequentList = new ArrayList<>();
//            sequentList.add(oneGroup);
            quicksortPairByPosition(oneGroup, 0, oneGroup.size() - 1, 0);
            oneGroup = Lists.reverse(oneGroup);
            List<Integer> shapeSizes = new ArrayList<>();
            for (int j = 0; j < oneGroup.get(0).shapeEntities.size(); j++) {
                shapeSizes.add(oneGroup.get(0).shapeEntities.get(j).edgeEntities.size());
            }
            for (int oneGroupIndex = 0; oneGroupIndex < oneGroup.size(); oneGroupIndex++) {
                if (oneGroupIndex == oneGroup.size() - 1) {
                    List<PairAngleShape> finalOneGroup1 = oneGroup;
                    int finalOneGroupIndex = oneGroupIndex;
                    boolean b = res.stream().anyMatch(pairAngleShapes -> {
                        return pairAngleShapes.parallelStream().anyMatch(pairAngleShape -> {
                            if (pairAngleShape.position.size() != finalOneGroup1.get(finalOneGroupIndex).position.size())
                                return false;
                            boolean check = true;
                            for (int i = 0; i < pairAngleShape.position.size(); i++) {
                                if (pairAngleShape.position.get(i) != finalOneGroup1.get(finalOneGroupIndex).position.get(i)) {
                                    check = false;
                                    break;
                                }
                            }
                            return check;
                        });
                    });
                    if (!b) {
                        List<PairAngleShape> list = new ArrayList<>();
                        list.add(oneGroup.get(oneGroupIndex));
                        res.add(list);
                    }
                    break;
                } else if (oneGroupIndex > oneGroup.size() - 1) {
                    break;
                }
                List<List<PairAngleShape>> sequentList = new ArrayList<>();
                int startI = oneGroupIndex + 1;
                while (startI < oneGroup.size() && oneGroup.get(startI).position.get(0) == oneGroup.get(oneGroupIndex).position.get(0))
                    startI++;
                if (startI == oneGroup.size()) {
                    List<PairAngleShape> finalOneGroup1 = oneGroup;
                    int finalOneGroupIndex = oneGroupIndex;
                    boolean b = res.stream().anyMatch(pairAngleShapes -> {
                        return pairAngleShapes.parallelStream().anyMatch(pairAngleShape -> {
                            if (pairAngleShape.position.size() != finalOneGroup1.get(finalOneGroupIndex).position.size())
                                return false;
                            boolean check = true;
                            for (int i = 0; i < pairAngleShape.position.size(); i++) {
                                if (pairAngleShape.position.get(i) != finalOneGroup1.get(finalOneGroupIndex).position.get(i)) {
                                    check = false;
                                    break;
                                }
                            }
                            return check;
                        });
                    });
                    if (!b) {
                        List<PairAngleShape> list = new ArrayList<>();
                        list.add(oneGroup.get(oneGroupIndex));
                        res.add(list);
                    }
                    continue;
                }
                int sub = oneGroup.get(startI).position.get(0) - oneGroup.get(oneGroupIndex).position.get(0);
                if (sub == 1) {
                    List<List<Integer>> listSubList = new ArrayList<>();
                    List<PairAngleShape> listNextNumber = new ArrayList<>();
//                    List<Integer> removeList = new ArrayList<>();
//                    removeList.add(oneGroupIndex);
                    listNextNumber.add(oneGroup.get(startI));
                    int end = startI;
                    for (int j = startI + 1; j < oneGroup.size(); j++) {
                        if (oneGroup.get(startI).position.get(0) == oneGroup.get(j).position.get(0)) {
                            listNextNumber.add(oneGroup.get(j));
                        } else {
                            end = j;
                            break;
                        }
                    }
                    List<PairAngleShape> listNextNumberAccept = new ArrayList<>();
                    for (int i = 0; i < listNextNumber.size(); i++) {
                        boolean check = true;
                        List<Integer> subList = new ArrayList<>();
                        subList.add(sub);
                        for (int j = 1; j < oneGroup.get(oneGroupIndex).shapeEntities.size(); j++) {
                            int subbb = listNextNumber.get(i).position.get(j) - oneGroup.get(oneGroupIndex).position.get(j);
                            if (subbb == 1 || subbb == -1) {
                                subList.add(subbb);
                            } else if (listNextNumber.get(i).position.get(j) == shapeSizes.get(j) - 1 && oneGroup.get(oneGroupIndex).position.get(j) == 0) {
                                subList.add(-1);
                            } else if (listNextNumber.get(i).position.get(j) == 0 && oneGroup.get(oneGroupIndex).position.get(j) == shapeSizes.get(j) - 1) {
                                subList.add(1);
                            } else {
                                check = false;
                                break;
                            }
                        }
                        if (check) {
                            listSubList.add(subList);
                            listNextNumberAccept.add(listNextNumber.get(i));
                        }
                    }
                    if (listNextNumberAccept.size() > 0) {
                        List<PairAngleShape> finalOneGroup = oneGroup;
                        int finalOneGroupIndex = oneGroupIndex;
                        List<List<PairAngleShape>> pairAngleList = listNextNumberAccept.parallelStream().map(pairAngleShape -> {
                            List<PairAngleShape> s = new ArrayList<>();
                            s.add(finalOneGroup.get(finalOneGroupIndex));
                            s.add(pairAngleShape);
                            return s;
                        }).collect(Collectors.toList());
                        if (oneGroup.get(end).position.get(0) - oneGroup.get(end - 1).position.get(0) != 1) {
                            sequentList.addAll(pairAngleList);
//                            for (int i = removeList.size() - 1; i >= 0; i--) {
//                                oneGroup.remove((int) removeList.get(i));
//                            }
                        } else {
                            while (pairAngleList.size() > 0) {
                                listNextNumber = new ArrayList<>();
                                listNextNumber.add(oneGroup.get(end));
                                for (int j = end + 1; j < oneGroup.size(); j++) {
                                    if (oneGroup.get(end).position.get(0) == oneGroup.get(j).position.get(0)) {
                                        listNextNumber.add(oneGroup.get(j));
                                    } else {
                                        end = j;
                                        break;
                                    }
                                }
                                List<Boolean> check = pairAngleList.parallelStream().map(pairAngleShape -> Boolean.FALSE).collect(Collectors.toList());
                                // listNextNumber lưu lại các phần tử 4 , (555555)
                                for (int i = 0; i < listNextNumber.size(); i++) {
                                    for (int j = 0; j < pairAngleList.size(); j++) {
                                        if (check.get(j)) continue;
                                        boolean che = true;
                                        for (int k = 1; k < oneGroup.get(oneGroupIndex).shapeEntities.size(); k++) {
                                            int subbb = listNextNumber.get(i).position.get(k) - pairAngleList.get(j).get(pairAngleList.get(j).size() - 1).position.get(k);
                                            if (subbb == listSubList.get(j).get(k)) {
                                            } else if (listNextNumber.get(i).position.get(k) == shapeSizes.get(k) - 1 && pairAngleList.get(j).get(pairAngleList.get(j).size() - 1).position.get(k) == 0 && listSubList.get(j).get(k) == -1) {
                                            } else if (listNextNumber.get(i).position.get(k) == 0 && pairAngleList.get(j).get(pairAngleList.get(j).size() - 1).position.get(k) == shapeSizes.get(k) - 1 && listSubList.get(j).get(k) == 1) {
                                            } else {
                                                che = false;
                                                break;
                                            }
                                        }
                                        if (che) {
                                            check.set(j, true);
                                            pairAngleList.get(j).add(listNextNumber.get(i));
                                        }
                                    }
                                }
                                List<List<PairAngleShape>> temp = new ArrayList<>();
                                for (int i = 0; i < check.size(); i++) {
                                    if (check.get(i)) {
                                        temp.add(pairAngleList.get(i));
                                    } else {
                                        sequentList.add(pairAngleList.get(i));
                                    }
                                }
                                pairAngleList = temp;
                            }
//                            for (int i = removeList.size() - 1; i >= 0; i--) {
//                                oneGroup.remove((int) removeList.get(i));
//                            }
                        }
//                    } else if (oneGroup.get(startI).position.get(0) == shapeSizes.get(0) - 1 && oneGroup.get(0).position.get(0) == 0) {

                    } else {
                        List<PairAngleShape> t = new ArrayList<>();
                        t.add(oneGroup.get(oneGroupIndex));
//                        oneGroup.remove(0);
                        sequentList.add(t);
                    }
                }
                List<PairAngleShape> finalOneGroup2 = oneGroup;
                for (int h = 0; h < sequentList.size(); h++) {
                    List<PairAngleShape> pairAngleShapes = sequentList.get(h);
                    boolean b = res.parallelStream().anyMatch(resPairAngleShapes -> {
                        return pairAngleShapes.stream().allMatch(pairAngleShape -> {
                            return resPairAngleShapes.parallelStream().anyMatch(pairAngleShapeRes -> {
                                finalOneGroup2.indexOf(pairAngleShapeRes);
                                if (pairAngleShapeRes.position.size() != pairAngleShape.position.size())
                                    return false;
                                boolean check = true;
                                for (int i = 0; i < pairAngleShapeRes.position.size(); i++) {
                                    if (pairAngleShapeRes.position.get(i) != pairAngleShape.position.get(i)) {
                                        check = false;
                                        break;
                                    }
                                }
                                return check;
                            });
                        });
                    });
                    if (!b) {
                        res.add(pairAngleShapes);
                    }
                }
            }
            if (oneGroup.size() == 1) {
                List<PairAngleShape> t = new ArrayList<>();
                t.add(oneGroup.get(0));
                res.add(t);
            }
            List<List<PairAngleShape>> pairStartListList = new ArrayList<>();
            List<List<PairAngleShape>> pairEndListList = new ArrayList<>();
            List<List<PairAngleShape>> list = new ArrayList<>();
            for (int i = 0; i < res.size(); i++) {
                List<PairAngleShape> t = res.get(i);
                if (t.get(t.size() - 1).position.get(0) != shapeSizes.get(0) - 1 && t.get(0).position.get(0) == 0) {
                    pairStartListList.add(t);
                } else if (t.get(t.size() - 1).position.get(0) == shapeSizes.get(0) - 1 && t.get(0).position.get(0) != 0) {
                    pairEndListList.add(t);
                } else {
                    list.add(t);
                }
            }
            List<Boolean> startCheckList = pairStartListList.parallelStream().map(pairAngleShape -> Boolean.FALSE).collect(Collectors.toList());
            if (pairEndListList.size() == 0) {
                result.addAll(pairStartListList);
            } else if (pairStartListList.size() == 0) {
                result.addAll(pairEndListList);
            } else {
                for (int i = 0; i < pairEndListList.size(); i++) {
                    boolean checkExistGroup = false;
                    List<PairAngleShape> pairEndList = pairEndListList.get(i);
                    if (pairEndList.size() == 1) {
                        for (int j = 0; j < pairStartListList.size(); j++) {
                            List<PairAngleShape> pairStartList = pairStartListList.get(j);
                            if (pairStartList.size() == 1) {
                                PairAngleShape pairAngleShapeEnd = pairEndList.get(pairEndList.size() - 1);
                                PairAngleShape pairAngleShapeStart = pairStartList.get(0);
                                boolean che = true;
                                for (int h = 1; h < shapeSizes.size(); h++) {
                                    int subbb = pairAngleShapeStart.position.get(h) - pairAngleShapeEnd.position.get(h);
                                    if (subbb == -1 || subbb == 1) {
                                    } else if (pairAngleShapeEnd.position.get(h) == shapeSizes.get(h) - 1 && pairAngleShapeStart.position.get(h) == 0) {
                                    } else if (pairAngleShapeEnd.position.get(h) == 0 && pairAngleShapeStart.position.get(h) == shapeSizes.get(h) - 1) {
                                    } else {
                                        che = false;
                                        break;
                                    }
                                }
                                if (che) {
                                    checkExistGroup = true;
                                    startCheckList.set(j, true);
                                    List<PairAngleShape> pairAngleShapes = new ArrayList<>();
                                    pairAngleShapes.add(pairAngleShapeEnd);
                                    pairAngleShapes.add(pairAngleShapeStart);
                                    list.add(pairAngleShapes);
                                }
                            } else {
                                PairAngleShape pairAngleShapeEnd = pairEndList.get(pairEndList.size() - 1);
                                PairAngleShape pairAngleShapeStart = pairStartList.get(0);
                                List<Integer> subList = new ArrayList<>();
                                boolean che = true;
                                subList.add(1);
                                for (int h = 1; h < shapeSizes.size(); h++) {
                                    int subbb = pairAngleShapeStart.position.get(h) - pairAngleShapeEnd.position.get(h);
                                    if (subbb == 1 || subbb == -1) {
                                        subList.add(subbb);
                                    } else if (pairAngleShapeEnd.position.get(h) == shapeSizes.get(h) - 1 && pairAngleShapeStart.position.get(h) == 0) {
                                        subList.add(1);
                                    } else if (pairAngleShapeEnd.position.get(h) == 0 && pairAngleShapeStart.position.get(h) == shapeSizes.get(h) - 1) {
                                        subList.add(-1);
                                    } else {
                                        che = false;
                                        break;
                                    }
                                }
                                if (che) {
                                    boolean check = true;
                                    for (int h = 1; h < shapeSizes.size(); h++) {
                                        int subbb = pairStartList.get(1).position.get(h) - pairAngleShapeStart.position.get(h);
                                        if (subbb == 1 || subbb == -1) {
                                            if (subList.get(h) != subbb) {
                                                check = false;
                                                break;
                                            }
                                        } else if (pairStartList.get(1).position.get(h) == shapeSizes.get(h) - 1 && pairAngleShapeStart.position.get(h) == 0) {
                                            if (subList.get(h) != -1) {
                                                check = false;
                                                break;
                                            }
                                        } else if (pairStartList.get(1).position.get(h) == 0 && pairAngleShapeStart.position.get(h) == shapeSizes.get(h) - 1) {
                                            if (subList.get(h) != 1) {
                                                check = false;
                                                break;
                                            }
                                        } else {
                                            check = false;
                                            break;
                                        }
                                    }
                                    if (check) {
                                        List<PairAngleShape> pairAngleShapes = new ArrayList<>();
                                        pairAngleShapes.add(pairAngleShapeEnd);
                                        pairAngleShapes.addAll(pairStartList);
                                        startCheckList.set(j, true);
                                        list.add(pairAngleShapes);
                                    } else {
                                        List<PairAngleShape> pairAngleShapes = new ArrayList<>();
                                        pairAngleShapes.add(pairAngleShapeEnd);
                                        pairAngleShapes.add(pairAngleShapeStart);
                                        list.add(pairAngleShapes);
                                    }
                                    checkExistGroup = true;
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < pairStartListList.size(); j++) {
                            List<PairAngleShape> pairStartList = pairStartListList.get(j);
                            if (pairStartList.size() == 1) {
                                PairAngleShape pairAngleShapeEnd = pairEndList.get(pairEndList.size() - 1);
                                PairAngleShape pairAngleShapeStart = pairStartList.get(0);
                                List<Integer> subList = new ArrayList<>();
                                boolean che = true;
                                subList.add(1);
                                for (int h = 1; h < shapeSizes.size(); h++) {
                                    int subbb = pairAngleShapeStart.position.get(h) - pairAngleShapeEnd.position.get(h);
                                    if (subbb == 1 || subbb == -1) {
                                        subList.add(subbb);
                                    } else if (pairAngleShapeEnd.position.get(h) == shapeSizes.get(h) - 1 && pairAngleShapeStart.position.get(h) == 0) {
                                        subList.add(1);
                                    } else if (pairAngleShapeEnd.position.get(h) == 0 && pairAngleShapeStart.position.get(h) == shapeSizes.get(h) - 1) {
                                        subList.add(-1);
                                    } else {
                                        che = false;
                                        break;
                                    }
                                }
                                if (che) {
                                    boolean check = true;
                                    for (int h = 1; h < shapeSizes.size(); h++) {
                                        int subbb = pairAngleShapeEnd.position.get(h) - pairEndList.get(pairEndList.size() - 2).position.get(h);
                                        if (subbb == 1 || subbb == -1) {
                                            if (subList.get(h) != subbb) {
                                                check = false;
                                                break;
                                            }
                                        } else if (pairAngleShapeEnd.position.get(h) == shapeSizes.get(h) - 1 && pairEndList.get(pairEndList.size() - 2).position.get(h) == 0) {
                                            if (subList.get(h) != -1) {
                                                check = false;
                                                break;
                                            }
                                        } else if (pairAngleShapeEnd.position.get(h) == 0 && pairEndList.get(pairEndList.size() - 2).position.get(h) == shapeSizes.get(h) - 1) {
                                            if (subList.get(h) != 1) {
                                                check = false;
                                                break;
                                            }
                                        } else {
                                            check = false;
                                            break;
                                        }
                                    }
                                    if (check) {
                                        List<PairAngleShape> pairAngleShapes = new ArrayList<>();
                                        pairAngleShapes.addAll(pairEndList);
                                        pairAngleShapes.add(pairAngleShapeStart);
                                        list.add(pairAngleShapes);
                                        checkExistGroup = true;
                                    } else {
                                        List<PairAngleShape> pairAngleShapes = new ArrayList<>();
                                        pairAngleShapes.add(pairAngleShapeEnd);
                                        pairAngleShapes.add(pairAngleShapeStart);
                                        list.add(pairAngleShapes);
                                    }
                                    startCheckList.set(j, true);
                                }
                            } else {
                                PairAngleShape pairAngleShapeEnd = pairEndList.get(pairEndList.size() - 1);
                                PairAngleShape pairAngleShapeStart = pairStartList.get(0);
                                List<Integer> subList = new ArrayList<>();
                                boolean che = true;
                                subList.add(1);
                                for (int h = 1; h < shapeSizes.size(); h++) {
                                    int subbb = pairAngleShapeStart.position.get(h) - pairAngleShapeEnd.position.get(h);
                                    if (subbb == 1 || subbb == -1) {
                                        subList.add(subbb);
                                    } else if (pairAngleShapeEnd.position.get(h) == shapeSizes.get(h) - 1 && pairAngleShapeStart.position.get(h) == 0) {
                                        subList.add(1);
                                    } else if (pairAngleShapeEnd.position.get(h) == 0 && pairAngleShapeStart.position.get(h) == shapeSizes.get(h) - 1) {
                                        subList.add(-1);
                                    } else {
                                        che = false;
                                        break;
                                    }
                                }
                                if (che) {
                                    boolean checkStart = true;
                                    for (int h = 1; h < shapeSizes.size(); h++) {
                                        int subbb = pairStartList.get(1).position.get(h) - pairAngleShapeStart.position.get(h);
                                        if (subbb == 1 || subbb == -1) {
                                            if (subList.get(h) != subbb) {
                                                checkStart = false;
                                                break;
                                            }
                                        } else if (pairStartList.get(1).position.get(h) == shapeSizes.get(h) - 1 && pairAngleShapeStart.position.get(h) == 0) {
                                            if (subList.get(h) != -1) {
                                                checkStart = false;
                                                break;
                                            }
                                        } else if (pairStartList.get(1).position.get(h) == 0 && pairAngleShapeStart.position.get(h) == shapeSizes.get(h) - 1) {
                                            if (subList.get(h) != 1) {
                                                checkStart = false;
                                                break;
                                            }
                                        } else {
                                            checkStart = false;
                                            break;
                                        }
                                    }
                                    boolean checkEnd = true;
                                    for (int h = 1; h < shapeSizes.size(); h++) {
                                        int subbb = pairAngleShapeEnd.position.get(h) - pairEndList.get(pairEndList.size() - 2).position.get(h);
                                        if (subbb == 1 || subbb == -1) {
                                            if (subList.get(h) != subbb) {
                                                checkEnd = false;
                                                break;
                                            }
                                        } else if (pairAngleShapeEnd.position.get(h) == shapeSizes.get(h) - 1 && pairEndList.get(pairEndList.size() - 2).position.get(h) == 0) {
                                            if (subList.get(h) != -1) {
                                                checkEnd = false;
                                                break;
                                            }
                                        } else if (pairAngleShapeEnd.position.get(h) == 0 && pairEndList.get(pairEndList.size() - 2).position.get(h) == shapeSizes.get(h) - 1) {
                                            if (subList.get(h) != 1) {
                                                checkEnd = false;
                                                break;
                                            }
                                        } else {
                                            checkEnd = false;
                                            break;
                                        }
                                    }
                                    if (checkStart && checkEnd) {
                                        List<PairAngleShape> pairAngleShapes = new ArrayList<>();
                                        pairAngleShapes.addAll(pairEndList);
                                        pairAngleShapes.addAll(pairStartList);
                                        list.add(pairAngleShapes);
                                        startCheckList.set(j, true);
                                        checkExistGroup = true;
                                    } else if (checkStart && !checkEnd) {
                                        List<PairAngleShape> pairAngleShapes = new ArrayList<>();
                                        pairAngleShapes.add(pairAngleShapeEnd);
                                        pairAngleShapes.addAll(pairStartList);
                                        list.add(pairAngleShapes);
                                        startCheckList.set(j, true);
                                    } else if (!checkStart && checkEnd) {
                                        List<PairAngleShape> pairAngleShapes = new ArrayList<>();
                                        pairAngleShapes.addAll(pairEndList);
                                        pairAngleShapes.add(pairAngleShapeStart);
                                        list.add(pairAngleShapes);
                                        checkExistGroup = true;
                                    } else {
                                        List<PairAngleShape> pairAngleShapes = new ArrayList<>();
                                        pairAngleShapes.add(pairAngleShapeEnd);
                                        pairAngleShapes.add(pairAngleShapeStart);
                                        list.add(pairAngleShapes);
                                    }
                                }
                            }
                        }
                    }
                    if (!checkExistGroup) {
                        list.add(pairEndList);
                    }
                }
            }
            for (int i = 0; i < startCheckList.size(); i++) {
                if (!startCheckList.get(i)) list.add(pairStartListList.get(i));
            }
            result.addAll(list);

        }
        return result;
    }

    public void separatePair(List<PairAngleShape> sortedPairAngleShapeList) {

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

    public List<ShapeEntity> s(List<ShapeEntity> shapeEntities, List<List<PairAngleShape>> pairShapeList) {
        try {
            System.out.println(mapper.writeValueAsString(shapeEntities));
            System.out.println();
            System.out.println();
            System.out.println();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (pairShapeList.size() == 0) {
            System.out.println("Null");
            return new ArrayList<>();
        }
        List<List<PairAngleShape>> clonePairShapeList;
        if (deepSize != pairShapeList.get(0).size()) {
            int threshhold = pairShapeList.get(0).size() - deepSize;
            List<List<PairAngleShape>> temp = pairShapeList.parallelStream().filter(pairAngleShapes1 -> pairAngleShapes1.size() >= threshhold).collect(Collectors.toList());
            clonePairShapeList = cloner.deepClone(temp);
        } else {
            clonePairShapeList = cloner.deepClone(pairShapeList);
        }
        quicksortPair(clonePairShapeList, 0, clonePairShapeList.size() - 1);
//        pair.forEach(pairAngleShapeList -> {
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
        List<ShapeEntity> result = new ArrayList<>();
        // pair chứa list đã nhóm các cặp và đã được sắp xếp
        for (int i = 0; i < clonePairShapeList.size(); i++) {
            List<PairAngleShape> pairShapeNot90 = clonePairShapeList.get(i);
            //pariShapeNot90 chứa 1 nhóm cặp các cách ghép
            for (int j = pairShapeNot90.size(); j >= pairShapeNot90.size() - deep && j >= 1; j--) { // thực hiện tạo các tổ hợp với k từ n -> 1
                List<List<Integer>> combineList = findCombine(0, 0, j, pairShapeNot90.size());
                // combineList là tổ hợp các cách sắp xếp có thể của 1 cặp
                List<PairShape> temp = new ArrayList<>();
                for (List<Integer> combine : combineList) {
                    //Lay ra cac cap hinh can duoc xet
                    List<PairAngleShape> t = new ArrayList<>();
                    for (Integer index : combine) {
                        t.add(pairShapeNot90.get(index));
                    }
                    List<PairShape> pairShapes = combineShapeBaseAngle(t);
                    temp.addAll(pairShapes);
                }
                // Lưu các hình có thể ghép được đối với cách chọn ghép sử dụng ở trên
                List<PairShape> pairShapeAfterFilterList = new ArrayList<>();
                // Lọc các hình trùng nhau
                temp.forEach(pairShape -> {
                    if (isOutOfRange(pairShape.shapeEntityAB)) return;
                    boolean check = pairShapeAfterFilterList.stream().anyMatch(pairShape1 -> {
                        return isSameShape(pairShape.shapeEntityAB, pairShape1.shapeEntityAB);
                    });
                    if (!check) {
                        pairShapeAfterFilterList.add(pairShape);
                    }
                });
                for (PairShape pairShape : pairShapeAfterFilterList) {
//                    List<List<PairAngleShape>> newPair = removePair(pairShapeList, pariShapeNot90);
                    // pairAngleShape lưu giữ tập các hình đã sử dụng cho cách ghép
                    PairAngleShape pairAngleShape = pairShapeNot90.get(0);
                    List<ShapeEntity> newShapeNot90List = new ArrayList<>();
                    // shapeEntityAB hình thể hiện của cách ghép đã cho
                    ShapeEntity shapeEntityAB = cloner.deepClone(pairShape.shapeEntityAB);

//                    if (!isShape90(shapeEntityAB)) {
                    newShapeNot90List.add(shapeEntityAB);
//                    }
                    // Loại bỏ các hình có trong cách ghép trên để tạo ra 1 tập so sánh mới
                    shapeEntities.forEach(shapeEntity -> {
                        if (pairAngleShape.shapeEntities.parallelStream().noneMatch(shapeEntity1 -> shapeEntity.shapeId == shapeEntity1.shapeId)) {
                            translateShapeGreat0(shapeEntity);
                            loadAngles(shapeEntity);
                            newShapeNot90List.add(shapeEntity);
                        }
                    });
                    if (newShapeNot90List.size() == 1) {
                        try {
                            System.out.println(mapper.writeValueAsString(newShapeNot90List));
                            return null;
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                    // Tìm các cách ghép mới từ tập hình được sinh ra
                    List<PairAngleShape> pairAngleShapeNewList = findAllArrShapeAngle2(newShapeNot90List);
                    // Nếu không tìm được cặp hình nào phù hợp thì kiểm tra nếu tất cả các hình là 90 độ thì chuẩn xác và dừng lại, sử dụng xử lí theo cạnh
                    if (pairAngleShapeNewList.size() == 0) {
                        System.out.println("Wrong!");
//                        try {
////                            System.out.println(mapper.writeValueAsString(newShapeNot90List));
////                            System.out.println();
//                            if (newShapeNot90List.parallelStream().allMatch(this::isShape90)) {
//                                if (!isFindedList(newShapeNot90List)) {
//                                    List<ShapeEntity> shapeEntityList = cloner.deepClone(newShapeNot90List);
//                                    shapeEntityList.parallelStream().forEach(this::translateShapeGreat0);
//                                    shapeNot90Findedlist.add(shapeEntityList);
//                                    newShapeNot90List.addAll(shape90Angle);
//                                    System.out.println(mapper.writeValueAsString(newShapeNot90List));
//                                    System.out.println();
//                                    System.out.println();
//                                    System.out.println();
////                                edgeBaseProcess(newShapeNot90List);
//                                    return newShapeNot90List;
//                                }
//                            }
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }
                    } else {
                        // Nếu tồn tại các cặp có thể ghép được thì thực hiện nhóm pair
                        List<List<PairAngleShape>> sortPairNew = groupPair(pairAngleShapeNewList);
                        // Thực hiện đệ quy
//                        System.out.println(pairShape.type);
                        List<ShapeEntity> entities = s(newShapeNot90List, sortPairNew);
                        // Kiểm tra xem có trả về giá trị hay không, có tức là đã hoàn thành tìm kiếm
                        if (entities.size() > 0) {
                            return result;
                        }
                    }
                }
//                pairShapeAfterFilterList.addAll(temp);
//                List<ShapeEntity> shapeEntities = pairShapeAfterFilterList.parallelStream().map(pairShape -> pairShape.shapeEntityAB).collect(Collectors.toList());

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

    public void translateShapeGreat0(ShapeEntity shapeEntity) {
        double minX = 9999;
        double minY = 9999;
        for (int i = 0; i < shapeEntity.edgeEntities.size(); i++) {
            EdgeEntity edgeEntity = shapeEntity.edgeEntities.get(i);
            minX = Math.min(minX, edgeEntity.startX);
            minY = Math.min(minY, edgeEntity.startY);
        }
        double finalMinX = 0 - minX;
        double finalMinY = 0 - minY;
        shapeEntity.edgeEntities.parallelStream().forEach(edgeEntity -> {
            edgeEntity.startX += finalMinX;
            edgeEntity.startY += finalMinY;
            edgeEntity.endX += finalMinX;
            edgeEntity.endY += finalMinY;
        });
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
//            double xA = shapeEntityA.edgeEntities.get(positionA).endX;
//            double yA = shapeEntityA.edgeEntities.get(positionA).endY;
            List<PairShape> pairShapes = null;
            for (int j = 1; j < shapeEntities.size(); j++) {
                ShapeEntity shapeEntityB = cloner.deepClone(shapeEntities.get(indexArr.get(j)));
                Integer positionB = cloner.deepClone(position.get(indexArr.get(j)));
//                double xB = shapeEntityB.edgeEntities.get(positionB).endX;
//                double yB = shapeEntityB.edgeEntities.get(positionB).endY;
                Checker checker = new Checker();
                if (pairShapes == null) {
                    pairShapes = combineShapeBasePoint(shapeEntityA, shapeEntityB, shapeEntityA.edgeEntities.get(positionA), shapeEntityB.edgeEntities.get(positionB), checker);
                } else {
                    List<PairShape> temp = new ArrayList<>();
                    pairShapes.parallelStream().map(pairShape -> {
                        return combineShapeBasePoint(pairShape.shapeEntityAB, shapeEntityB, shapeEntityA.edgeEntities.get(positionA), shapeEntityB.edgeEntities.get(positionB), checker);
                    }).collect(Collectors.toList()).forEach(temp::addAll);
                    pairShapes = temp;
                }
            }
            if (pairShapes != null) {
                List<PairShape> shapes = pairShapes.parallelStream().filter(pairShape -> {
                    boolean check = true;
                    try {
                        ShapeEntity shapeEntityAB = pairShape.shapeEntityAB;
                        loadAngles(shapeEntityAB);
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return check;
                }).collect(Collectors.toList());
                result.addAll(shapes);
            }
        }

        return result;
    }

    public List<PairShape> combineShapeBaseEdge(ShapeEntity shapeEntityA, ShapeEntity shapeEntityB, Checker checker) {
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

                            ShapeEntity newShape = mergeShape(shapeEntityA, newShapeEntityB, edgeEntityA, checker);
                            ShapeEntity mergedFlipXShape = mergeShape(shapeEntityA, flipXShape, edgeEntityA, checker);
                            ShapeEntity mergedFlipYShape = mergeShape(shapeEntityA, flipYShape, edgeEntityA, checker);
                            ShapeEntity mergedFlipXYShape = mergeShape(shapeEntityA, flipXYShape, edgeEntityA, checker);
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
        try {
            for (int i = startIndex; i < shapeEntities.size(); i++) {
                ShapeEntity shapeEntityA = shapeEntities.get(i);
                for (int j = 0; j < shapeEntityA.angles.size(); j++) {
                    double angleA = shapeEntityA.angles.get(j);
                    double sum = angleA + sumAngle;
                    if (sum <= 360) {
                        if (sum == 360) {
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
        } catch (Exception e) {
            System.out.println("findAllArrShapeAngle");
            e.printStackTrace();
        }

        return result;
    }

    public List<PairAngleShape> findAllArrShapeAngle2(List<ShapeEntity> shapeEntities) {
        List<PairAngleShape> result = new ArrayList<>();
        try {
            for (int i = 0; i < shapeEntities.size() - 1; i++) {
                ShapeEntity shapeEntityA = shapeEntities.get(i);
                for (int j = 0; j < shapeEntityA.angles.size(); j++) {
                    double angleA = shapeEntityA.angles.get(j);
                    for (int k = i + 1; k < shapeEntities.size(); k++) {
                        ShapeEntity shapeEntityB = shapeEntities.get(k);
                        for (int h = 0; h < shapeEntityB.angles.size(); h++) {
                            double angleB = shapeEntityB.angles.get(h);
                            double total = angleA + angleB;
                            if (total == 360) {
                                PairAngleShape pairAngleShape = new PairAngleShape();
                                pairAngleShape.totalAngle = total;
                                pairAngleShape.shapeEntities.add(cloner.deepClone(shapeEntityA));
                                pairAngleShape.shapeEntities.add(cloner.deepClone(shapeEntityB));
                                pairAngleShape.position.add(j);
                                pairAngleShape.position.add(h);
                                result.add(pairAngleShape);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("findAllArrShapeAngle");
            e.printStackTrace();
        }
        return result;
    }

    public void edgeBaseProcess(List<ShapeEntity> shapeEntities) {
        int shapeSize = shapeEntities.size();
        for (int i = 0; i < shapeSize - 1; i++) {
            Checker checker = new Checker();
            List<PairShape> pairShapeList = new ArrayList<>();
            for (int j = i + 1; j < shapeSize; j++) {
                ShapeEntity shapeA = shapeEntities.get(i);
                ShapeEntity shapeB = shapeEntities.get(j);
                List<PairShape> pairShapes = combineShapeBaseEdge(shapeA, shapeB, checker);
                pairShapes = pairShapes.parallelStream().filter(pairShape -> {
                    if (isOutOfRange(pairShape.shapeEntityAB)) {
                        return false;
                    } else {
                        return true;
                    }
                }).collect(Collectors.toList());
                pairShapeList.addAll(pairShapes);
            }
            pairShapeList = sortBaseWeight(pairShapeList);
//            for (int j = pairShapeList.size() - 1; j >= 0; j--) {
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
                Checker newChecker = new Checker();
                if (call2(pairShape, tree, newChecker)) {
                    try {
                        System.out.println("," + mapper.writeValueAsString(pairShape.shapeEntityA));
                        System.out.println("," + mapper.writeValueAsString(pairShape.shapeEntityB));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                if (newChecker.isCenterEmpty) {
                    tree.isCenterEmpty = true;
                } else {
                    overTreeShape.childs.put(pairShape, tree);
                }
            }
        }
    }

    public boolean call2(PairShape pairShape, TreeShape treeShape, Checker checker) {
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
                if (!temp.childs.get(pairShape).isCenterEmpty) {
                    return false;
                }
            }
        }
        int shapeSize = shapeEntities.size();
        List<PairShape> clone = new ArrayList<>();
        ShapeEntity shapeCompare = pairShape.shapeEntityAB;
        for (int i = 0; i < shapeSize; i++) {
            ShapeEntity shapeB = shapeEntities.get(i);
            List<PairShape> pairShapes = combineShapeBaseEdge(shapeCompare, shapeB, checker);
//            List<ShapeEntity> shapeEntityList = pairShapes.parallelStream().map(pairShape1 -> pairShape1.shapeEntityAB).collect(Collectors.toList());
//            try {
//                System.out.println(mapper.writeValueAsString(shapeEntityList));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
            clone.addAll(pairShapes.parallelStream().filter(pair -> {
                return !isOutOfRange(pair.shapeEntityAB);
            }).collect(Collectors.toList()));

        }
        clone = sortBaseWeight(clone);
//            for (int j = clone.size() - 1; j >= 0; j--) {
        Checker newChecker = new Checker();
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
            if (call2(pair, tree, newChecker)) {
                try {
                    System.out.println("," + mapper.writeValueAsString(pair.shapeEntityA));
                    System.out.println("," + mapper.writeValueAsString(pair.shapeEntityB));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return true;
            }
            if (newChecker.isCenterEmpty) {
                tree.isCenterEmpty = true;
            }
            treeShape.childs.put(pair, tree);
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

    public List<PairShape> combineShapeBasePoint(ShapeEntity shapeEntityA, ShapeEntity shapeEntityB, EdgeEntity edgeEntityA, EdgeEntity edgeEntityB, Checker checker) {
        double xA = edgeEntityA.endX;
        double yA = edgeEntityA.endY;
        double xB = edgeEntityB.endX;
        double yB = edgeEntityB.endY;
        EdgeEntity edgeEntityB90 = cloner.deepClone(edgeEntityB);
        edgeEntityB90 = rotateEdge90(edgeEntityB90);
        double xB90 = edgeEntityB90.endX;
        double yB90 = edgeEntityB90.endY;
        List<PairShape> result = new ArrayList<>();
        EdgeEntity edgeEntityBB = new EdgeEntity(xB - 1, yB, xB + 1, yB);
        edgeEntityB90 = new EdgeEntity(xB90 - 1, yB90, xB90 + 1, yB90);
        ShapeEntity newShapeEntityB = cloner.deepClone(shapeEntityB);
        ShapeEntity flipXShape = flipX(newShapeEntityB, edgeEntityBB);
        ShapeEntity flipYShape = flipY(newShapeEntityB, edgeEntityBB);
        ShapeEntity flipXYShape = flipXY(newShapeEntityB, edgeEntityBB);
        ShapeEntity newShapeEntityB90 = rotateShape90(newShapeEntityB);
        ShapeEntity flipXShape90 = flipX(newShapeEntityB90, edgeEntityB90);
        ShapeEntity flipYShape90 = flipY(newShapeEntityB90, edgeEntityB90);
        ShapeEntity flipXYShape90 = flipXY(newShapeEntityB90, edgeEntityB90);
        newShapeEntityB = translate(newShapeEntityB, xA, yA, xB, yB);
        flipXShape = translate(flipXShape, xA, yA, xB, yB);
        flipYShape = translate(flipYShape, xA, yA, xB, yB);
        flipXYShape = translate(flipXYShape, xA, yA, xB, yB);
        newShapeEntityB90 = translate(newShapeEntityB90, xA, yA, xB90, yB90);
        flipXShape90 = translate(flipXShape90, xA, yA, xB90, yB90);
        flipYShape90 = translate(flipYShape90, xA, yA, xB90, yB90);
        flipXYShape90 = translate(flipXYShape90, xA, yA, xB90, yB90);
        ShapeEntity newShape = mergeShape(shapeEntityA, newShapeEntityB, null, checker);
        ShapeEntity mergedFlipXShape = mergeShape(shapeEntityA, flipXShape, null, checker);
        ShapeEntity mergedFlipYShape = mergeShape(shapeEntityA, flipYShape, null, checker);
        ShapeEntity mergedFlipXYShape = mergeShape(shapeEntityA, flipXYShape, null, checker);
        ShapeEntity newShape90 = mergeShape(shapeEntityA, newShapeEntityB90, null, checker);
        ShapeEntity mergedFlipXShape90 = mergeShape(shapeEntityA, flipXShape90, null, checker);
        ShapeEntity mergedFlipYShape90 = mergeShape(shapeEntityA, flipYShape90, null, checker);
        ShapeEntity mergedFlipXYShape90 = mergeShape(shapeEntityA, flipXYShape90, null, checker);
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
//        if (shapeEntity.shapeId == 1010) {
//            System.out.println();
//        }
        ShapeEntity cloneShapeEntity = cloner.deepClone(shapeEntity);
        cloneShapeEntity.edgeEntities = new ArrayList<>();
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
                // Kiểm tra xem có giao điểm của 2 đoạn thẳng, nếu không có tức là cùng nằm trên 1 đường thẳng vì có 2 điểm đầu và cuối trùng nhau
//                if (shapeEntity.edgeEntities.get((k + 1) % size).edgeId == 11172) {
//                    System.out.println();
//                }
                if (findIntersectPoint(shapeEntity.edgeEntities.get(k % size), shapeEntity.edgeEntities.get((k + 1) % size)) == null) {
                    for (int j = 0; j < size; j++) {
                        if (findIntersectPoint(shapeEntity.edgeEntities.get((k + j + 1) % size), shapeEntity.edgeEntities.get((k + j + 2) % size)) != null) {
                            EdgeEntity edgeToClone = shapeEntity.edgeEntities.get(k % size);
                            EdgeEntity edgeEntity = cloner.deepClone(edgeToClone);
                            edgeEntity.endX = shapeEntity.edgeEntities.get((k + 1 + j) % size).endX;
                            edgeEntity.endY = shapeEntity.edgeEntities.get((k + 1 + j) % size).endY;
                            cloneShapeEntity.edgeEntities.add(edgeEntity);
                            i += (j + 1);
                            break;
                        }
                    }
                } else {
                    cloneShapeEntity.edgeEntities.add(cloner.deepClone(shapeEntity.edgeEntities.get(k % size)));
                }
            }
//            startPoint--;
//            int endPoint = (startPoint + 2 * size) % size;
//            startPoint--;
//            boolean check = false;
//            EdgeEntity edgeEndPointEntity = null;
//            for (int i = 0; i < size; i++) {
//                int k = startPoint - i + 2 * size;
//                if (findIntersectPoint(shapeEntity.edgeEntities.get(endPoint), shapeEntity.edgeEntities.get(k % size)) == null) {
//                    edgeEndPointEntity = shapeEntity.edgeEntities.get(k % size);
//                    check = true;
//                } else {
//                    break;
//                }
//            }
//            if (check) {
//                EdgeEntity edgeEntity = cloner.deepClone(shapeEntity.edgeEntities.get(endPoint));
//                cloneShapeEntity.edgeEntities.remove(cloneShapeEntity.edgeEntities.size() - 1);
//                edgeEndPointEntity = cloner.deepClone(edgeEndPointEntity);
//                EdgeEntity edgeEntity1 = cloneShapeEntity.edgeEntities.get(cloneShapeEntity.edgeEntities.size() - 1);
//                edgeEntity.startX = edgeEndPointEntity.startX;
//                edgeEntity.startY = edgeEndPointEntity.startY;
//                edgeEndPointEntity.startX = edgeEntity1.endX;
//                edgeEndPointEntity.startY = edgeEntity1.endY;
//                cloneShapeEntity.edgeEntities.add(edgeEndPointEntity);
//            }

        }
        return cloneShapeEntity;
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

    public ShapeEntity mergeShape(ShapeEntity shapeEntityA, ShapeEntity newShapeEntityB, EdgeEntity edgeEntityA, Checker checker) {
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
                Polygon union = (Polygon) p1.union(p2);
                if (shapeEntityA.area + newShapeEntityB.area == union.getArea()) {
                    Coordinate[] newCoordinates = union.getCoordinates();
                    mergedShape.edgeEntities = new ArrayList<>();
//                    HashMap<Coordinate, Integer> emptyCenter = new HashMap<>();
//                    System.out.println(union.getNumInteriorRing());
                    if (union.getNumInteriorRing() > 0) {
                        checker.isCenterEmpty = true;
                        return null;
                    }
                    for (int i = 0; i < newCoordinates.length - 1; i++) {
                        mergedShape.edgeEntities.add(new EdgeEntity(idEdge++, newCoordinates[i].x, newCoordinates[i].y, newCoordinates[i + 1].x, newCoordinates[i + 1].y, 1));
                    }
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
            } catch (ClassCastException e) {
//                System.out.println("ClassCast mergeShape");
                return null;
            } catch (AssertionFailedException e) {
                System.out.println("Assert Test");
                isAssert = true;
//                    ShapeEntity[] groupPair = {shapeEntityA,shapeEntityB};
//                    try {
//                        System.out.println(mapper.writeValueAsString(groupPair));
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
//                                if (findIntersectPoint(aEdgeEntity, bEdgeEntity) == null) {
//                                    Double ax = aEdgeEntity.startX;
//                                    Double ay = aEdgeEntity.startY;
//                                    Double bx = aEdgeEntity.endX;
//                                    Double by = aEdgeEntity.endY;
//                                    Double cx = bEdgeEntity.startX;
//                                    Double cy = bEdgeEntity.startY;
//                                    Double dx = bEdgeEntity.endX;
//                                    Double dy = bEdgeEntity.endY;
//                                    if (Objects.equals(cx, ax) && Objects.equals(cy, ay)) {
//                                        if ((bx - ax) * (dx - cx) >= 0 && (by - ay) * (dy - cy) >= 0) {
//                                            next++;
//                                        }
//                                    } else {
//                                        if ((ax - bx) * (cx - dx) >= 0 && (ay - by) * (cy - dy) >= 0) {
//                                            next++;
//                                        }
//                                    }
//                                }
                                break;
                            }
                        }
                        for (int i = 0; i < sizeA; i++) {
                            aEdgeEntity = shapeEntityA.edgeEntities.get((positionEdgeEntityA - i - 1 + sizeA) % sizeA);
                            bEdgeEntity = newShapeEntityB.edgeEntities.get((positionEdgeEntityB - i - 1 + sizeB) % sizeB);
                            if (isSameEdge(aEdgeEntity, bEdgeEntity)) {
                                pre++;
                            } else {
//                                if (findIntersectPoint(aEdgeEntity, bEdgeEntity) == null) {
//                                    Double ax = aEdgeEntity.startX;
//                                    Double ay = aEdgeEntity.startY;
//                                    Double bx = aEdgeEntity.endX;
//                                    Double by = aEdgeEntity.endY;
//                                    Double cx = bEdgeEntity.startX;
//                                    Double cy = bEdgeEntity.startY;
//                                    Double dx = bEdgeEntity.endX;
//                                    Double dy = bEdgeEntity.endY;
//                                    if (Objects.equals(cx, ax) && Objects.equals(cy, ay)) {
//                                        if ((bx - ax) * (dx - cx) >= 0 && (by - ay) * (dy - cy) >= 0) {
//                                            pre++;
//                                        }
//                                    } else {
//                                        if ((ax - bx) * (cx - dx) >= 0 && (ay - by) * (cy - dy) >= 0) {
//                                            pre++;
//                                        }
//                                    }
//                                }
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

    public List<PairShape> sortBaseWeight(List<PairShape> pairShapes) {
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

    public void quicksortPairByPosition(List<PairAngleShape> sortPair, int low, int high, int index) {
        int i = low, j = high;
        PairAngleShape pivot = sortPair.get(low + (high - low) / 2);
        while (i <= j) {
            while (sortPair.get(i).position.get(index) > pivot.position.get(index)) {
                i++;
            }
            while (sortPair.get(j).position.get(index) < pivot.position.get(index)) {
                j--;
            }
            if (i <= j) {
                PairAngleShape temp = sortPair.get(i);
                sortPair.set(i, sortPair.get(j));
                sortPair.set(j, temp);
                i++;
                j--;
            }
        }
        if (low < j)
            quicksortPairByPosition(sortPair, low, j, index);
        if (i < high)
            quicksortPairByPosition(sortPair, i, high, index);
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
        ShapeEntity result = cloner.deepClone(shapeEntity);
        result.edgeEntities = result.edgeEntities.parallelStream().map(this::rotateEdge90).collect(Collectors.toList());
        return result;
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
