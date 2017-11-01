package Module.User;

import Module.File.FileModel;
import Module.Problem.ProblemModel;
import Module.Shape.ShapeModel;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "user", schema = "intelligent", catalog = "")
public class UserModel {
    private int userId;
    private String userName;
    private String passWord;
    private Collection<FileModel> filesByUserId;
    private Collection<ProblemModel> problemsByUserId;
    private Collection<ShapeModel> shapesByUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "userName", nullable = true, length = 100)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "passWord", nullable = true, length = 100)
    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserModel that = (UserModel) o;

        if (userId != that.userId) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (passWord != null ? !passWord.equals(that.passWord) : that.passWord != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (passWord != null ? passWord.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<FileModel> getFilesByUserId() {
        return filesByUserId;
    }

    public void setFilesByUserId(Collection<FileModel> filesByUserId) {
        this.filesByUserId = filesByUserId;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<ProblemModel> getProblemsByUserId() {
        return problemsByUserId;
    }

    public void setProblemsByUserId(Collection<ProblemModel> problemsByUserId) {
        this.problemsByUserId = problemsByUserId;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<ShapeModel> getShapesByUserId() {
        return shapesByUserId;
    }

    public void setShapesByUserId(Collection<ShapeModel> shapesByUserId) {
        this.shapesByUserId = shapesByUserId;
    }
}
