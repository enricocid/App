/*
 * Copyright (C) 2017 Paranoid Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.enrico.launcher3;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.HashSet;
import java.util.Set;

abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private Set<String> mSelections;
    private Context mContext;

    SelectableAdapter() {

        mContext = LauncherAppState.getInstanceNoCreate().getContext();

        Set<String> mHiddenApps = PreferenceManager.getDefaultSharedPreferences(mContext).getStringSet(Utilities.KEY_HIDDEN_APPS_SET, null);;

        mSelections = new HashSet<>();

        //add already hidden apps to selections
        if (mHiddenApps != null && mHiddenApps.size()!=0) {
            for (String hiddenItem: mHiddenApps) {
                mSelections.add(hiddenItem);
            }
        }
    }

    boolean isSelected(String packageName) {

        return mSelections.contains(packageName);
    }

    void toggleSelection(Toolbar toolbar, int position, String packageName) {

        if (mSelections.contains(packageName)) {

            mSelections.remove(packageName);
        } else {
            mSelections.add(packageName);
        }
        if (mSelections.size() != 0) {
            toolbar.setTitle(String.valueOf(mSelections.size()) + mContext.getString(R.string.hide_app_selected));
        } else {
            toolbar.setTitle(mContext.getString(R.string.hidden_app));
        }
        notifyItemChanged(position);
    }

    void addSelectionsToHideList(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putStringSet(Utilities.KEY_HIDDEN_APPS_SET, mSelections).apply();
    }
}