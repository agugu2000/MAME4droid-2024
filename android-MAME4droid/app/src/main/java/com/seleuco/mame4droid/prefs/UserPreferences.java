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

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.seleuco.mame4droid.Emulator;
import com.seleuco.mame4droid.R;
import com.seleuco.mame4droid.helpers.MainHelper;
import com.seleuco.mame4droid.helpers.PrefsHelper;
import com.seleuco.mame4droid.helpers.ScraperHelper;
import com.seleuco.mame4droid.input.ControlCustomizer;
import com.seleuco.mame4droid.input.GameController;

import com.seleuco.mame4droid.prefs.LanguageSwitchActivity; 

public class UserPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	private Context context;

    public UserPreferences() {

    }

	public UserPreferences(Context context) {
	this.context = context; 
	}

	private SharedPreferences settings;

	protected ListPreference mPrefGlobalVideoRenderMode;
	protected ListPreference mPrefResolution;
	protected ListPreference mPrefOSDResolution;
	protected ListPreference mPrefPortraitMode;
    protected ListPreference mPrefLandsMode;
	protected ListPreference mPrefOverlay;
	protected ListPreference mPrefOrientation;
    protected ListPreference mPrefControllerType;
    protected ListPreference mPrefAnalogDZ;
    protected ListPreference mPrefGamepadDZ;
    protected ListPreference mPrefTiltDZ;
    protected ListPreference mPrefTiltNeutral;

    protected ListPreference mPrefSound;
    protected ListPreference mPrefStickType;
    protected ListPreference mPrefNumButtons;
    protected ListPreference mPrefSizeButtons;
	protected ListPreference mPrefAlphaButtons;
    protected ListPreference mPrefSizeStick;

    protected ListPreference mPrefMainThPr;
    protected ListPreference mPrefSoundEngine;

    protected ListPreference mPrefNavbar;
    protected EditTextPreference mPrefInstPath;

	protected ListPreference mPrefShader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		LanguageSwitchActivity.updateLanguage(this); 

		addPreferencesFromResource(R.xml.userpreferences);

		settings = PreferenceManager.getDefaultSharedPreferences(this);

		mPrefGlobalVideoRenderMode = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_GLOBAL_VIDEO_RENDER_MODE);
		mPrefResolution = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_EMU_RESOLUTION);
		mPrefOSDResolution = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_EMU_RESOLUTION_OSD);
        mPrefPortraitMode = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_PORTRAIT_SCALING_MODE);
        mPrefLandsMode = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_LANDSCAPE_SCALING_MODE);

		mPrefOverlay = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_OVERLAY);
		mPrefOrientation = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_ORIENTATION);

        mPrefControllerType = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_CONTROLLER_TYPE);
        mPrefAnalogDZ = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_ANALOG_DZ);
        mPrefGamepadDZ = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_GAMEPAD_DZ);
        mPrefTiltDZ = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_TILT_DZ);
        mPrefTiltNeutral = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_TILT_NEUTRAL);

        mPrefSound = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_EMU_SOUND);
        mPrefStickType = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_STICK_TYPE);
        mPrefNumButtons = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_NUMBUTTONS);
        mPrefSizeButtons = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_BUTTONS_SIZE);
		mPrefAlphaButtons = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_BUTTONS_ALPHA);
        mPrefSizeStick = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_STICK_SIZE);
        mPrefMainThPr = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_MAIN_THREAD_PRIORITY);
        mPrefSoundEngine = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_SOUND_ENGINE);

		//mPrefOverlayInt = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_OVERLAY_INTENSITY);

		mPrefNavbar = (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_GLOBAL_NAVBAR_MODE);
		mPrefInstPath = (EditTextPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_INSTALLATION_DIR);
		mPrefShader= (ListPreference)getPreferenceScreen().findPreference(PrefsHelper.PREF_SHADER_EFFECT);
		populateShaders(mPrefShader);
	}


	  @Override
	    protected void onResume() {
	        super.onResume();
	        boolean enable;

			LanguageSwitchActivity.updateLanguage(this);
			setTitle(R.string.settings_label);

	        // Setup the initial values
	        //mCheckBoxPreference.setSummary(sharedPreferences.getBoolean(key, false) ? "Disable this setting" : "Enable this setting");
	        mPrefGlobalVideoRenderMode.setSummary(getString(R.string.current_value_string) + mPrefGlobalVideoRenderMode.getEntry()+"'");

	        mPrefResolution.setSummary(getString(R.string.current_value_string) + mPrefResolution.getEntry()+"'");
		    mPrefOSDResolution.setSummary(getString(R.string.current_value_string) + mPrefOSDResolution.getEntry()+"'");
	        mPrefPortraitMode.setSummary(getString(R.string.current_value_string) + mPrefPortraitMode.getEntry()+"'");
	        mPrefLandsMode.setSummary(getString(R.string.current_value_string) + mPrefLandsMode.getEntry()+"'");
			mPrefOverlay.setSummary(getString(R.string.current_value_string) + mPrefOverlay.getEntry()+"'");
		    mPrefOrientation.setSummary(getString(R.string.current_value_string) + mPrefOrientation.getEntry()+"'");

	        mPrefControllerType.setSummary(getString(R.string.current_value_string) + mPrefControllerType.getEntry()+"'");

	        mPrefAnalogDZ.setSummary(getString(R.string.current_value_string) + mPrefAnalogDZ.getEntry()+"'");
	        mPrefGamepadDZ.setSummary(getString(R.string.current_value_string) + mPrefGamepadDZ.getEntry()+"'");
	        mPrefTiltDZ.setSummary(getString(R.string.current_value_string) + mPrefTiltDZ.getEntry()+"'");
	        mPrefTiltNeutral.setSummary(getString(R.string.current_value_string) + mPrefTiltNeutral.getEntry()+"'");
	        mPrefSound.setSummary(getString(R.string.current_value_string) + mPrefSound.getEntry()+"'");
	        mPrefStickType.setSummary(getString(R.string.current_value_string) + mPrefStickType.getEntry()+"'");
	        mPrefNumButtons.setSummary(getString(R.string.current_value_string) + mPrefNumButtons.getEntry()+"'");
	        mPrefSizeButtons.setSummary(getString(R.string.current_value_string) + mPrefSizeButtons.getEntry()+"'");
		    mPrefAlphaButtons.setSummary(getString(R.string.current_value_string) + mPrefAlphaButtons.getEntry()+"'");
	        mPrefSizeStick.setSummary(getString(R.string.current_value_string) + mPrefSizeStick.getEntry()+"'");
	        mPrefMainThPr.setSummary(getString(R.string.current_value_string) + mPrefMainThPr.getEntry()+"'");
	        mPrefSoundEngine.setSummary(getString(R.string.current_value_string) + mPrefSoundEngine.getEntry()+"'");
	        mPrefNavbar.setSummary(getString(R.string.current_value_string) + mPrefNavbar.getEntry()+"'");
			mPrefInstPath.setSummary(getString(R.string.current_value_string) + mPrefInstPath.getText()+"'");
		    mPrefShader.setSummary(getString(R.string.current_value_string) + mPrefShader.getEntry()+"'");

	        // Set up a listener whenever a key changes
	        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

	    }

	    @Override
	    protected void onPause() {
	        super.onPause();

	        // Unregister the listener whenever a key changes
	        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	    }

	    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	        // Let's do something a preference values changes
	    	/*
	        if (key.equals(KEY_CHECKBOX_PREFERENCE)) {
	          mCheckBoxPreference.setSummary(sharedPreferences.getBoolean(key, false) ? "Disable this setting" : "Enable this setting");
	        }
	        else*/
	        if (key.equals(PrefsHelper.PREF_PORTRAIT_SCALING_MODE))
	        {
	            mPrefPortraitMode.setSummary(getString(R.string.current_value_string) + mPrefPortraitMode.getEntry()+"'");
	        }
	        else if(key.equals(PrefsHelper.PREF_LANDSCAPE_SCALING_MODE))
	        {
	        	mPrefLandsMode.setSummary(getString(R.string.current_value_string) + mPrefLandsMode.getEntry()+"'");
	        }
			else if(key.equals(PrefsHelper.PREF_OVERLAY))
			{
				mPrefOverlay.setSummary(getString(R.string.current_value_string) + mPrefOverlay.getEntry()+"'");
			}
			else if(key.equals(PrefsHelper.PREF_ORIENTATION))
			{
				mPrefOrientation.setSummary(getString(R.string.current_value_string) + mPrefOrientation.getEntry()+"'");
			}
	        else if(key.equals(PrefsHelper.PREF_CONTROLLER_TYPE))
	        {
	            mPrefControllerType.setSummary(getString(R.string.current_values_string) + mPrefControllerType.getEntry()+"'");
	        }
	        else if(key.equals(PrefsHelper.PREF_GLOBAL_VIDEO_RENDER_MODE))
	        {
				mPrefGlobalVideoRenderMode.setSummary(getString(R.string.current_value_string) + mPrefGlobalVideoRenderMode.getEntry()+"'");
				boolean enable = Integer.valueOf(mPrefGlobalVideoRenderMode.getValue()).intValue() ==PrefsHelper.PREF_RENDER_GL;
	        }
	        else if(key.equals(PrefsHelper.PREF_EMU_RESOLUTION))
	        {
	        	mPrefResolution.setSummary(getString(R.string.current_value_string) + mPrefResolution.getEntry()+"'");
	        }
			else if(key.equals(PrefsHelper.PREF_EMU_RESOLUTION_OSD))
			{
				mPrefOSDResolution.setSummary(getString(R.string.current_value_string) + mPrefOSDResolution.getEntry()+"'");
			}
	        else if(key.equals(PrefsHelper.PREF_ANALOG_DZ))
	        {
	        	mPrefAnalogDZ.setSummary(getString(R.string.current_value_string) + mPrefAnalogDZ.getEntry()+"'");
	        }
	        else if(key.equals(PrefsHelper.PREF_GAMEPAD_DZ))
	        {
	        	mPrefGamepadDZ.setSummary(getString(R.string.current_value_string) + mPrefGamepadDZ.getEntry()+"'");
	        }
	        else if(key.equals(PrefsHelper.PREF_TILT_DZ))
	        {
	        	mPrefTiltDZ.setSummary(getString(R.string.current_value_string) + mPrefTiltDZ.getEntry()+"'");
	        }
	        else if(key.equals(PrefsHelper.PREF_TILT_NEUTRAL))
	        {
	        	mPrefTiltNeutral.setSummary(getString(R.string.current_value_string) + mPrefTiltNeutral.getEntry()+"'");
	        }
		    else if(key.equals(PrefsHelper.PREF_EMU_SOUND))
		    {
		    	mPrefSound.setSummary(getString(R.string.current_value_string) + mPrefSound.getEntry()+"'");
	        }
		    else if(key.equals(PrefsHelper.PREF_STICK_TYPE))
		    {
		    	mPrefStickType.setSummary(getString(R.string.current_value_string) + mPrefStickType.getEntry()+"'");
		    }
		    else if(key.equals(PrefsHelper.PREF_NUMBUTTONS))
		    {
		    	mPrefNumButtons.setSummary(getString(R.string.current_value_string) + mPrefNumButtons.getEntry()+"'");
		    }
		    else if(key.equals(PrefsHelper.PREF_BUTTONS_SIZE))
		    {
		    	mPrefSizeButtons.setSummary(getString(R.string.current_value_string) + mPrefSizeButtons.getEntry()+"'");
		    }
			else if(key.equals(PrefsHelper.PREF_BUTTONS_ALPHA))
			{
				mPrefAlphaButtons.setSummary(getString(R.string.current_value_string) + mPrefAlphaButtons.getEntry()+"'");
			}
		    else if(key.equals(PrefsHelper.PREF_STICK_SIZE))
		    {
		    	mPrefSizeStick.setSummary(getString(R.string.current_value_string) + mPrefSizeStick.getEntry()+"'");
		    }
			else if(key.equals(PrefsHelper.PREF_MAIN_THREAD_PRIORITY))
			{
	            mPrefMainThPr.setSummary(getString(R.string.current_value_string) + mPrefMainThPr.getEntry()+"'");
			}
		    else if(key.equals(PrefsHelper.PREF_SOUND_ENGINE))
		    {
	            mPrefSoundEngine.setSummary(getString(R.string.current_value_string) + mPrefSoundEngine.getEntry()+"'");
		    }
		    else if(key.equals(PrefsHelper.PREF_GLOBAL_NAVBAR_MODE))
		    {
		    	mPrefNavbar.setSummary(getString(R.string.current_value_string) + mPrefNavbar.getEntry()+"'");
		    }
		    else if(key.equals(PrefsHelper.PREF_INSTALLATION_DIR))
		    {
		    	mPrefInstPath.setSummary(getString(R.string.current_value_string) + mPrefInstPath.getText()+"'");
		    }
			else if(key.equals(PrefsHelper.PREF_SHADER_EFFECT))
			{
				mPrefShader.setSummary(getString(R.string.current_value_string) + mPrefShader.getEntry()+"'");
			}
			else if(key.equals(PrefsHelper.PREF_SHADERS_ENABLED))
			{
				SharedPreferences.Editor edit = sharedPreferences.edit();
				boolean enable = sharedPreferences.getBoolean(PrefsHelper.PREF_SHADERS_ENABLED,false);

				android.app.UiModeManager u = (android.app.UiModeManager) this.getSystemService(Context.UI_MODE_SERVICE);
				boolean androidTv  = u.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;

				if(enable){
					edit.putString(PrefsHelper.PREF_EMU_RESOLUTION, "0");
					//if(!androidTv)
					  //edit.putString(PrefsHelper.PREF_EMU_RESOLUTION_OSD, "0");
				}
				else{
					edit.putString(PrefsHelper.PREF_EMU_RESOLUTION, "1");
					//if(!androidTv)
					   //edit.putString(PrefsHelper.PREF_EMU_RESOLUTION_OSD, "1");
				}
				edit.commit();
			}
			else if(key.equals(PrefsHelper.PREF_SCRAPE_ICONS) ||
				key.equals(PrefsHelper.PREF_SCRAPE_SNAPSHOTS) ||
				key.equals(PrefsHelper.PREF_SCRAPE_ALL)){
				ScraperHelper.reset();
			}

	    }

		@Override
		public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
				Preference pref) {

			if (pref.getKey().equals("defineKeys")) {
				startActivityForResult(new Intent(this, DefineKeys.class), 1);
			}
			else if (pref.getKey().equals("changeRomPath")) {
				 AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    	builder.setMessage(R.string.checkboxprefwithwarn_confirm)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.confirm_dialog_yes, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    					SharedPreferences.Editor editor =  settings.edit();
		    					editor.putString(PrefsHelper.PREF_ROMsDIR, null);
		    					editor.commit();
		    					Emulator.setNeedRestart(true);
		    	                //android.os.Process.killProcess(android.os.Process.myPid());
		    	           }
		    	       })
		    	       .setNegativeButton(R.string.confirm_dialog_no, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                dialog.cancel();
		    	           }
		    	       });
			    	Dialog dialog = builder.create();
			    	dialog.show();
			}
			else if (pref.getKey().equals("defaultsKeys")) {

				 AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    	builder.setMessage(R.string.confirm_restore)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.confirm_dialog_yes, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    					SharedPreferences.Editor editor =  settings.edit();

		    					StringBuffer definedKeysStr = new StringBuffer();

		    					for(int i=0; i< GameController.defaultKeyMapping.length;i++)
		    					{
									GameController.keyMapping[i] = GameController.defaultKeyMapping[i];
		    						definedKeysStr.append(GameController.defaultKeyMapping[i]+":");
		    					}
		    					editor.putString(PrefsHelper.PREF_DEFINED_KEYS, definedKeysStr.toString());
		    					editor.commit();
		    					//finish();

		    	           }
		    	       })
		    	       .setNegativeButton(R.string.confirm_dialog_no, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                dialog.cancel();
		    	           }
		    	       });
			    	Dialog dialog = builder.create();
			    	dialog.show();
			}
			else if (pref.getKey().equals("customControlLayout")) {
				ControlCustomizer.setEnabled(true);
				finish();
			}
			else if (pref.getKey().equals("defaultControlLayout")) {

				 AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    	builder.setMessage(R.string.confirm_restore)
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.confirm_dialog_yes, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    					SharedPreferences.Editor editor =  settings.edit();
		    					editor.putString(PrefsHelper.PREF_DEFINED_CONTROL_LAYOUT, null);
		    					editor.putString(PrefsHelper.PREF_DEFINED_CONTROL_LAYOUT_P, null);
		    					editor.commit();
		    	           }
		    	       })
		    	       .setNegativeButton(R.string.confirm_dialog_no, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                dialog.cancel();
		    	           }
		    	       });
			    	Dialog dialog = builder.create();
			    	dialog.show();
			}
			else if (pref.getKey().equals("defaultData")) {

				 AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    	builder.setMessage(getString(R.string.confirm_restore)+getString(R.string.confirm_restore_warning))
		    	       .setCancelable(false)
		    	       .setPositiveButton(R.string.confirm_dialog_yes, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	        	SharedPreferences.Editor editor =  settings.edit();
		    	       		editor.putBoolean(PrefsHelper.PREF_MAME_DEFAULTS, true);
		    	    		editor.commit();
		    	    		Emulator.setNeedRestart(true);
		    	           }
		    	       })
		    	       .setNegativeButton(R.string.confirm_dialog_no, new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                dialog.cancel();
		    	           }
		    	       });
			    	Dialog dialog = builder.create();
			    	dialog.show();
			}

			return super.onPreferenceTreeClick(preferenceScreen, pref);
		}

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);

			if (resultCode == RESULT_OK && requestCode == 0) {
				setResult(RESULT_OK, data);
			}
			else if (requestCode == 1) {
				SharedPreferences.Editor editor =  settings.edit();

				StringBuffer definedKeysStr = new StringBuffer();

				for(int i=0; i< GameController.keyMapping.length;i++)
					definedKeysStr.append(GameController.keyMapping[i]+":");

				editor.putString(PrefsHelper.PREF_DEFINED_KEYS, definedKeysStr.toString());
				editor.commit();
				return;
			}
			finish();
		}

		protected void populateShaders(ListPreference lp){

			CharSequence[] cs = null;
			CharSequence[] csv = null;

			MainHelper mh = new MainHelper(null, this);

			String path = getPreferenceScreen().getSharedPreferences().getString(
				PrefsHelper.PREF_INSTALLATION_DIR, "");

			ArrayList<ArrayList<String>> data = mh.readShaderCfg(path);

			int n = data.size();

			cs = new String[n + 1];
			csv = new String[n + 1];

			cs[0] = "No effect";
			csv[0] = "-1";

			int i = 0;
			while (i < n) {
				ArrayList<String> s = data.get(i);
				if(s.size() >= 2) {
					csv[i + 1] = s.get(0);
					cs[i + 1] = s.get(1);
				}
				i++;
			}
			lp.setEntries(cs);
			lp.setEntryValues(csv);
		}
}
