package Module.Point;

import Module.Problem.ProblemEntity;

import java.io.Serializable;

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

    public PointEntity(PointModel PointModel) {
        this.pointId = PointModel.getPointId();
        this.x = PointModel.getX();
        this.y = PointModel.getY();
        this.problemId = PointModel.getProblemId();
        if (PointModel.getProblemByProblemId() != null) this.problemEntity = new ProblemEntity(PointModel.getProblemByProblemId());
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
