package Console2;

import Module.Shape.ShapeEntity;

import java.util.List;

class PairShape {
    public ShapeEntity a;
    public ShapeEntity b;
    public ShapeEntity ab;
    public List<ShapeEntity> ListShape;
    public PairShape(ShapeEntity a, ShapeEntity b) {
        this.a = a;
        this.b = b;
    }

    public PairShape(ShapeEntity a, ShapeEntity b, ShapeEntity ab) {
        this.a = a;
        this.b = b;
        this.ab = ab;
    }

    public PairShape(ShapeEntity a, ShapeEntity b, ShapeEntity ab, List<ShapeEntity> listShape) {
        this.a = a;
        this.b = b;
        this.ab = ab;
        ListShape = listShape;
    }

    public PairShape(ShapeEntity a, ShapeEntity b, List<ShapeEntity> listShape) {
        this.a = a;
        this.b = b;
        ListShape = listShape;
    }
}
