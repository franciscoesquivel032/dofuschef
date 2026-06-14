package com.franciscoesquivel.dofuschef.mapper;

import com.dofusdude.client.model.ListItem;

public interface ListItemMapper<T> {
    T map(ListItem li);
}
