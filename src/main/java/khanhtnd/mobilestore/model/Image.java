package khanhtnd.mobilestore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
