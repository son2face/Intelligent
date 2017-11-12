package Module.Problem;

import Module.File.FileModel;
import Module.Point.PointModel;
import Module.Shape.ShapeModel;
import Module.User.UserModel;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "problem", schema = "intelligent", catalog = "")
public class ProblemModel {
    private int problemId;
    private String status;
    private Integer fileId;
    private Integer userId;
    private Integer frameId;
    private Integer width;
    private Integer height;
    private Collection<PointModel> pointsByProblemId;
    private FileModel fileByFileId;
    private UserModel userByUserId;
    private Collection<ShapeModel> shapesByProblemId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "frameId", nullable = true)
    public Integer getFrameId() {
        return frameId;
    }

    public void setFrameId(Integer frameId) {
        this.frameId = frameId;
    }

    @Basic
    @Column(name = "width", nullable = true)
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Basic
    @Column(name = "height", nullable = true)
    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
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
        if (frameId != null ? !frameId.equals(that.frameId) : that.frameId != null) return false;
        if (width != null ? !width.equals(that.width) : that.width != null) return false;
        if (height != null ? !height.equals(that.height) : that.height != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = problemId;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (fileId != null ? fileId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (frameId != null ? frameId.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "problemByProblemId")
    public Collection<PointModel> getPointsByProblemId() {
        return pointsByProblemId;
    }

    public void setPointsByProblemId(Collection<PointModel> pointsByProblemId) {
        this.pointsByProblemId = pointsByProblemId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fileId", referencedColumnName = "fileId", insertable = false, updatable = false)
    public FileModel getFileByFileId() {
        return fileByFileId;
    }

    public void setFileByFileId(FileModel fileByFileId) {
        this.fileByFileId = fileByFileId;
    }

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    public UserModel getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserModel userByUserId) {
        this.userByUserId = userByUserId;
    }

    @OneToMany(mappedBy = "problemByProblemId", orphanRemoval = true)
    public Collection<ShapeModel> getShapesByProblemId() {
        return shapesByProblemId;
    }

    public void setShapesByProblemId(Collection<ShapeModel> shapesByProblemId) {
        this.shapesByProblemId = shapesByProblemId;
    }
}
