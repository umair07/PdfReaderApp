/*
 * Copyright 2013 David Schreiber
 *           2013 John Paul Nalog
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.coeus.pdfreader.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;

import com.coeus.pdfreader.R;

public class CoverFlowAdapter extends FancyCoverFlowAdapter {

    // =============================================================================
    // Private members
    // =============================================================================

    private int[] cover_images = {R.drawable.android_cover, R.drawable.cpp_cover, R.drawable.csharp_cover, R.drawable.java_cover,};

    // =============================================================================
    // Supertype overrides
    // =============================================================================

    @Override
    public int getCount() {
        return cover_images.length;
    }

    @Override
    public Integer getItem(int i) {
        return cover_images[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getCoverFlowItem(int position, View reuseableView, ViewGroup viewGroup) {
        ImageView imageView_cover = null;

        if (reuseableView != null) {
            imageView_cover = (ImageView) reuseableView;
        } else {
            imageView_cover = new ImageView(viewGroup.getContext());
            imageView_cover.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView_cover.setLayoutParams(new FancyCoverFlow.LayoutParams(600,LayoutParams.WRAP_CONTENT));
            imageView_cover.setPadding(20, 0, 20, 0);

        }

        imageView_cover.setImageResource(this.getItem(position));
        return imageView_cover;
    }
}
