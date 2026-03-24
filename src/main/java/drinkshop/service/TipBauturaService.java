package drinkshop.service;

import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;

import java.util.List;

public class TipBauturaService {

    private final Repository<Integer, TipBautura> repo;

    public TipBauturaService(Repository<Integer, TipBautura> repo) {
        this.repo = repo;
    }

    public void add(TipBautura t) { repo.save(t); }
    public void update(TipBautura t) { repo.update(t); }
    public void delete(int id) { repo.delete(id); }
    public List<TipBautura> getAll() { return repo.findAll(); }
}
