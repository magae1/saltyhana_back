package com.saltyhana.saltyhanaserver.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity(name = "uuser")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String identifier;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String profileImage;

    @Column(nullable = false)
    private LocalDate birth;

    @ManyToMany
    @JoinTable(
            name = "recommended",
            joinColumns = @JoinColumn(nullable = false, name = "user_id"),
            inverseJoinColumns = @JoinColumn(nullable = false, name = "product_id")
    )
    Set<Product> recommendedProducts;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private ConsumptionTendency consumptionTendency;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean active;
}
