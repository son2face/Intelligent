package Module.Problem;

import Module.File.FileModel;
import Module.File.FileEntity;
import Module.Point.PointModel;
import Module.Point.PointEntity;
import Module.Shape.ShapeModel;
import Module.Shape.ShapeEntity;
import Module.User.UserEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class ProblemEntity implements Serializable {
    public int problemId;
    public String status;
    public Integer fileId;
    public Integer userId;
    public FileEntity fileEntity;
    public UserEntity userEntity;
    public List<PointEntity> pointEntityList;
    public List<ShapeEntity> shapeEntityList;

    public void setFileEntity(FileEntity fileEntity) {
        this.fileEntity = fileEntity;
    }

    public ProblemEntity() {
    }

    public ProblemEntity(int problemId, String status, Integer fileId, Integer userId) {
        this.problemId = problemId;
        this.status = status;
        this.fileId = fileId;
        this.userId = userId;
    }

    public ProblemEntity(ProblemModel ProblemModel, Object... objects) {
        this.problemId = ProblemModel.getProblemId();
        this.status = ProblemModel.getStatus();
        this.fileId = ProblemModel.getFileId();
        this.userId = ProblemModel.getUserId();
        for (Object object : objects) {
            if (object instanceof FileModel) {
                this.fileEntity = new FileEntity(ProblemModel.getFileByFileId());
            } else if (object instanceof FileModel) {
                this.userEntity = new UserEntity(ProblemModel.getUserByUserId());
            } else if (object instanceof Collection) {
                for (Object o : (Collection<Object>) object) {
                    if (o instanceof PointModel) {
                        this.pointEntityList = ProblemModel.getPointsByProblemId().parallelStream().map(PointEntity::new).collect(Collectors.toList());
                        break;
                    }
                    if (o instanceof ShapeModel) {
                        this.shapeEntityList = ProblemModel.getShapesByProblemId().parallelStream().map(ShapeEntity::new).collect(Collectors.toList());
                        break;
                    }
                }
            }
        }
    }

    public ProblemModel toModel() {
        ProblemModel ProblemModel = new ProblemModel();
        ProblemModel.setProblemId(problemId);
        ProblemModel.setStatus(status);
        ProblemModel.setFileId(fileId);
        ProblemModel.setUserId(userId);
//        if (fileEntity != null) ProblemModel.setFileByFileId(fileEntity.toModel());
//        if (userEntity != null) ProblemModel.setUserByUserId(userEntity.toModel());
//        if (pointEntityList != null)
//            ProblemModel.setPointsByProblemId(pointEntityList.parallelStream().map(PointEntity::toModel).collect(Collectors.toList()));
//        if (shapeEntityList != null)
//            ProblemModel.setShapesByProblemId(shapeEntityList.parallelStream().map(ShapeEntity::toModel).collect(Collectors.toList()));
        return ProblemModel;
    }
}
