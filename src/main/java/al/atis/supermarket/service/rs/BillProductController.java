package al.atis.supermarket.service.rs;

import al.atis.api.service.RsRepositoryService;
import al.atis.supermarket.model.BillProduct;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static al.atis.supermarket.managment.AppConstants.BILL_PRODUCT_PATH;

@RestController
@RequestMapping(BILL_PRODUCT_PATH)
public class BillProductController extends RsRepositoryService<BillProduct, String> {

    public BillProductController() {
        super(BillProduct.class);
    }

    @Override
    public void applyFilters() {

        if (nn("obj.bill_uuid")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("obj.bill_uuid")
                    .setParameter("billUuid", _stringParam("obj.bill_uuid"));
        }

        if (nn("obj.product_uuid")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("obj.product_uuid")
                    .setParameter("productUuid", _stringParam("obj.product_uuid"));
        }

    }

    @Override
    protected String getDefaultOrderBy() {
        return "quantity desc";
    }
}
