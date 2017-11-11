package Module.Shape;

import Module.Edge.EdgeEntity;
import Module.Edge.EdgeModel;
import Module.Problem.ProblemEntity;
import Module.Problem.ProblemModel;
import Module.User.UserEntity;
import Module.User.UserModel;

import java.io.Serializable;
import java.util.Collection;
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
    public List<EdgeEntity> edgeEntities;
    public ProblemEntity problemEntity;
    public UserEntity userEntity;
    public Integer code;
    private Integer centerX;
    private Integer centerY;
    public int weight;
    public ShapeEntity combineA;
    public ShapeEntity combineB;
    public ShapeEntity() {
    }

    public ShapeEntity(int shapeId, Integer problemId, Integer level, Integer userId, Integer code, Integer centerX, Integer centerY) {
        this.shapeId = shapeId;
        this.level = level;
        this.problemId = problemId;
        this.userId = userId;
        this.code = code;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public ShapeEntity(ShapeModel ShapeModel, Object... objects) {
        this.shapeId = ShapeModel.getShapeId();
        this.problemId = ShapeModel.getProblemId();
        this.level = ShapeModel.getLevel();
        this.userId = ShapeModel.getUserId();
        this.code = ShapeModel.getCode();
        this.centerX = ShapeModel.getCenterX();
        this.centerY = ShapeModel.getCenterY();
        for (Object object : objects) {
            if (object instanceof UserModel) {
                this.userEntity = new UserEntity((UserModel) object);
            } else if (object instanceof ProblemModel) {
                this.problemEntity = new ProblemEntity((ProblemModel) object);
            } else if (object instanceof Collection) {
                for (Object o : (Collection<Object>) object) {
                    if (o instanceof EdgeModel) {
                        this.edgeEntities = ((Collection<EdgeModel>) object).parallelStream().map(EdgeEntity::new).collect(Collectors.toList());
                        break;
                    }
                }
            }
        }
    }

    public ShapeModel toModel() {
        ShapeModel ShapeModel = new ShapeModel();
        ShapeModel.setShapeId(shapeId);
        ShapeModel.setLevel(level);
        ShapeModel.setCenterX(centerX);
        ShapeModel.setCenterY(centerY);
        ShapeModel.setCode(code);
        ShapeModel.setProblemId(problemId);
//        if (problemEntity != null) ShapeModel.setProblemByProblemId(this.problemEntity.toModel());
//        if (userEntity != null) ShapeModel.setUserByUserId(this.userEntity.toModel());
//        if (edgeEntities != null)
//            ShapeModel.setEdgesByShapeId(edgeEntities.parallelStream().map(EdgeEntity::toModel).collect(Collectors.toList()));
        return ShapeModel;
    }
}
