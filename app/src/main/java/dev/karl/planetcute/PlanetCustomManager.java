package dev.karl.planetcute;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class PlanetCustomManager extends LinearLayoutManager {

    private boolean scrollable = true;

    public PlanetCustomManager(Context context) {
        super(context);
    }

    public void setScrollable(boolean flag) {
        scrollable = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return scrollable && super.canScrollVertically();
    }
}
