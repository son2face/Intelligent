package Module;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.QueryParam;

public class FilterModel<T> {
    @QueryParam("skip")
    public Integer skip;
    @QueryParam("take")
    public Integer take;
    @QueryParam("orderBy")
    public String orderBy;
    @QueryParam("orderType")
    public String orderType;

    public FilterModel() {

    }

    public CriteriaQuery<T> order(CriteriaBuilder builder, CriteriaQuery<T> criteria, Root<T> root) {
        if (orderBy != null) {
            Class<T> type = criteria.getResultType();
            String property = "";
            try {
                type.getDeclaredField(orderBy);
                property = orderBy;
            } catch (NoSuchFieldException e) {
                property = type.getDeclaredFields()[0].getName();
                e.printStackTrace();
            }
            switch (orderType) {
                case "Asc":
                case "asc":
                    criteria.orderBy(builder.asc(root.get(property)));
                    break;
                case "Desc":
                case "desc":
                    criteria.orderBy(builder.desc(root.get(property)));
                    break;
            }
        }
        return criteria;
    }

    public Query skipAndTake(Query query) {
        if (skip != null && skip > 0) {
            query.setFirstResult(skip);
        }
        if (take != null && take > 0) {
            query.setMaxResults(take);
        }
        return query;
    }
}
