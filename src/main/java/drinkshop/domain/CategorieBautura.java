package drinkshop.domain;


public class CategorieBautura {

    private int id;
    private String name;

    public CategorieBautura(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
