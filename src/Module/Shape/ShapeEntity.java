package Module.Shape;

import Module.Edge.EdgeEntity;
import Module.Problem.ProblemEntity;
import Module.User.UserEntity;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class ShapeEntity implements Serializable {
    public int shapeId;
    public Integer problemId;
    public Integer level;
    public Integer userId;
    public List<EdgeEntity> edgeEntityList;
    public ProblemEntity problemEntity;
    public UserEntity userEntity;
    public Integer code;

    public ShapeEntity() {
    }

    public ShapeEntity(int shapeId, Integer problemId, Integer level, Integer userId, Integer code) {
        this.shapeId = shapeId;
        this.level = level;
        this.problemId = problemId;
        this.userId = userId;
        this.code = code;
    }

    public ShapeEntity(ShapeModel ShapeModel) {
        this.shapeId = ShapeModel.getShapeId();
        this.problemId = ShapeModel.getProblemId();
        this.level = ShapeModel.getLevel();
        this.userId = ShapeModel.getUserId();
        this.code = ShapeModel.getCode();
        if (ShapeModel.getUserByUserId() != null) this.userEntity = new UserEntity(ShapeModel.getUserByUserId());
        if (ShapeModel.getProblemByProblemId() != null)
            this.problemEntity = new ProblemEntity(ShapeModel.getProblemByProblemId());
        if (ShapeModel.getEdgesByShapeId() != null)
            this.edgeEntityList = ShapeModel.getEdgesByShapeId().parallelStream().map(EdgeEntity::new).collect(Collectors.toList());
    }

    public ShapeModel toModel() {
        ShapeModel ShapeModel = new ShapeModel();
        ShapeModel.setShapeId(shapeId);
        ShapeModel.setLevel(level);
        ShapeModel.setCode(code);
        ShapeModel.setProblemId(problemId);
//        if (problemEntity != null) ShapeModel.setProblemByProblemId(this.problemEntity.toModel());
//        if (userEntity != null) ShapeModel.setUserByUserId(this.userEntity.toModel());
//        if (edgeEntityList != null)
//            ShapeModel.setEdgesByShapeId(edgeEntityList.parallelStream().map(EdgeEntity::toModel).collect(Collectors.toList()));
        return ShapeModel;
    }
}
