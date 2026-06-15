package com.franciscoesquivel.dofuschef.mapper;
import com.dofusdude.client.model.Images;
import com.dofusdude.client.model.ListItem;
import com.franciscoesquivel.dofuschef.model.ImageUrls;
import com.franciscoesquivel.dofuschef.model.Resource;

public class ResourceMapper implements ListItemMapper<Resource> {
    @Override
    public Resource map(ListItem li) {
        if (li == null) throw new IllegalArgumentException();
        if (li.getAnkamaId() == null) throw new IllegalArgumentException();
        Images imgs = li.getImageUrls() != null ? li.getImageUrls() : new Images();
        Resource resource = new Resource();
        resource.setAnkamaId(li.getAnkamaId());
        resource.setName(li.getName());
        resource.setDescription(li.getDescription());
        resource.setLevel(li.getLevel() != null ? li.getLevel() : 0);
        resource.setPods(li.getPods() != null ? li.getPods() : 0);
        resource.setImages(ImageUrls.builder()
                .icon(imgs.getIcon())
                .hd(imgs.getHd())
                .hq(imgs.getHq())
                .sd(imgs.getSd())
                .build());
        resource.setRecipe(buildRecipe(li));
        return resource;
    }
}
