/*
 * Copyright (C) 2013 The CyanogenMod Project
 * Copyright (C) 2012 Alex Curran
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.cyanogenmod.focal.ui.showcase;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ShowcaseViews {

    private final List<ShowcaseView> views = new ArrayList<ShowcaseView>();
    private final Activity activity;
    private final int showcaseTemplateId;
    private OnShowcaseAcknowledged showcaseAcknowledgedListener = new OnShowcaseAcknowledged() {
        @Override
        public void onShowCaseAcknowledged(ShowcaseView showcaseView) {
            //DEFAULT LISTENER - DOESN'T DO ANYTHING!
        }
    };

    public interface OnShowcaseAcknowledged {
        void onShowCaseAcknowledged(ShowcaseView showcaseView);
    }

    public ShowcaseViews(Activity activity, int showcaseTemplateLayout) {
        this.activity = activity;
        this.showcaseTemplateId = showcaseTemplateLayout;
    }

    public ShowcaseViews(Activity activity, int showcaseTemplateLayout, OnShowcaseAcknowledged acknowledgedListener) {
        this(activity, showcaseTemplateLayout);
        this.showcaseAcknowledgedListener = acknowledgedListener;
    }

    public void addView(ItemViewProperties properties) {
        ShowcaseView showcaseView = new ShowcaseViewBuilder(activity, showcaseTemplateId).setShowcaseItem(properties.itemType, properties.id, activity)
                .setText(properties.titleResId, properties.messageResId)
                .setShowcaseIndicatorScale(properties.scale)
                .build();
        showcaseView.overrideButtonClick(createShowcaseViewDismissListener(showcaseView));
        views.add(showcaseView);
    }

    private View.OnClickListener createShowcaseViewDismissListener(final ShowcaseView showcaseView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcaseView.hide();
                if (views.isEmpty()) {
                    showcaseAcknowledgedListener.onShowCaseAcknowledged(showcaseView);
                } else {
                    show();
                }
            }
        };
    }

    public void show() {
        if (views.isEmpty()) {
            return;
        }
        final ShowcaseView view = views.get(0);
        ((ViewGroup) activity.getWindow().getDecorView()).addView(view);
        views.remove(0);
    }

    public boolean hasViews() {
        return !views.isEmpty();
    }

    public static class ItemViewProperties {
        public static final int ID_SPINNER = 0;
        public static final int ID_TITLE = 1;
        public static final int ID_OVERFLOW = 2;
        private static final float DEFAULT_SCALE = 1f;

        protected final int titleResId;
        protected final int messageResId;
        protected final int id;
        protected final int itemType;
        protected final float scale;

        public ItemViewProperties(int id, int titleResId, int messageResId, int itemType) {
            this(id, titleResId, messageResId, itemType, DEFAULT_SCALE);
        }

        public ItemViewProperties(int id, int titleResId, int messageResId, int itemType, float scale) {
            this.id = id;
            this.titleResId = titleResId;
            this.messageResId = messageResId;
            this.itemType = itemType;
            this.scale = scale;
        }
    }
}