/*
 * This file is part of MAME4droid.
 *
 * Copyright (C) 2024 David Valdeita (Seleuco)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Linking MAME4droid statically or dynamically with other modules is
 * making a combined work based on MAME4droid. Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * In addition, as a special exception, the copyright holders of MAME4droid
 * give you permission to combine MAME4droid with free software programs
 * or libraries that are released under the GNU LGPL and with code included
 * in the standard release of MAME under the MAME License (or modified
 * versions of such code, with unchanged license). You may copy and
 * distribute such a system following the terms of the GNU GPL for MAME4droid
 * and the licenses of the other code concerned, provided that you include
 * the source code of that other code when and as the GNU GPL requires
 * distribution of source code.
 *
 * Note that people who make modified versions of MAME4idroid are not
 * obligated to grant this special exception for their modified versions; it
 * is their choice whether to do so. The GNU General Public License
 * gives permission to release a modified version without this exception;
 * this exception also makes it possible to release a modified version
 * which carries forward this exception.
 *
 * MAME4droid is dual-licensed: Alternatively, you can license MAME4droid
 * under a MAME license, as set out in http://mamedev.org/
 */

package com.seleuco.mame4droid.prefs;
import com.seleuco.mame4droid.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.seleuco.mame4droid.R;
import com.seleuco.mame4droid.input.GameController;
import com.seleuco.mame4droid.prefs.LanguageSwitchActivity; 

public class DefineKeys extends ListActivity {

    private Context context;
    private String[] playerLabels;

    public DefineKeys() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        LanguageSwitchActivity.updateLanguage(this);
        setTitle(R.string.define_keys_label);
    }

	public DefineKeys(Context context) {
	this.context = context; 
	}

    protected int playerIndex = 0;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        LanguageSwitchActivity.updateLanguage(this); 

        playerLabels = new String[]{
            getString(R.string.definekeys_p1),
            getString(R.string.definekeys_p2),
            getString(R.string.definekeys_p3),
            getString(R.string.definekeys_p4),
        };

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        drawListAdapter();
    }

    private void drawListAdapter() {
        final Context context = this;

        ArrayAdapter<String> keyLabelsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, playerLabels) {
            @Override
            public View getView(final int position, final View convertView,
                                final ViewGroup parent) {
                final TextView textView = new TextView(context);
                textView.setPadding(60, 0, 60, 0);
                textView.setTextAppearance(context, R.style.ListText);
                textView.setText(getItem(position));
                return textView;
            }
        };

        setListAdapter(keyLabelsAdapter);
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        playerIndex = position;

        boolean auto_detected = false;
        try {
            auto_detected = GameController.deviceIDs[position] != -1;
        } catch (Throwable e) {
        }

        if (auto_detected) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle(R.string.gamepad_auto_detected_title);
            alertDialog.setPositiveButton(R.string.dialog_dismiss, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.setMessage(context.getString(R.string.gamepad_auto_detected_msg1) + (position + 1) + context.getString(R.string.gamepad_auto_detected_msg2));
            alertDialog.show();
        } else {
            startActivityForResult(new Intent(this, ListKeys.class).putExtra(
                    "playerIndex", playerIndex), 0);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        drawListAdapter();
    }
}

