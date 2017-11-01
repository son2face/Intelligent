import {ProblemEntity} from "../problem/problem.Entity";

export class PointEntity {
    pointId: number;
    x: string;
    y: string;
    problemId: string;
    problemEntity: ProblemEntity;
    IsEdit: boolean;
    IsActive: boolean = false;
    IsSelected: boolean = false;

    constructor(point: any = null) {
        if (point == null) {
            this.pointId = null;
            this.x = null;
            this.y = null;
            this.problemId = null;
            this.problemEntity = new ProblemEntity();
        } else {
            this.pointId = point.pointId;
            this.x = point.x;
            this.y = point.y;
            this.problemId = point.problemId;
            this.problemEntity = point.problemEntity;
            if (this.problemEntity == null) this.problemEntity = new ProblemEntity();
        }
        this.IsEdit = false;
    }
}