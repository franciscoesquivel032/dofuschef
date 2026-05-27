package com.franciscoesquivel.dofuschef;

import com.dofusdude.client.model.Images;
import com.dofusdude.client.model.ListItem;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Slf4j
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Builder
@ToString
public class Resource {
    @EqualsAndHashCode.Include
    private int ankamaId;
    private String name;
    private String description;
    private int level;
    private int pods;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private ImageUrls images;

    public static Resource mapListItem(ListItem item) {
        if(item == null) throw new IllegalArgumentException();
        if(item.getAnkamaId() == null) throw new IllegalArgumentException();
        try {
            Images imgs = item.getImageUrls() != null ? item.getImageUrls() : new Images();
            return Resource.builder().
                   ankamaId(item.getAnkamaId()).
                   name(item.getName()).
                   description(item.getDescription()).
                   level(item.getLevel() != null ? item.getLevel() : 0).
                   pods(item.getPods() != null ? item.getLevel() : 0).
                   images(ImageUrls.builder().icon(imgs.getIcon()).sd(imgs.getHd()).hq(imgs.getHq()).hd(imgs.getHd()).build()).
                   build();
        } catch(IllegalArgumentException e) {
            log.error("error: ", e);
            return null;
        }
    }
}
