package Module.User;

import Module.File.FileEntity;
import Module.Problem.ProblemEntity;
import Module.Shape.ShapeEntity;

import java.io.Serializable;
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
    private List<FileEntity> fileEntityList;
    private List<ProblemEntity> problemEntityList;
    private List<ShapeEntity> shapeEntityList;

    public UserEntity() {
    }

    public UserEntity(int userId, String userName, String passWord) {
        this.userId = userId;
        this.userName = userName;
        this.passWord = passWord;
    }

    public UserEntity(UserModel UserModel) {
        this.userId = UserModel.getUserId();
        this.userName = UserModel.getUserName();
        this.passWord = UserModel.getPassWord();
        if (UserModel.getFilesByUserId() != null)
            this.fileEntityList = UserModel.getFilesByUserId().parallelStream().map(FileEntity::new).collect(Collectors.toList());
        if (UserModel.getProblemsByUserId() != null)
            this.problemEntityList = UserModel.getProblemsByUserId().parallelStream().map(ProblemEntity::new).collect(Collectors.toList());
        if (UserModel.getShapesByUserId() != null)
            this.shapeEntityList = UserModel.getShapesByUserId().parallelStream().map(ShapeEntity::new).collect(Collectors.toList());
    }

    public UserModel toModel() {
        UserModel UserModel = new UserModel();
        UserModel.setUserId(userId);
        UserModel.setUserName(userName);
        UserModel.setPassWord(passWord);
//        if (fileEntityList != null)
//            UserModel.setFilesByUserId(fileEntityList.parallelStream().map(FileEntity::toModel).collect(Collectors.toList()));
//        if (problemEntityList != null)
//            UserModel.setProblemsByUserId(problemEntityList.parallelStream().map(ProblemEntity::toModel).collect(Collectors.toList()));
//        if (shapeEntityList != null)
//            UserModel.setShapesByUserId(shapeEntityList.parallelStream().map(ShapeEntity::toModel).collect(Collectors.toList()));
        return UserModel;
    }

}
