package com.alena.happysweets.model;

import jakarta.persistence.*;
import lombok.Data;
//Category model
@Entity
@Data
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

}
