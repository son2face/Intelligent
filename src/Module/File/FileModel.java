package Module.File;

import Module.Problem.ProblemModel;
import Module.User.UserModel;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "file", schema = "intelligent", catalog = "")
public class FileModel {
    private int fileId;
    private String name;
    private byte[] data;
    private Timestamp createdTime;
    private String type;
    private Timestamp expiredTime;
    private Integer userId;
    private UserModel userByUserId;
    private Collection<ProblemModel> problemsByFileId;

    public FileModel() {

    }

    public FileModel(int fileId, String name, Date createdTime, String type, Date expiredTime, Integer userId) {
        this.fileId = fileId;
        this.name = name;
        if (createdTime != null) this.createdTime = new Timestamp(createdTime.getTime());
        this.type = type;
        if (expiredTime != null) this.expiredTime = new Timestamp(expiredTime.getTime());
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileId", nullable = false)
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 1000)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "data", nullable = true)
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Basic
    @Column(name = "createdTime", nullable = true)
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Basic
    @Column(name = "type", nullable = true, length = 100)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "expiredTime", nullable = true)
    public Timestamp getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Timestamp expiredTime) {
        this.expiredTime = expiredTime;
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

        FileModel that = (FileModel) o;

        if (fileId != that.fileId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (!Arrays.equals(data, that.data)) return false;
        if (createdTime != null ? !createdTime.equals(that.createdTime) : that.createdTime != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (expiredTime != null ? !expiredTime.equals(that.expiredTime) : that.expiredTime != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fileId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(data);
        result = 31 * result + (createdTime != null ? createdTime.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (expiredTime != null ? expiredTime.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    public UserModel getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserModel userByUserId) {
        this.userByUserId = userByUserId;
    }

    @OneToMany(mappedBy = "fileByFileId")
    public Collection<ProblemModel> getProblemsByFileId() {
        return problemsByFileId;
    }

    public void setProblemsByFileId(Collection<ProblemModel> problemsByFileId) {
        this.problemsByFileId = problemsByFileId;
    }
}
