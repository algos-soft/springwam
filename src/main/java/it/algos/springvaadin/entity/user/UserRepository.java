package it.algos.springvaadin.entity.user;

import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.company.Company;

import java.util.List;

/**
 * Created by gac on 16-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_USE)
public interface UserRepository extends MongoRepository<User, String> {

    public User findByCompanyAndNickname(Company company, String nickname);

    public List<User> findByOrderByNicknameAsc();

    public List<User> findByCompanyOrderByNicknameAsc(Company company);

}// end of class
