package al.atis.supermarket.model;

import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

@Entity
@Data

@FilterDef(name = "obj.role", parameters = @ParamDef(name = "role", type = "string"))
@Filter(name = "obj.role", condition = "role = :role")

public class User {

    @Id
    @Column(name = "uuid", unique = true)
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    protected String uuid;
    protected String username;
    protected String password;

    @Enumerated(EnumType.STRING)
    protected Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User() {

    }
}


