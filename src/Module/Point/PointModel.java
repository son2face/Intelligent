package Module.Point;

import Module.Problem.ProblemModel;

import javax.persistence.*;

@Entity
@Table(name = "point", schema = "intelligent", catalog = "")
public class PointModel {
    private int pointId;
    private Integer x;
    private Integer y;
    private Integer problemId;
    private ProblemModel problemByProblemId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pointId", nullable = false)
    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    @Basic
    @Column(name = "x", nullable = true)
    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    @Basic
    @Column(name = "y", nullable = true)
    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Basic
    @Column(name = "problemId", nullable = true)
    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointModel that = (PointModel) o;

        if (pointId != that.pointId) return false;
        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;
        if (problemId != null ? !problemId.equals(that.problemId) : that.problemId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pointId;
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (problemId != null ? problemId.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "problemId", referencedColumnName = "problemId", insertable=false, updatable=false)
    public ProblemModel getProblemByProblemId() {
        return problemByProblemId;
    }

    public void setProblemByProblemId(ProblemModel problemByProblemId) {
        this.problemByProblemId = problemByProblemId;
    }
}
