package com.syrtin.dataprocessor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_data", nullable = false)
    private byte[] photoData;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
