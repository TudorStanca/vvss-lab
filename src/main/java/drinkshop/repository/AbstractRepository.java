package drinkshop.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class AbstractRepository<ID, E>
        implements Repository<ID, E> {

    protected Map<ID, E> entities = new HashMap<>();

    @Override
    public E findOne(ID id) {
        return entities.get(id);
    }

    @Override
    public List<E> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public E save(E entity) {
        try{
            entities.put(getId(entity), entity);
        } catch (RuntimeException e)
        {
            throw new RepositoryException("Failed to save entity: " + entity);
        }
        return entity;
    }

    @Override
    public E delete(ID id) {
        try{
            if (!entities.containsKey(id)) {
                throw new RepositoryException("Entity with id " + id + " does not exist.");
            }
        } catch (RuntimeException e)
        {
            throw new RepositoryException("Failed to delete entity with id: " + id);
        }
        return entities.remove(id);
    }

    @Override
    public E update(E entity) {
        try{
            if (!entities.containsKey(getId(entity))) {
                throw new RepositoryException("Entity with id " + getId(entity) + " does not exist.");
            }
        } catch (RuntimeException e)
        {
            throw new RepositoryException("Failed to update entity: " + entity);
        }
        entities.put(getId(entity), entity);
        return entity;
    }

    protected abstract ID getId(E entity);
}
