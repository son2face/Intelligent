import {ProblemEntity} from "../problem/problem.Entity";
import {UserEntity} from "../user/user.Entity";
import {EdgeEntity} from "../edge/edge.Entity";

export class ShapeEntity {
    shapeId: number;
    problemId: string;
    level: string;
    userId: string;
    problemEntity: ProblemEntity;
    userEntity: UserEntity;
    edgeEntities: EdgeEntity[];
    IsEdit: boolean;
    IsActive: boolean = false;
    IsSelected: boolean = false;
    code : number;
    constructor(shape: any = null) {
        if (shape == null) {
            this.shapeId = null;
            this.problemId = null;
            this.level = null;
            this.userId = null;
            this.code = null;
            this.problemEntity = new ProblemEntity();
            this.userEntity = new UserEntity();
            this.edgeEntities = [];
        } else {
            this.shapeId = shape.shapeId;
            this.problemId = shape.problemId;
            this.level = shape.level;
            this.code = shape.code;
            this.userId = shape.userId;
            this.problemEntity = shape.problemEntity;
            this.userEntity = shape.userEntity;
            if (shape.edgeEntities != null) {
                this.edgeEntities = [];
                for (let item of shape.edgeEntities) {
                    this.edgeEntities.push(new EdgeEntity(item));
                }
            }
            if (this.problemEntity == null) this.problemEntity = new ProblemEntity();
            if (this.userEntity == null) this.userEntity = new UserEntity();
            if (this.edgeEntities == null) this.edgeEntities = [];
        }
        this.IsEdit = false;
    }
}