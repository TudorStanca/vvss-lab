package drinkshop.repository.file;

import drinkshop.domain.Stoc;
import drinkshop.repository.RepositoryException;

public class FileStocRepository
        extends FileAbstractRepository<Integer, Stoc> {

    public FileStocRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(Stoc entity) {
        return entity.getId();
    }

    @Override
    protected Stoc extractEntity(String line) {
        String[] elems = line.split(";");

        if (elems.length != 4) {
            throw new RepositoryException("Invalid line format: " + line);
        }

        int id = Integer.parseInt(elems[0]);
        String ingredient = elems[1];
        double cantitate = Double.parseDouble(elems[2]);
        double stocMinim = Double.parseDouble(elems[3]);

        return new Stoc(id, ingredient, cantitate, stocMinim);
    }

    @Override
    protected String createEntityAsString(Stoc entity) {
        return entity.getId() + ";" +
                entity.getIngredient() + ";" +
                entity.getCantitate() + ";" +
                entity.getStocMinim();
    }
}