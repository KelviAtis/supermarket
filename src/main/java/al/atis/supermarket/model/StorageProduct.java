package al.atis.supermarket.model;

import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

@Entity
@Data

@FilterDef(name = "obj.product_uuid", parameters = @ParamDef(name = "productUuid", type = "string"))
@Filter(name = "obj.product_uuid", condition = " product_uuid = :productUuid")

@FilterDef(name = "gt.quantity", parameters = @ParamDef(name = "quantity", type = "double"))
@Filter(name = "gt.quantity", condition = "quantity > :quantity")

@FilterDef(name = "lt.quantity", parameters = @ParamDef(name = "quantity", type = "double"))
@Filter(name = "lt.quantity", condition = "quantity < :quantity")

@FilterDef(name = "eq.quantity", parameters = @ParamDef(name = "quantity", type = "double"))
@Filter(name = "eq.quantity", condition = "quantity = :quantity")

public class StorageProduct {

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    @Id
    protected String uuid;

    protected double quantity;

    protected String product_uuid;
}
