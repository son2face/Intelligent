package Console2;

import java.util.HashMap;

class TreeShape {
    public HashMap<PairShape, TreeShape> childs;
    public TreeShape parent;

    public TreeShape(HashMap<PairShape, TreeShape> childs, TreeShape parent) {
        this.childs = childs;
        this.parent = parent;
    }

    public TreeShape(TreeShape parent) {
        this.parent = parent;
        this.childs = new HashMap<>();
    }
}
