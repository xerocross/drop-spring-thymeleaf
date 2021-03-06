package com.adamfgcross.drop.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "droplet")
public class Drop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max=255, message="text is too long")
    @Size(min=1, message="text is too short")
    private String text;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public Drop() {
    }

    public Drop(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Drop{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Drop drop = (Drop) o;

        return id.equals(drop.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
