package com.franciscoesquivel.dofuschef.dofusdude;

import com.dofusdude.client.model.ListItem;

public interface ListItemMapper<T> {
    T map(ListItem li);
}
