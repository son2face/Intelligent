import {FileEntity} from "../file/file.Entity";
import {ProblemEntity} from "../problem/problem.Entity";
import {ShapeEntity} from "../shape/shape.Entity";

export class UserEntity {
    userId: number;
    userName: string;
    passWord: string;
    fileEntities: FileEntity[];
    problemEntities: ProblemEntity[];
    shapeEntities: ShapeEntity[];
    IsEdit: boolean;
    IsActive: boolean = false;
    IsSelected: boolean = false;

    constructor(user: any = null) {
        if (user == null) {
            this.userId = null;
            this.userName = null;
            this.passWord = null;
            this.fileEntities = [];
            this.problemEntities = [];
            this.shapeEntities = [];
        } else {
            this.userId = user.userId;
            this.userName = user.userName;
            this.passWord = user.passWord;
            if (user.fileEntities != null) {
                this.fileEntities = [];
                for (let item of user.fileEntities) {
                    this.fileEntities.push(new FileEntity(item));
                }
            }
            if (user.problemEntities != null) {
                this.problemEntities = [];
                for (let item of user.problemEntities) {
                    this.problemEntities.push(new ProblemEntity(item));
                }
            }
            if (user.shapeEntities != null) {
                this.shapeEntities = [];
                for (let item of user.shapeEntities) {
                    this.shapeEntities.push(new ShapeEntity(item));
                }
            }
            if (this.fileEntities == null) this.fileEntities = [];
            if (this.problemEntities == null) this.problemEntities = [];
            if (this.shapeEntities == null) this.shapeEntities = [];
        }
        this.IsEdit = false;
    }
}