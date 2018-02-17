package it.algos.springvaadin.service;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.IAUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 27-gen-2018
 * Time: 12:03
 */
@Slf4j
@SpringComponent
@Service
@Scope("singleton")
@Qualifier(ACost.TAG_USE)
@AIScript(sovrascrivibile = false)
public abstract class ALoginService extends AService {


    /**
     * Default constructor
     */
    public ALoginService() {
    }// end of constructor


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     */
    public ALoginService(MongoRepository repository) {
        super(repository);
    }// end of Spring constructor


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public abstract IAUser findByNickname(String nickname);


    /**
     * Controlla che esiste un utente con questo nickname e questa password
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password (obbligatoria o facoltativa, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public abstract boolean passwordValida(String nickname, String password);

}// end of class
