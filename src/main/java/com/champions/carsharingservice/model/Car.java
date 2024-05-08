package com.champions.carsharingservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"rentals"})
@ToString(exclude = {"rentals"})
@SQLDelete(sql = "UPDATE cars SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted=false")
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CarType type;

    @Column(nullable = false)
    private Integer inventory;

    @Column(nullable = false, name = "daily_fee")
    private BigDecimal dailyFee;

    @OneToMany(mappedBy = "car",
            orphanRemoval = true,
            cascade = CascadeType.REMOVE)
    private Set<Rental> rentals = new HashSet<>();

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public enum CarType {
        SEDAN,
        SUV,
        HATCHBACK,
        UNIVERSAL
    }
}
