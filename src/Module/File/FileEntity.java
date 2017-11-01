package Module.File;

import Module.Problem.ProblemEntity;
import Module.Problem.ProblemModel;
import Module.User.UserEntity;
import Module.User.UserModel;
import org.infinispan.commons.util.Base64;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class FileEntity implements Serializable {

    public int fileId;
    public String name;
    public String data;
    public Timestamp createdTime;
    public String type;
    public Timestamp expiredTime;
    public Integer userId;
    public UserEntity userEntity;
    public List<ProblemEntity> problemEntityList;


    public FileEntity() {
    }

    public FileEntity(int fileId, String name, byte[] data, Timestamp createdTime, String type, Timestamp expiredTime, Integer userId) {
        this.fileId = fileId;
        this.name = name;
        if (data != null) this.data = Base64.encodeBytes(data);
        this.createdTime = createdTime;
        this.type = type;
        this.expiredTime = expiredTime;
        this.userId = userId;
    }

    public FileEntity(FileModel FileModel, Object... objects) {
        this.fileId = FileModel.getFileId();
        this.name = FileModel.getName();
        if (FileModel.getData() != null) this.data = Base64.encodeBytes(FileModel.getData());
        this.createdTime = FileModel.getCreatedTime();
        this.type = FileModel.getType();
        this.expiredTime = FileModel.getExpiredTime();
        this.userId = FileModel.getUserId();
        for (Object object : objects) {
            if (object instanceof UserModel) {
                this.userEntity = new UserEntity(FileModel.getUserByUserId());
            } else if (object instanceof Collection) {
                if (object.getClass().getGenericSuperclass() instanceof ProblemModel)
                    this.problemEntityList = FileModel.getProblemsByFileId().parallelStream().map(ProblemEntity::new).collect(Collectors.toList());
            }
        }
    }

    public FileModel toModel() {
        FileModel FileModel = new FileModel();
        FileModel.setFileId(fileId);
        FileModel.setName(name);
        if (data != null) FileModel.setData(Base64.decode(data));
        FileModel.setCreatedTime(createdTime);
        FileModel.setType(type);
        FileModel.setExpiredTime(expiredTime);
        FileModel.setUserId(userId);
//        if (userEntity != null) FileModel.setUserByUserId(userEntity.toModel());
//        if (problemEntityList != null)
//            FileModel.setProblemsByFileId(problemEntityList.parallelStream().map(ProblemEntity::toModel).collect(Collectors.toList()));
        return FileModel;
    }
}
