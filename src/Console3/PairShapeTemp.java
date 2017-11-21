package Console3;

import Module.Shape.ShapeEntity;

public class PairShapeTemp {
    int minIndex;
    int maxIndex;
    ShapeEntity shapeEntityA;
    ShapeEntity shapeEntityB;

    public PairShapeTemp(int minIndex, int maxIndex) {
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
    }

    public PairShapeTemp(int minIndex, int maxIndex, ShapeEntity shapeEntityA, ShapeEntity shapeEntityB) {
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
        this.shapeEntityA = shapeEntityA;
        this.shapeEntityB = shapeEntityB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PairShapeTemp that = (PairShapeTemp) o;

        if (minIndex != that.minIndex) return false;
        if (maxIndex != that.maxIndex) return false;
        if (!shapeEntityA.equals(that.shapeEntityA)) return false;
        return shapeEntityB.equals(that.shapeEntityB);
    }

    @Override
    public int hashCode() {
        int result = shapeEntityA.shapeId;
        result = 31 * result + shapeEntityB.shapeId;
        return result;
    }
}
