package com.franciscoesquivel.dofuschef.mapper;

import com.dofusdude.client.model.Images;
import com.dofusdude.client.model.ListItem;
import com.franciscoesquivel.dofuschef.model.Equipment;
import com.franciscoesquivel.dofuschef.model.ImageUrls;
import com.franciscoesquivel.dofuschef.model.Recipe;
import com.franciscoesquivel.dofuschef.model.TranslatedID;

public class EquipmentMapper implements ListItemMapper<Equipment> {
    @Override
    public Equipment map(ListItem li) {
        if (li == null) throw new IllegalArgumentException();
        if (li.getAnkamaId() == null) throw new IllegalArgumentException();
        Images imgs = li.getImageUrls() != null ? li.getImageUrls() : new Images();
        String typeName = li.getType().getName() != null ? li.getType().getName() : "";
        Equipment equipment = new Equipment();
        equipment.setAnkamaId(li.getAnkamaId());
        equipment.setName(li.getName());
        equipment.setType(new TranslatedID(typeName));
        ImageUrls imageUrls = new ImageUrls();
        imageUrls.setIcon(imgs.getIcon());
        imageUrls.setSd(imgs.getSd());
        imageUrls.setHq(imgs.getHq());
        imageUrls.setHd(imgs.getHd());
        equipment.setImages(imageUrls);
        equipment.setLevel(li.getLevel() != null ? li.getLevel() : 0);
        equipment.setDescription(li.getDescription());
        equipment.setRecipe((Recipe) li.getRecipe());
        equipment.setWeapon(Boolean.TRUE.equals(li.getIsWeapon()));
        return equipment;
    }
}
