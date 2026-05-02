package drinkshop.service.validator;

import drinkshop.domain.Product;

public class ProductValidator implements Validator<Product> {

    @Override
    public void validate(Product product) {

        String errors = "";

        if (product.getId() <= 0)
            errors += "Invalid id!\n";

        if (product.getNume() == null || product.getNume().isBlank())
            errors += "Invalid name!\n";

        if (product.getPret() <= 0)
            errors += "Invalid price!\n";

        if (product.getCategorie() == null || product.getCategorie().isBlank())
            errors += "Invalid category!\n";

        if (product.getTip() == null || product.getTip().isBlank())
            errors += "Invalid type!\n";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
