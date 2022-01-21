package al.atis.supermarket.model;


import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;


import javax.persistence.*;
import java.time.LocalDate;



import static al.atis.supermarket.managment.AppConstants.BILL_TABLE_NAME;

//@Data
@Entity
@Table(name = BILL_TABLE_NAME)

@FilterDef(name = "like.created_by", parameters = @ParamDef(name = "created_by", type = "string"))
@Filter(name = "like.created_by", condition = "lower(created_by) LIKE :created_by")

@FilterDef(name = "gt.total_price", parameters = @ParamDef(name = "total_price", type = "double"))
@Filter(name = "gt.total_price", condition = "total_price > :total_price")

@FilterDef(name = "lt.total_price", parameters = @ParamDef(name = "total_price", type = "double"))
@Filter(name = "lt.total_price", condition = "total_price < :total_price")

@FilterDef(name = "from.created_date", parameters = @ParamDef(name = "createdDate", type = "java.time.LocalDate"))
@Filter(name = "from.created_date" , condition = "created_date > :createdDate")

@FilterDef(name = "to.created_date", parameters = @ParamDef(name = "toCreatedDate", type = "java.time.LocalDate"))
@Filter(name = "to.created_date" , condition = "created_date < :toCreatedDate")

public class Bill {

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    @Id
    public String uuid;
    public Double total_price;
    public LocalDate created_date;
    public String created_by;


    @Override
    public String toString() {
        return "Bill{" +
                "uuid='" + uuid + '\'' +
                ", total_price=" + total_price +
                ", created_date=" + created_date +
                ", created_by='" + created_by + '\'' +
                '}';
    }
}
