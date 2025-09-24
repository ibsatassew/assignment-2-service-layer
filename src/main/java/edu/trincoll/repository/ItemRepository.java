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
   

    // Additional query methods
    List<Item> findByStatus(Item.Status status);

    List<Item> findByCategory(String category);

    List<Item> findByTag(String tag);

    List<Item> findByTitleContaining(String query);

    List<Item> saveAll(List<Item> items);


}
