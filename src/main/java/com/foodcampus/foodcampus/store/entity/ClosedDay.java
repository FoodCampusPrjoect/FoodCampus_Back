package com.foodcampus.foodcampus.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "closed_days")
public class ClosedDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long closed_id;

    @Column(length = 200)
    private String closedDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
