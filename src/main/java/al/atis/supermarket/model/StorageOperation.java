package al.atis.supermarket.model;

import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data

@FilterDef(name = "obj.operation_type", parameters = @ParamDef(name = "operationType", type = "string"))
@Filter(name = "obj.operation_type", condition = "operation_type = :operationType")

@FilterDef(name = "obj.bill_uuid", parameters = @ParamDef(name = "bill_uuid", type = "string"))
@Filter(name = "obj.bill_uuid", condition = "bill_uuid = :bill_uuid")

@FilterDef(name = "gt.operation_date", parameters = @ParamDef(name = "operationDate", type = "java.time.LocalDate"))
@Filter(name = "gt.operation_date", condition = "operation_date > :operationDate")

@FilterDef(name = "lt.operation_date", parameters = @ParamDef(name = "operationDate", type = "java.time.LocalDate"))
@Filter(name = "lt.operation_date", condition = "operation_date < :operationDate")

public class StorageOperation {
    @Id
    @Column(name = "uuid", unique = true)
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    protected String uuid;
    protected String bill_uuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    protected OperationType type;
    @Column(name = "operation_date")
    protected LocalDate operationDate;
}
enum OperationType {
    ENTRY, EXIT
}
