package al.atis.supermarket.model;

import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

@Entity
@Data
@Table(name = "bill_product")

@FilterDef(name = "obj.bill_uuid", parameters = @ParamDef(name = "billUuid", type = "java.lang.String"))
@Filter(name = "obj.bill_uuid", condition = "bill_uuid = :billUuid")

@FilterDef(name = "obj.product_uuid", parameters = @ParamDef(name = "productUuid", type = "string"))
@Filter(name = "obj.product_uuid", condition = " product_uuid = :productUuid")

public class BillProduct {

    @Id
    @Column(name = "uuid", unique = true)
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    protected String uuid;

    protected int quantity;

    @Column(name = "bill_uuid")
    protected String billUuid;

    //kjo ishte e sakta
    @Column(name = "product_uuid")
    protected String productUuid;
}
