package al.atis.supermarket.model;

import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;


@Entity
@Data

@FilterDef(name = "gt.price", parameters = @ParamDef(name = "productPrice", type = "double"))
@Filter(name = "gt.price", condition = "price > :productPrice")

@FilterDef(name = "lt.price", parameters = @ParamDef(name = "productPrice", type = "double"))
@Filter(name = "lt.price", condition = "price < :productPrice")

@FilterDef(name = "like.name", parameters = @ParamDef(name = "prod_name", type = "string"))
@Filter(name = "like.name", condition = "lower(name) LIKE :prod_name")

@FilterDef(name = "obj.category", parameters = @ParamDef(name = "category", type = "string"))
@Filter(name = "obj.category", condition = "category = :category")

public class Product {

    @Id
    @Column(name = "uuid", unique = true)
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    protected String uuid;
    protected String name;
    protected double price;
    @Enumerated(EnumType.STRING)
    protected ProductCategory category;

}

enum ProductCategory {
     FOOD,CLOTHES,HYGIENE,TOYS
}
