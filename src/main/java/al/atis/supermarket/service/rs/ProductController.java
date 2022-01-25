package al.atis.supermarket.service.rs;


import al.atis.api.service.RsRepositoryService;
import al.atis.supermarket.model.Product;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static al.atis.supermarket.managment.AppConstants.PRODUCTS_PATH;

@RestController
@RequestMapping(PRODUCTS_PATH)
public class ProductController extends RsRepositoryService<Product, String> {
    public ProductController(){
        super(Product.class);
    }

    @Override
    public void applyFilters() throws Exception {

        if (nn("gt.price")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("gt.price")
                    .setParameter("productPrice", _doubleParam("gt.price"));
        }
        if (nn("lt.price")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("lt.price")
                    .setParameter("productPrice", _doubleParam("lt.price"));
        }

        if (nn("like.name")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("like.name")
                    .setParameter("prod_name", likeParamToLowerCase("like.name"));
        }

        if (nn("obj.category")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("obj.category")
                    .setParameter("category", _stringParam("obj.category"));
        }

    }

    @Override
    protected String getDefaultOrderBy() {
        return null;
    }
}
