package edu.trincoll.service;

import edu.trincoll.model.Item;
import edu.trincoll.repository.ItemRepository;
import edu.trincoll.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service layer for Item entity.
 */
@Service
public class ItemService extends BaseService<Item, Long> {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public Repository<Item, Long> getRepository() {
        return repository;
    }

    @Override
    public void validateEntity(Item item) {
        if (item == null) throw new IllegalArgumentException("Item cannot be null");
        if (item.getTitle() == null || item.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("Title is required");
        if (item.getTitle().length() > 100)
            throw new IllegalArgumentException("Title cannot exceed 100 characters");
    }

    // --- Collection Operations ---

    public Map<String, List<Item>> groupByCategory() {
        return findAll().stream()
                .collect(Collectors.groupingBy(Item::getCategory));
    }

    public Set<String> getAllUniqueTags() {
        return findAll().stream()
                .flatMap(i -> i.getTags().stream())
                .collect(Collectors.toSet());
    }

    public Map<Item.Status, Long> countByStatus() {
        return findAll().stream()
                .collect(Collectors.groupingBy(Item::getStatus, Collectors.counting()));
    }

    public List<Item> findByAllTags(Set<String> tags) {
        return findAll().stream()
                .filter(item -> item.getTags().containsAll(tags))
                .collect(Collectors.toList());
    }

    public List<Item> findByAnyTag(Set<String> tags) {
        return findAll().stream()
                .filter(item -> !Collections.disjoint(item.getTags(), tags))
                .collect(Collectors.toList());
    }

    public int archiveInactiveItems() {
        List<Item> inactive = findAll().stream()
                .filter(item -> item.getStatus() == Item.Status.INACTIVE)
                .collect(Collectors.toList());

        inactive.forEach(item -> item.setStatus(Item.Status.ARCHIVED));
        return inactive.size();
    }

    public List<String> getMostPopularTags(int topN) {
        Map<String, Long> tagCounts = findAll().stream()
                .flatMap(i -> i.getTags().stream())
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));

        return tagCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Item> search(String query) {
        String q = query.toLowerCase();
        return findAll().stream()
                .filter(item -> item.getTitle().toLowerCase().contains(q) ||
                        (item.getDescription() != null && item.getDescription().toLowerCase().contains(q)))
                .collect(Collectors.toList());
    }

    public List<Item> findByStatus(Item.Status status) {
        return findAll().stream()
                .filter(item -> item.getStatus() == status)
                .collect(Collectors.toList());
    }
}
