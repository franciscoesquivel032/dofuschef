package com.franciscoesquivel.dofuschef;

import com.dofusdude.client.model.Images;
import com.dofusdude.client.model.ListItem;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.awt.*;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Builder
@ToString
public class Resource implements IMapListItem<Resource>{
    @Id
    @Generated
    @EqualsAndHashCode.Include
    private int id;
    @NotNull
    @EqualsAndHashCode.Include
    private int ankamaId;
    private String name;
    private String description;
    private int level;
    private int pods;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private ImageUrls images;

    @Override
    public Resource mapListItem(ListItem item) {
        if (item == null) throw new IllegalArgumentException();
        if (item.getAnkamaId() == null) throw new IllegalArgumentException();
        Images imgs = item.getImageUrls() != null ? item.getImageUrls() : new Images();
        return Resource.builder().
                ankamaId(item.getAnkamaId()).
                name(item.getName()).
                description(item.getDescription()).
                level(item.getLevel() != null ? item.getLevel() : 0).
                pods(item.getPods() != null ? item.getLevel() : 0).
                images(ImageUrls.builder().icon(imgs.getIcon()).sd(imgs.getHd()).hq(imgs.getHq()).hd(imgs.getHd()).build()).
                build();
    }
}
