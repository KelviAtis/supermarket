package al.atis.api.service;

import al.atis.supermarket.model.Role;
import al.atis.supermarket.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class RsRepositoryServiceTest {

    @Autowired
    private RsRepositoryService service;

    void findUser(){
        User user = new User("username","password", Role.ADMIN);

    }

}