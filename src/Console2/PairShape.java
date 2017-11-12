package Console2;

import Module.Edge.EdgeEntity;
import Module.Shape.ShapeEntity;

import java.util.List;

class PairShape {
    public ShapeEntity shapeEntityA;
    public ShapeEntity shapeEntityB;
    public EdgeEntity edgeEntityA;
    public EdgeEntity edgeEntityB;
    public ShapeEntity primitiveShapeB;
    public int type;
    public ShapeEntity shapeEntityAB;
    public List<ShapeEntity> ListShape;

    public PairShape(ShapeEntity a, ShapeEntity b) {
        this.shapeEntityA = a;
        this.shapeEntityB = b;
    }

    public PairShape(ShapeEntity a, ShapeEntity b, ShapeEntity shapeEntityAB) {
        this.shapeEntityA = a;
        this.shapeEntityB = b;
        this.shapeEntityAB = shapeEntityAB;
    }

    public PairShape(ShapeEntity a, ShapeEntity b, ShapeEntity shapeEntityAB, List<ShapeEntity> listShape) {
        this.shapeEntityA = a;
        this.shapeEntityB = b;
        this.shapeEntityAB = shapeEntityAB;
        ListShape = listShape;
    }

    public PairShape(ShapeEntity a, ShapeEntity b, List<ShapeEntity> listShape) {
        this.shapeEntityA = a;
        this.shapeEntityB = b;
        ListShape = listShape;
    }

    @Override
    public int hashCode() {
        int result = shapeEntityB.shapeId;
        result = 31 * result + edgeEntityA.edgeId;
        result = 31 * result + edgeEntityB.edgeId;
        result = 31 * result + type;
        return result;
    }
}
