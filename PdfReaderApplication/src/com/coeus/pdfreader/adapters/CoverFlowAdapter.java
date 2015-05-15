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

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;

import com.coeus.pdfreader.imageloader.FileCache;
import com.coeus.pdfreader.imageloader.ImageLoader;
import com.coeus.pdfreader.imageloader.MemoryCache;
import com.coeus.pdfreader.model.PdfFileDataModel;

public class CoverFlowAdapter extends FancyCoverFlowAdapter {

    // =============================================================================
    // Private members
    // =============================================================================
	ArrayList<PdfFileDataModel> pdfFileDetailList;
	private ImageLoader imageLoader;
//    private int[] cover_images = {R.drawable.android_cover, R.drawable.cpp_cover, R.drawable.csharp_cover, R.drawable.java_cover,};

    // =============================================================================
    // Supertype overrides
    // =============================================================================

    public CoverFlowAdapter(ArrayList<PdfFileDataModel> pdfFileDetailList, Context pContext) {
    	this.pdfFileDetailList = pdfFileDetailList;
    	FileCache f = new FileCache(pContext);
		MemoryCache m = new MemoryCache();
		m.clear();
		f.clear();
    	imageLoader = new ImageLoader(pContext);
	}

	@Override
    public int getCount() {
        return pdfFileDetailList.size();
    }

    @Override
    public String getItem(int i) {
        return pdfFileDetailList.get(i).getCoverUrl();
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
            imageView_cover.setScaleType(ImageView.ScaleType.CENTER);
            imageView_cover.setLayoutParams(new FancyCoverFlow.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
            imageView_cover.setPadding(20, 20, 20, 0);

        }

        
        imageLoader.DisplayImage(pdfFileDetailList.get(position).getCoverUrl(), imageView_cover);
//        imageView_cover.setImageResource(this.getItem(position));
        return imageView_cover;
    }
}
