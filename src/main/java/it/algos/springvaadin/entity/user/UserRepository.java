package it.algos.springvaadin.entity.user;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.ACost;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by gac on TIMESTAMP
 * Estende la l'interaccia MongoRepository col casting alla Entity relativa di questa repository
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 */
@SpringComponent
@Qualifier(ACost.TAG_USE)
public interface UserRepository extends MongoRepository<User, String> {

    public User findByNickname(String nickname);

    public User findByCompanyAndNickname(Company company, String nickname);

    public List<User> findByOrderByNicknameAsc();

    public List<User> findByCompanyOrderByNicknameAsc(Company company);

    public User findByNicknameAndPassword(String nickname, String password);

}// end of class
