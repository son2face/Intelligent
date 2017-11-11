package Console2;

import java.util.HashMap;

class TreeShape {
    public int idShape;
    public HashMap<Integer, TreeShape> childs;
    public TreeShape parent;

    public TreeShape(int idShape, HashMap<Integer, TreeShape> childs, TreeShape parent) {
        this.idShape = idShape;
        this.childs = childs;
        this.parent = parent;
    }

    public TreeShape(int idShape, TreeShape parent) {
        this.idShape = idShape;
        this.parent = parent;
        this.childs = new HashMap<>();
    }
}
