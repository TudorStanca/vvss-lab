package drinkshop.repository.file;

import drinkshop.domain.TipBautura;
import drinkshop.repository.RepositoryException;

public class FileTipBauturaRepository
        extends FileAbstractRepository<Integer, TipBautura> {

    public FileTipBauturaRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(TipBautura entity) {
        return entity.getId();
    }

    @Override
    protected TipBautura extractEntity(String line) {
        String[] elems = line.split(",");
        if (elems.length != 2) {
            throw new RepositoryException("Invalid line format: " + line);
        }
        int id = Integer.parseInt(elems[0]);
        String name = elems[1];
        return new TipBautura(id, name);
    }

    @Override
    protected String createEntityAsString(TipBautura entity) {
        return entity.getId() + "," + entity.getName();
    }
}
