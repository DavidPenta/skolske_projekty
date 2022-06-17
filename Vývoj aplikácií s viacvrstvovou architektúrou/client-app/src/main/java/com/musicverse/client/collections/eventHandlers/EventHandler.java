package com.musicverse.client.collections.eventHandlers;

import javafx.scene.layout.AnchorPane;
@Deprecated
public interface EventHandler<U> {
    public void handler(U u, AnchorPane mainPane);
}
