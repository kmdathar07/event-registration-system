package com.event.model;

public class Registration {
    private int id;
    private String name;
    private String email;
    private String eventName;
    private String phone;

    // 🔹 Constructors
    public Registration() {}

    public Registration(String name, String email, String eventName, String phone) {
        this.name = name;
        this.email = email;
        this.eventName = eventName;
        this.phone = phone;
    }

    public Registration(int id, String name, String email, String eventName, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.eventName = eventName;
        this.phone = phone;
    }

    // 🔹 Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // 🔹 For displaying registration details
    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Email: %s | Event: %s | Phone: %s",
                id, name, email, eventName, phone);
    }
}
