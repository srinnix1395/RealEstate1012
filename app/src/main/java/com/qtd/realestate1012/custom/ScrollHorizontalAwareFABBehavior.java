package com.qtd.realestate1012.custom;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DELL on 8/25/2016.
 */
public class ScrollHorizontalAwareFABBehavior extends FloatingActionButton.Behavior {

    public ScrollHorizontalAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_HORIZONTAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dxConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            // User scrolled right and the FAB is currently visible -> hide the FAB
            child.hide();
        } else if (dxConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            // User scrolled left and the FAB is currently not visible -> show the FAB
            child.show();
        }
    }
}