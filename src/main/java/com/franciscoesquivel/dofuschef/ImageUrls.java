package com.franciscoesquivel.dofuschef;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "image_urls")
public class ImageUrls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String icon;
    private String sd;
    private String hq;
    private String hd;
}
