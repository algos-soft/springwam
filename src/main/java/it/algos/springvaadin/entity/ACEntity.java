package it.algos.springvaadin.entity;

import it.algos.springvaadin.annotation.AIColumn;
import it.algos.springvaadin.annotation.AIField;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.enumeration.EAFieldAccessibility;
import it.algos.springvaadin.enumeration.EAFieldType;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 22-dic-2017
 * Time: 12:14
 */
@Getter
public abstract class ACEntity extends AEntity {


    /**
     * Riferimento alla company (per le sottoclassi che usano questa classe)
     * - Nullo se il flag AlgosApp.USE_MULTI_COMPANY=false
     * - Facoltativo od obbligatorio a seconda della sottoclasse, se il flag AlgosApp.USE_MULTI_COMPANY=true
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @AIField(type = EAFieldType.combo, clazz = Company.class, dev = EAFieldAccessibility.newOnly, admin = EAFieldAccessibility.showOnly)
    @AIColumn(name = "Company", width = 115)
    public Company company;


}// end of class
