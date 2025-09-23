package edu.trincoll.repository;

import edu.trincoll.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    private InMemoryItemRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryItemRepository();
        repository.deleteAll(); // ensure clean state
    }

    @Test
    void testSaveAndFindById() {
        Item item = new Item("Test Item", "Description");
        item.setCategory("Test");
        Item saved = repository.save(item);

        assertNotNull(saved.getId());
        Optional<Item> fetched = repository.findById(saved.getId());
        assertTrue(fetched.isPresent());
        assertEquals("Test Item", fetched.get().getTitle());
    }

    @Test
    void testFindAllAndCount() {
        repository.save(new Item("Item 1", "Desc 1"));
        repository.save(new Item("Item 2", "Desc 2"));

        List<Item> allItems = repository.findAll();
        assertEquals(2, allItems.size());
        assertEquals(2, repository.count());
    }

    @Test
    void testDeleteByIdAndExistsById() {
        Item item = repository.save(new Item("Delete Me", "Desc"));
        Long id = item.getId();
        assertTrue(repository.existsById(id));

        repository.deleteById(id);
        assertFalse(repository.existsById(id));
    }

    @Test
    void testFindByStatus() {
        Item active = new Item("Active", "Desc");
        active.setStatus(Item.Status.ACTIVE);
        Item inactive = new Item("Inactive", "Desc");
        inactive.setStatus(Item.Status.INACTIVE);

        repository.save(active);
        repository.save(inactive);

        List<Item> activeItems = repository.findByStatus(Item.Status.ACTIVE);
        assertEquals(1, activeItems.size());
        assertEquals("Active", activeItems.get(0).getTitle());
    }

    @Test
    void testFindByCategoryAndTag() {
        Item item = new Item("Work Item", "Desc");
        item.setCategory("Work");
        item.getTags().add("urgent");
        repository.save(item);

        List<Item> categoryItems = repository.findByCategory("Work");
        assertEquals(1, categoryItems.size());

        List<Item> tagItems = repository.findByTag("urgent");
        assertEquals(1, tagItems.size());
    }

    @Test
    void testFindByTitleContaining() {
        repository.save(new Item("Java Programming", "Learn Java"));
        repository.save(new Item("Python Guide", "Learn Python"));

        List<Item> results = repository.findByTitleContaining("Java");
        assertEquals(1, results.size());
        assertEquals("Java Programming", results.get(0).getTitle());
    }

    @Test
    void testSaveAll() {
        List<Item> items = List.of(
                new Item("Item 1", "Desc 1"),
                new Item("Item 2", "Desc 2")
        );

        List<Item> saved = repository.saveAll(items);
        assertEquals(2, saved.size());
        assertEquals(2, repository.count());
    }
}
