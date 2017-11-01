package Module.User;

import Module.File.FileEntity;
import Module.File.FileModel;
import Module.Point.PointModel;
import Module.Problem.ProblemEntity;
import Module.Problem.ProblemModel;
import Module.Shape.ShapeEntity;
import Module.Shape.ShapeModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class UserEntity implements Serializable {
    public int userId;
    public String userName;
    public String passWord;
    private List<FileEntity> fileEntities;
    private List<ProblemEntity> problemEntities;
    private List<ShapeEntity> shapeEntities;

    public UserEntity() {
    }

    public UserEntity(int userId, String userName, String passWord) {
        this.userId = userId;
        this.userName = userName;
        this.passWord = passWord;
    }

    public UserEntity(UserModel UserModel, Object... objects) {
        this.userId = UserModel.getUserId();
        this.userName = UserModel.getUserName();
        this.passWord = UserModel.getPassWord();
        for (Object object : objects) {
            if (object instanceof Collection) {
                for (Object o : (Collection<Object>) object) {
                    if (o instanceof PointModel) {
                        this.fileEntities = ((Collection<FileModel>) object).parallelStream().map(FileEntity::new).collect(Collectors.toList());
                        break;
                    }
                    if (o instanceof ShapeModel) {
                        this.problemEntities = ((Collection<ProblemModel>) object).parallelStream().map(ProblemEntity::new).collect(Collectors.toList());
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

    public UserModel toModel() {
        UserModel UserModel = new UserModel();
        UserModel.setUserId(userId);
        UserModel.setUserName(userName);
        UserModel.setPassWord(passWord);
//        if (fileEntities != null)
//            UserModel.setFilesByUserId(fileEntities.parallelStream().map(FileEntity::toModel).collect(Collectors.toList()));
//        if (problemEntities != null)
//            UserModel.setProblemsByUserId(problemEntities.parallelStream().map(ProblemEntity::toModel).collect(Collectors.toList()));
//        if (shapeEntities != null)
//            UserModel.setShapesByUserId(shapeEntities.parallelStream().map(ShapeEntity::toModel).collect(Collectors.toList()));
        return UserModel;
    }

}
