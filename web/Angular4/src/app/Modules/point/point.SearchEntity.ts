import {FilterEntity} from "../../Shared/Filter.Entity";

export class SearchpointEntity extends FilterEntity {
    pointId: number;
    x: string;
    y: string;
    problemId: string;

    constructor(point: any = null) {
        super(point);
        this.pointId = point == null ? null : point.pointId;
        this.x = point == null ? null : point.x;
        this.y = point == null ? null : point.y;
        this.problemId = point == null ? null : point.problemId;
    }
}