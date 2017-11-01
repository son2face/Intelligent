import {FilterEntity} from "../../Shared/Filter.Entity";

export class SearchshapeEntity extends FilterEntity {
    shapeId: number;
    problemId: string;
    level: string;
    userId: string;

    constructor(shape: any = null) {
        super(shape);
        this.shapeId = shape == null ? null : shape.shapeId;
        this.problemId = shape == null ? null : shape.problemId;
        this.level = shape == null ? null : shape.level;
        this.userId = shape == null ? null : shape.userId;
    }
}