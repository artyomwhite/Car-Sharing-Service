package com.champions.carsharingservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
@ToString(exclude = {"car", "user", "payments"})
@EqualsAndHashCode(exclude = {"car", "user", "payments"})
@SQLDelete(sql = "UPDATE rentals SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "rental_date", nullable = false)
    private LocalDateTime rentalDateTime;

    @Column(name = "return_date", nullable = false)
    private LocalDateTime returnDateTime;

    @Column(name = "actual_return_date")
    private LocalDateTime actualReturnDateTime;

    @OneToMany(mappedBy = "rental",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();

    @Column(nullable = false)
    private boolean isDeleted = false;
}
