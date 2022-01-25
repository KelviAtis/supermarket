package al.atis.supermarket.service.rs;

import al.atis.api.service.RsRepositoryService;
import al.atis.supermarket.model.StorageOperation;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static al.atis.supermarket.managment.AppConstants.STORAGE_OPERATION_PATH;

@RestController
@RequestMapping(STORAGE_OPERATION_PATH)
public class StorageOperationController extends RsRepositoryService<StorageOperation,String> {
    protected StorageOperationController() {
        super(StorageOperation.class);
    }

    @Override
    public void applyFilters() throws Exception {

        if (nn("obj.operation_type")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("obj.operation_type")
                    .setParameter("operationType", _stringParam("obj.operation_type"));
        }

        if (nn("obj.bill_uuid")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("obj.bill_uuid")
                    .setParameter("bill_uuid", _stringParam("obj.bill_uuid"));
        }

        if (nn("gt.operation_date")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("gt.operation_date")
                    .setParameter("operationDate", _localDateParam("gt.operation_date"));
        }

        if (nn("lt.operation_date")) {
            getEntityManager().unwrap(Session.class)
                    .enableFilter("lt.operation_date")
                    .setParameter("operationDate", _localDateParam("lt.operation_date"));
        }

    }

    @Override
    protected String getDefaultOrderBy() {
        return "type desc";
    }

}
