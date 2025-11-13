package com.julian.proyectos.personales.tienda.products_microservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.proyectos.personales.tienda.libs_commons.models.Product;
import com.julian.proyectos.personales.tienda.products_microservice.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private Environment environment;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return repository.findAll().stream().map(product -> {
            getAndSetPort(product);
            return product;
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findProductByIdOrName(String entry) {
        Object value = isLong(entry) ? Long.parseLong(entry) : entry;
        if (isLong(entry)) {
            return repository.findById((Long) value).map(product -> {
                getAndSetPort(product);
                return product;
            });
        } else {
            return repository.findByName((String) value).map(product -> {
                getAndSetPort(product);
                return product;
            });
        }
    }

    @Override
    @Transactional
    public Optional<Product> saveProduct(Product product) {
        getAndSetPort(product);
        return Optional.of(repository.save(product));
    }

    @Override
    @Transactional
    public Optional<Product> updateProductByIdOrName(String value, Product product) {
        Product productDb = findProductByIdOrName(value).get();
        boolean isNameEqualOrUnique = productDb.getName().equals(product.getName())
                || repository.findByName(product.getName()).isEmpty();
        if (isNameEqualOrUnique) {
            productDb.setName(product.getName());
            productDb.setPrice(product.getPrice());
            productDb.setDescription(product.getDescription());
            return Optional.of(repository.save(productDb)).map(p -> {
                getAndSetPort(p);
                return p;
            });
        }
        return Optional.empty();

    }

    @Override
    @Transactional
    public void deleteProductByIdOrName(String entry) {
        if (isLong(entry)) {
            repository.deleteById(Long.valueOf(entry));
        }
        repository.deleteByName(entry);
    }

    private void getAndSetPort(Product product) {
        product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
    }

    private boolean isLong(String entry) {
        return entry.matches("\\d+") ? true : false;
    }

}
