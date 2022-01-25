package al.atis.supermarket.service.rs;

import al.atis.api.service.RsRepositoryService;
import al.atis.supermarket.model.User;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static al.atis.supermarket.managment.AppConstants.User_PATH;

@RestController
@RequestMapping(User_PATH)
public class UserController extends RsRepositoryService<User, String> {
    public UserController() {
        super(User.class);
    }

    @Override
    public void applyFilters() throws Exception {
        if(nn("obj.role")){
            getEntityManager().unwrap(Session.class)
                    .enableFilter("obj.role")
                    .setParameter("role",_stringParam("obj.role"));
        }
    }

    @Override
    protected String getDefaultOrderBy() {
        return "role asc";
    }
}
