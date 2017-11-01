package Module.Edge;

import Module.Shape.ShapeEntity;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class EdgeEntity implements Serializable {
    public int edgeId;
    public Double startX;
    public Double startY;
    public Double endX;
    public Double endY;
    public Integer shapeId;
    public ShapeEntity shapeEntity;


    public EdgeEntity() {
    }

    public EdgeEntity(int edgeId, Double startX, Double startY, Double endX, Double endY, Integer shapeId) {
        this.edgeId = edgeId;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.shapeId = shapeId;
    }

    public EdgeEntity(EdgeModel EdgeModel, Object... objects) {
        this.edgeId = EdgeModel.getEdgeId();
        this.startX = EdgeModel.getStartX();
        this.endX = EdgeModel.getEndX();
        this.startY = EdgeModel.getStartY();
        this.endY = EdgeModel.getEndY();
        this.shapeId = EdgeModel.getShapeId();
        for (Object object : objects) {
            if (object instanceof EdgeModel) {
                this.shapeEntity = new ShapeEntity(EdgeModel.getShapeByShapeId());
            }
        }
    }

    public EdgeModel toModel() {
        EdgeModel EdgeModel = new EdgeModel();
        EdgeModel.setEdgeId(edgeId);
        EdgeModel.setStartX(startX);
        EdgeModel.setEndX(endX);
        EdgeModel.setStartY(startY);
        EdgeModel.setEndY(endY);
        EdgeModel.setShapeId(shapeId);
//        if (shapeEntity != null) EdgeModel.setShapeByShapeId(shapeEntity.toModel());
        return EdgeModel;
    }
}
