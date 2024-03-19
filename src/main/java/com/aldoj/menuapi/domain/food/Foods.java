package com.aldoj.menuapi.domain.food;

import com.aldoj.menuapi.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "foods")
@Entity(name = "foods")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Foods {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Foods(FoodDTO data, User user) {
        this.name = data.name();
        this.description = data.description();
        this.price = data.price();
        this.user = user;
    }
}
