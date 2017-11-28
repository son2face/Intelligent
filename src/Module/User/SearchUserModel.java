package Module.User;

import Module.FilterModel;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.QueryParam;

@Entity
@Table(name = "point", schema = "intelligent", catalog = "")
public class SearchUserModel extends FilterModel {
    @QueryParam("userId")
    private Integer userId;
    @QueryParam("userName")
    private String userName;
    @QueryParam("passWord")
    private String passWord;

    public CriteriaQuery<UserModel> applyTo(CriteriaBuilder builder, CriteriaQuery<UserModel> criteria, Root<UserModel> root) {
        if (userId != null) {
            criteria.where(builder.equal(root.get(UserModel_.userId), userId));
        }
        if (userName != null && !userName.isEmpty()) {
            criteria.where(builder.like(builder.lower(root.get(UserModel_.userName)), "%" + userName.toLowerCase() + "%"));
        }
        if (passWord != null && !passWord.isEmpty()) {
            criteria.where(builder.like(builder.lower(root.get(UserModel_.passWord)), "%" + passWord.toLowerCase() + "%"));
        }
        return criteria;
    }
}
