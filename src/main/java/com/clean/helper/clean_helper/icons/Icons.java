package com.clean.helper.clean_helper.icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.util.Objects;

public interface Icons {

    String ActionIconPath = "/icons/ActionLogo.svg";
    Icon ActionIcon = IconLoader.getIcon(ActionIconPath, Icons.class);

    ImageIcon ActionImageIcon = new ImageIcon(Objects.requireNonNull(Icons.class.getResource(ActionIconPath)));
}
