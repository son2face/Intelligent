package Module.Shape;

import Module.Edge.EdgeModel;
import Module.Problem.ProblemModel;
import Module.User.UserModel;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "shape", schema = "intelligent", catalog = "")
public class ShapeModel {
    private int shapeId;
    private Integer problemId;
    private Integer level;
    private Integer userId;
    private Integer code;
    private Collection<EdgeModel> edgesByShapeId;
    private ProblemModel problemByProblemId;
    private UserModel userByUserId;
    private Integer centerX;
    private Integer centerY;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shapeId", nullable = false)
    public int getShapeId() {
        return shapeId;
    }

    public void setShapeId(int shapeId) {
        this.shapeId = shapeId;
    }

    @Basic
    @Column(name = "problemId", nullable = true)
    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    @Basic
    @Column(name = "level", nullable = true)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Basic
    @Column(name = "userId", nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "code", nullable = true)
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Basic
    @Column(name = "centerX", nullable = true)
    public Integer getCenterX() {
        return centerX;
    }

    public void setCenterX(Integer centerX) {
        this.centerX = centerX;
    }

    @Basic
    @Column(name = "centerY", nullable = true)
    public Integer getCenterY() {
        return centerY;
    }

    public void setCenterY(Integer centerY) {
        this.centerY = centerY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShapeModel that = (ShapeModel) o;

        if (shapeId != that.shapeId) return false;
        if (problemId != null ? !problemId.equals(that.problemId) : that.problemId != null) return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (centerX != null ? !centerX.equals(that.centerX) : that.centerX != null) return false;
        if (centerY != null ? !centerY.equals(that.centerY) : that.centerY != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = shapeId;
        result = 31 * result + (problemId != null ? problemId.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (centerX != null ? centerX.hashCode() : 0);
        result = 31 * result + (centerY != null ? centerY.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "shapeByShapeId", orphanRemoval = true)
    public Collection<EdgeModel> getEdgesByShapeId() {
        return edgesByShapeId;
    }

    public void setEdgesByShapeId(Collection<EdgeModel> edgesByShapeId) {
        this.edgesByShapeId = edgesByShapeId;
    }

    @ManyToOne
    @JoinColumn(name = "problemId", referencedColumnName = "problemId", insertable = false, updatable = false)
    public ProblemModel getProblemByProblemId() {
        return problemByProblemId;
    }

    public void setProblemByProblemId(ProblemModel problemByProblemId) {
        this.problemByProblemId = problemByProblemId;
    }

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    public UserModel getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserModel userByUserId) {
        this.userByUserId = userByUserId;
    }
}
