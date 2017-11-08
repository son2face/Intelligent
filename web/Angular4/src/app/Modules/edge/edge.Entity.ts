import {ShapeEntity} from "../shape/shape.Entity";

export class EdgeEntity {
    edgeId: number;
    startX: number;
    startY: number;
    endX: number;
    endY: number;
    shapeId: string;
    centerX: number;
    centerY: number;
    shapeEntity: ShapeEntity;
    IsEdit: boolean;
    IsActive: boolean = false;
    IsSelected: boolean = false;

    constructor(edge: any = null) {
        if (edge == null) {
            this.edgeId = null;
            this.startX = null;
            this.startY = null;
            this.endX = null;
            this.endY = null;
            this.shapeId = null;
            this.shapeEntity = new ShapeEntity();
        } else {
            this.edgeId = edge.edgeId;
            this.startX = edge.startX;
            this.startY = edge.startY;
            this.endX = edge.endX;
            this.endY = edge.endY;
            this.shapeId = edge.shapeId;
            this.shapeEntity = edge.shapeEntity;
            if (this.shapeEntity == null) this.shapeEntity = new ShapeEntity();
        }
        if (this.endX != null && this.startX != null) {
            this.centerX = (this.endX + this.startX) / 2;
        }
        if (this.endY != null && this.startY != null) {
            this.centerY = (this.endY + this.startY) / 2;
        }
        this.IsEdit = false;
    }
}