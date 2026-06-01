package com.franciscoesquivel.dofuschef.Resources;

import com.dofusdude.client.model.Images;
import com.dofusdude.client.model.ListItem;
import com.franciscoesquivel.dofuschef.ImageUrls;
import com.franciscoesquivel.dofuschef.dofusdude.ListItemMapper;

public class ResourceMapper implements ListItemMapper<Resource> {
    @Override
    public Resource map(ListItem li) {
        if (li == null) throw new IllegalArgumentException();
        if (li.getAnkamaId() == null) throw new IllegalArgumentException();
        Images imgs = li.getImageUrls() != null ? li.getImageUrls() : new Images();
        return Resource.builder().
                ankamaId(li.getAnkamaId()).
                name(li.getName()).
                description(li.getDescription()).
                level(li.getLevel() != null ? li.getLevel() : 0).
                pods(li.getPods() != null ? li.getLevel() : 0).
                images(ImageUrls.builder().icon(imgs.getIcon()).sd(imgs.getHd()).hq(imgs.getHq()).hd(imgs.getHd()).build()).
                build();
    }
}
