import {UserEntity} from "../user/user.Entity";
import {PointEntity} from "../point/point.Entity";
import {ShapeEntity} from "../shape/shape.Entity";
import {FileEntity} from "../file/file.Entity";

export class ProblemEntity {
    problemId: number;
    status: string;
    fileId: string;
    fileEntity: FileEntity;
    userId: string;
    userEntity: UserEntity;
    pointEntities: PointEntity[];
    shapeEntities: ShapeEntity[];
    IsEdit: boolean;
    IsActive: boolean = false;
    IsSelected: boolean = false;

    constructor(problem: any = null) {
        if (problem == null) {
            this.problemId = null;
            this.status = null;
            this.fileId = null;
            this.userId = null;
            this.userEntity = new UserEntity();
            this.fileEntity = new FileEntity();
            this.pointEntities = [];
            this.shapeEntities = [];
        } else {
            this.problemId = problem.problemId;
            this.status = problem.status;
            this.fileId = problem.fileId;
            this.userId = problem.userId;
            this.userEntity = problem.userEntity;
            this.fileEntity = problem.fileEntity;
            if (problem.pointEntities != null) {
                this.pointEntities = [];
                for (let item of problem.pointEntities) {
                    this.pointEntities.push(new PointEntity(item));
                }
            }
            if (problem.shapeEntities != null) {
                this.shapeEntities = [];
                for (let item of problem.shapeEntities) {
                    this.shapeEntities.push(new ShapeEntity(item));
                }
            }
            if (this.userEntity == null) this.userEntity = new UserEntity();
            if (this.fileEntity == null) this.fileEntity = new FileEntity();
            if (this.pointEntities == null) this.pointEntities = [];
            if (this.shapeEntities == null) this.shapeEntities = [];
        }
        this.IsEdit = false;
    }
}