package com.franciscoesquivel.dofuschef;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ImageUrls {
    @Id
    @Generated
    private int id;
    private String icon;
    private String sd;
    private String hq;
    private String hd;
}
