package com.franciscoesquivel.dofuschef.equipment;

import com.dofusdude.client.model.Images;
import com.dofusdude.client.model.ListItem;
import com.franciscoesquivel.dofuschef.ImageUrls;
import com.franciscoesquivel.dofuschef.Recipe;
import com.franciscoesquivel.dofuschef.TranslatedID;
import com.franciscoesquivel.dofuschef.dofusdude.ListItemMapper;

public class EquipmentMapper implements ListItemMapper<Equipment> {
    @Override
    public Equipment map(ListItem li) {
        if (li == null) throw new IllegalArgumentException();
        if (li.getAnkamaId() == null) throw new IllegalArgumentException();
        Images imgs = li.getImageUrls() != null ? li.getImageUrls() : new Images();
        String typeName = li.getType().getName() != null ? li.getType().getName() : "";
        return Equipment.builder()
                .ankamaId(li.getAnkamaId())
                .name(li.getName())
                .type(new TranslatedID(typeName))
                .images(ImageUrls.builder().icon(imgs.getIcon()).sd(imgs.getHd()).hq(imgs.getHq()).hd(imgs.getHd()).build())
                .level(li.getLevel() != null ? li.getLevel() : 0)
                .description(li.getDescription())
                .recipe((Recipe) li.getRecipe())
                .isWeapon(Boolean.TRUE.equals(li.getIsWeapon()))
                .build();
    }
}
