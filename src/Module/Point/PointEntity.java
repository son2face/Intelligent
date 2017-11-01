package Module.Point;

import Module.Edge.EdgeModel;
import Module.File.FileEntity;
import Module.File.FileModel;
import Module.Problem.ProblemEntity;
import Module.Problem.ProblemModel;
import Module.Shape.ShapeEntity;
import Module.Shape.ShapeModel;
import Module.User.UserEntity;
import Module.User.UserModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class PointEntity implements Serializable {
    public int pointId;
    public Integer x;
    public Integer y;
    public Integer problemId;
    public ProblemEntity problemEntity;


    public PointEntity() {
    }

    public PointEntity(int pointId, Integer x, Integer y, Integer problemId) {
        this.pointId = pointId;
        this.x = x;
        this.y = y;
        this.problemId = problemId;
    }

    public PointEntity(PointModel PointModel, Object... objects) {
        this.pointId = PointModel.getPointId();
        this.x = PointModel.getX();
        this.y = PointModel.getY();
        this.problemId = PointModel.getProblemId();
        for (Object object : objects) {
            if (object instanceof ProblemModel) {
                this.problemEntity = new ProblemEntity((ProblemModel) object);
            }
        }
    }

    public PointModel toModel() {
        PointModel PointModel = new PointModel();
        PointModel.setPointId(pointId);
        PointModel.setX(x);
        PointModel.setY(y);
        PointModel.setProblemId(problemId);
//        if (problemEntity != null) PointModel.setProblemByProblemId(problemEntity.toModel());
        return PointModel;
    }
}
