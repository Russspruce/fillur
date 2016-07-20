package com.epicodus.guest.fillur.utils;

/**
 * Created by Guest on 7/20/16.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
