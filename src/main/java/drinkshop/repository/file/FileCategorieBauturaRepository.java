package drinkshop.repository.file;

import drinkshop.domain.CategorieBautura;
import drinkshop.repository.RepositoryException;

public class FileCategorieBauturaRepository
        extends FileAbstractRepository<Integer, CategorieBautura> {

    public FileCategorieBauturaRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(CategorieBautura entity) {
        return entity.getId();
    }

    @Override
    protected CategorieBautura extractEntity(String line) {
        String[] elems = line.split(",");
        if (elems.length != 2) {
            throw new RepositoryException("Invalid line format: " + line);
        }
        int id = Integer.parseInt(elems[0]);
        String name = elems[1];
        return new CategorieBautura(id, name);
    }

    @Override
    protected String createEntityAsString(CategorieBautura entity) {
        return entity.getId() + "," + entity.getName();
    }
}
