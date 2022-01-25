package al.atis.supermarket.service.rs;

import al.atis.api.service.RsRepositoryService;
import al.atis.supermarket.model.StorageProduct;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static al.atis.supermarket.managment.AppConstants.STORAGE_PRODUCT_PATH;

@RestController
@RequestMapping(STORAGE_PRODUCT_PATH)
public class StorageProductController extends RsRepositoryService<StorageProduct, String> {
    public StorageProductController (){
        super(StorageProduct.class);
    }

    @Override
    public void applyFilters() throws Exception {

        if (nn("obj.product_uuid")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("obj.product_uuid")
                    .setParameter("productUuid", _stringParam("obj.product_uuid"));
        }

        if (nn("gt.quantity")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("gt.quantity")
                    .setParameter("quantity", _doubleParam("gt.quantity"));
        }

        if (nn("lt.quantity")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("lt.quantity")
                    .setParameter("quantity", _doubleParam("lt.quantity"));
        }

        if (nn("eq.quantity")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("eq.quantity")
                    .setParameter("quantity", _doubleParam("eq.quantity"));
        }

    }

    @Override
    protected String getDefaultOrderBy() {
        return "quantity asc";
    }
}
