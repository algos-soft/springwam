package it.algos.springvaadin.repository;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.versione.Versione;
import org.bson.types.ObjectId;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;
import java.util.List;

@SpringComponent
public class AlgosMongoRepository implements AlgosRepository, MongoRepository {

    @Override
    public List save(Iterable iterable) {
        return null;
    }


    @Override
    public List findAll() {
        return null;
    }

    @Override
    public List findAll(Sort sort) {
        return null;
    }

    @Override
    public Object insert(Object o) {
        return null;
    }

    @Override
    public List insert(Iterable iterable) {
        return null;
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param serializable must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Object findOne(Serializable serializable) {
        return null;
    }

    public Object findOne(ObjectId id) {
        return null;
    }


    @Override
    public List findAll(Example example) {
        return null;
    }

    @Override
    public List findAll(Example example, Sort sort) {
        return null;
    }

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable
     *
     * @return a page of entities
     */
    @Override
    public Page findAll(Pageable pageable) {
        return null;
    }

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity
     *
     * @return the saved entity
     */
    @Override
    public Object save(Object entity) {
        return null;
    }


    /**
     * Returns whether an entity with the given id exists.
     *
     * @param serializable must not be {@literal null}.
     *
     * @return true if an entity with the given id exists, {@literal false} otherwise
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public boolean exists(Serializable serializable) {
        return false;
    }

    /**
     * Returns all instances of the type with the given IDs.
     *
     * @param iterable
     *
     * @return
     */
    @Override
    public Iterable findAll(Iterable iterable) {
        return null;
    }

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * Deletes the entity with the given id.
     *
     * @param serializable must not be {@literal null}.
     *
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    @Override
    public void delete(Serializable serializable) {

    }

    /**
     * Deletes a given entity.
     *
     * @param entity
     *
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    public void delete(Object entity) {

    }

    /**
     * Deletes the given entities.
     *
     * @param entities
     *
     * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
     */
    @Override
    public void delete(Iterable entities) {

    }

    /**
     * Deletes all entities managed by the repository.
     */
    @Override
    public void deleteAll() {

    }

    /**
     * Returns a single entity matching the given {@link Example} or {@literal null} if none was found.
     *
     * @param example can be {@literal null}.
     *
     * @return a single entity matching the given {@link Example} or {@literal null} if none was found.
     *
     * @throws IncorrectResultSizeDataAccessException if the Example yields more than one result.
     */
    @Override
    public Object findOne(Example example) {
        return null;
    }

    /**
     * Returns a {@link Page} of entities matching the given {@link Example}. In case no match could be found, an empty
     * {@link Page} is returned.
     *
     * @param example  can be {@literal null}.
     * @param pageable can be {@literal null}.
     *
     * @return a {@link Page} of entities matching the given {@link Example}.
     */
    @Override
    public Page findAll(Example example, Pageable pageable) {
        return null;
    }

    /**
     * Returns the number of instances matching the given {@link Example}.
     *
     * @param example the {@link Example} to count instances for, can be {@literal null}.
     *
     * @return the number of instances matching the {@link Example}.
     */
    @Override
    public long count(Example example) {
        return 0;
    }

    /**
     * Checks whether the data store contains elements that match the given {@link Example}.
     *
     * @param example the {@link Example} to use for the existence check, can be {@literal null}.
     *
     * @return {@literal true} if the data store contains elements that match the given {@link Example}.
     */
    @Override
    public boolean exists(Example example) {
        return false;
    }
}// end of class
