import {UserEntity} from "../user/user.Entity";

export class FileEntity {
    fileId: number;
    name: string;
    data: string;
    createdTime: string;
    type: string;
    expriedTime: string;
    userId: string;
    userEntity: UserEntity;
    IsEdit: boolean;
    IsActive: boolean = false;
    IsSelected: boolean = false;

    constructor(file: any = null) {
        if (file == null) {
            this.fileId = null;
            this.name = null;
            this.data = null;
            this.createdTime = null;
            this.type = null;
            this.expriedTime = null;
            this.userId = null;
            this.userEntity = new UserEntity();
        } else {
            this.fileId = file.fileId;
            this.name = file.name;
            this.data = file.data;
            this.createdTime = file.createdTime;
            this.type = file.type;
            this.expriedTime = file.expriedTime;
            this.userId = file.userId;
            this.userEntity = file.userEntity;
            if (this.userEntity == null) this.userEntity = new UserEntity();
        }
        this.IsEdit = false;
    }
}