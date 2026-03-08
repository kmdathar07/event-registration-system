package com.event.model;

import java.sql.Date;

public class Event {
    private int id;
    private String name;
    private Date eventDate;
    private String location;
    private int capacity;

    public Event() {}

    public Event(int id, String name, Date eventDate, String location, int capacity) {
        this.id = id;
        this.name = name;
        this.eventDate = eventDate;
        this.location = location;
        this.capacity = capacity;
    }

    public Event(String name, Date eventDate, String location, int capacity) {
        this.name = name;
        this.eventDate = eventDate;
        this.location = location;
        this.capacity = capacity;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    @Override
    public String toString() {
        return "Event [id=" + id + ", name=" + name + ", eventDate=" + eventDate +
                ", location=" + location + ", capacity=" + capacity + "]";
    }
}
