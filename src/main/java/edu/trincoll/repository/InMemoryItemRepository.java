package edu.trincoll.repository;

import edu.trincoll.model.Item;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(idCounter.getAndIncrement());
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void deleteById(Long id) {
        items.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return items.containsKey(id);
    }

    @Override
    public void deleteAll() {
        items.clear();
    }

    @Override
    public long count() {
        return items.size();
    }

    @Override
    public List<Item> findByStatus(Item.Status status) {
        return items.values().stream()
                .filter(i -> i.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByCategory(String category) {
        return items.values().stream()
                .filter(i -> category.equals(i.getCategory()))
                .collect(Collectors.toList());
    }

        @Override
    public List<Item> findByTag(String tag) {
        return items.values().stream()
                .filter(i -> i.getTags() != null && i.getTags().contains(tag))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByTitleContaining(String query) {
        return items.values().stream()
                .filter(i -> i.getTitle() != null && i.getTitle().contains(query))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> saveAll(List<Item> itemsList) {
        return itemsList.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }


}
