package edu.trincoll.repository;

import edu.trincoll.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends Repository<Item, Long> {

    Item save(Item item);

    Optional<Item> findById(Long id);

    List<Item> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    void deleteAll();

    long count();


}
