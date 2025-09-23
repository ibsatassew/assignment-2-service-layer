package edu.trincoll.model;

import java.util.*;

public class Item {

    public enum Status { ACTIVE, INACTIVE, ARCHIVED }

    private Long id;
    private String title;
    private String description;
    private String category;
    private Status status = Status.ACTIVE;
    private Set<String> tags = new HashSet<>();

    public Item() {}
    public Item(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Set<String> getTags() { return tags; }
    public void addTag(String tag) { tags.add(tag); }
}
