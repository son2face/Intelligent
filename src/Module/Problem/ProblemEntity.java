package Module.Problem;

import Module.File.FileEntity;
import Module.File.FileModel;
import Module.Point.PointEntity;
import Module.Point.PointModel;
import Module.Shape.ShapeEntity;
import Module.Shape.ShapeModel;
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
public class ProblemEntity implements Serializable {
    public int problemId;
    public String status;
    public Integer fileId;
    public Integer userId;
    public FileEntity fileEntity;
    public UserEntity userEntity;
    public List<PointEntity> pointEntities;
    public List<ShapeEntity> shapeEntities;
    public Integer frameId;
    public Integer width;
    public Integer height;

    public ProblemEntity() {
    }

    public ProblemEntity(int problemId, String status, Integer fileId, Integer userId, Integer frameId, Integer width, Integer height) {
        this.problemId = problemId;
        this.status = status;
        this.fileId = fileId;
        this.userId = userId;
        this.frameId = frameId;
        this.width = width;
        this.height = height;
    }

    public ProblemEntity(ProblemModel ProblemModel, Object... objects) {
        this.problemId = ProblemModel.getProblemId();
        this.status = ProblemModel.getStatus();
        this.fileId = ProblemModel.getFileId();
        this.userId = ProblemModel.getUserId();
        this.frameId = ProblemModel.getFrameId();
        this.height = ProblemModel.getHeight();
        this.width = ProblemModel.getWidth();
        for (Object object : objects) {
            if (object instanceof FileModel) {
                this.fileEntity = new FileEntity((FileModel) object);
            } else if (object instanceof UserModel) {
                this.userEntity = new UserEntity((UserModel) object);
            } else if (object instanceof Collection) {
                for (Object o : (Collection<Object>) object) {
                    if (o instanceof PointModel) {
                        this.pointEntities = ((Collection<PointModel>) object).parallelStream().map(PointEntity::new).collect(Collectors.toList());
                        break;
                    }
                    if (o instanceof ShapeModel) {
                        this.shapeEntities = ((Collection<ShapeModel>) object).parallelStream().map(ShapeEntity::new).collect(Collectors.toList());
                        break;
                    }
                }
            }
        }
    }

    public void setFileEntity(FileEntity fileEntity) {
        this.fileEntity = fileEntity;
    }

    public ProblemModel toModel() {
        ProblemModel ProblemModel = new ProblemModel();
        ProblemModel.setProblemId(problemId);
        ProblemModel.setStatus(status);
        ProblemModel.setFileId(fileId);
        ProblemModel.setUserId(userId);
        ProblemModel.setFrameId(frameId);
        ProblemModel.setWidth(width);
        ProblemModel.setHeight(height);
//        if (fileEntity != null) ProblemModel.setFileByFileId(fileEntity.toModel());
//        if (userEntity != null) ProblemModel.setUserByUserId(userEntity.toModel());
//        if (pointEntities != null)
//            ProblemModel.setPointsByProblemId(pointEntities.parallelStream().map(PointEntity::toModel).collect(Collectors.toList()));
//        if (shapeEntities != null)
//            ProblemModel.setShapesByProblemId(shapeEntities.parallelStream().map(ShapeEntity::toModel).collect(Collectors.toList()));
        return ProblemModel;
    }
}
