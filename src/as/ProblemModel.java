package as;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "problem", schema = "intelligent", catalog = "")
public class ProblemModel {
    private int problemId;
    private String status;
    private Integer fileId;
    private Integer userId;
    private Collection<PointModel> pointsByProblemId;
    private FileModel fileByFileId;
    private UserModel userByUserId;
    private Collection<ShapeModel> shapesByProblemId;

    @Id
    @Column(name = "problemId", nullable = false)
    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    @Basic
    @Column(name = "status", nullable = true, length = 45)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "fileId", nullable = true)
    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    @Basic
    @Column(name = "userId", nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProblemModel that = (ProblemModel) o;

        if (problemId != that.problemId) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (fileId != null ? !fileId.equals(that.fileId) : that.fileId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = problemId;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (fileId != null ? fileId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "problemByProblemId")
    public Collection<PointModel> getPointsByProblemId() {
        return pointsByProblemId;
    }

    public void setPointsByProblemId(Collection<PointModel> pointsByProblemId) {
        this.pointsByProblemId = pointsByProblemId;
    }

    @ManyToOne
    @JoinColumn(name = "fileId", referencedColumnName = "fileId")
    public FileModel getFileByFileId() {
        return fileByFileId;
    }

    public void setFileByFileId(FileModel fileByFileId) {
        this.fileByFileId = fileByFileId;
    }

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    public UserModel getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserModel userByUserId) {
        this.userByUserId = userByUserId;
    }

    @OneToMany(mappedBy = "problemByProblemId")
    public Collection<ShapeModel> getShapesByProblemId() {
        return shapesByProblemId;
    }

    public void setShapesByProblemId(Collection<ShapeModel> shapesByProblemId) {
        this.shapesByProblemId = shapesByProblemId;
    }
}
