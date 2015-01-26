package de.javax.util.eventbinding.impl.testmodel;

import java.util.ArrayList;
import java.util.List;

public class Button {

    private List<ButtonClickListener> listeners = new ArrayList<ButtonClickListener>();

    public void addButtonClickListener(ButtonClickListener listener) {
        listeners.add(listener);

    }

    public void removeButtonClickedListener(ButtonClickListener listener) {
        listeners.remove(listener);
    }
}
