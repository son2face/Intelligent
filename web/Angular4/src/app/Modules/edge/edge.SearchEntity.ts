import {FilterEntity} from "../../Shared/Filter.Entity";

export class SearchedgeEntity extends FilterEntity {
    edgeId: number;
    startX: string;
    startY: string;
    endX: string;
    endY: string;
    shapeId: string;

    constructor(edge: any = null) {
        super(edge);
        this.edgeId = edge == null ? null : edge.edgeId;
        this.startX = edge == null ? null : edge.startX;
        this.startY = edge == null ? null : edge.startY;
        this.endX = edge == null ? null : edge.endX;
        this.endY = edge == null ? null : edge.endY;
        this.shapeId = edge == null ? null : edge.shapeId;
    }
}