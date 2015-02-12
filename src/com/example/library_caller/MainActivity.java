/*
 * (C) Copyright 2015 Dennis Titze
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Dennis Titze (https://github.com/titze)
 */
package com.example.library_caller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import dalvik.system.PathClassLoader;

public class MainActivity extends Activity {
	
	// the name of the library package we want to load
	private static final String LIBRARY_PACKAGE_NAME = "com.example.library";
	// the class we want to load from the package
	private static final String LIBRARY_CLASS_NAME = "com.example.library.Library";

	/**
	 * One Button which loads the library and executes it without any verification
	 * @author titze
	 */
	private class ButtonClick_Unverified implements View.OnClickListener {
		public void onClick(View v) {
			String error = null;
			try {
				String apkName = getPackageManager().getApplicationInfo(LIBRARY_PACKAGE_NAME,0).sourceDir;
	
				PathClassLoader pathClassLoader = new dalvik.system.PathClassLoader(
					apkName,
					ClassLoader.getSystemClassLoader());

				Class<?> clazz = Class.forName(LIBRARY_CLASS_NAME, true, pathClassLoader);
				Method method = clazz.getMethod("test");
				method.invoke(null);
			} catch (PackageManager.NameNotFoundException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			} catch (ClassNotFoundException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			} catch (NoSuchMethodException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			} catch (IllegalAccessException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			} catch (IllegalArgumentException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			} catch (InvocationTargetException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			}
			if (error != null) {
				Toast errorToast = Toast.makeText(MainActivity.this, "Error:\n"+error, Toast.LENGTH_LONG);
				errorToast.show();
			}
		}
	}
	
	/**
	 * One Button which loads the library and executes it with proper verification
	 * @author titze
	 */
	private class ButtonClick_Verified implements View.OnClickListener {
		public void onClick(View v) {
			String error = null;
			Context c = MainActivity.this;
			Log.v("Test", "App context is " + c.toString());
			try {
				if (Verifier.verify(c, LIBRARY_PACKAGE_NAME)) {
					String apkName = c.getPackageManager().getApplicationInfo(LIBRARY_PACKAGE_NAME,0).sourceDir;
		
					PathClassLoader pathClassLoader = new dalvik.system.PathClassLoader(
						apkName,
						ClassLoader.getSystemClassLoader());
	
					Class<?> clazz = Class.forName(LIBRARY_CLASS_NAME, true, pathClassLoader);
					Method method = clazz.getMethod("test");
					method.invoke(null);
				} else {
					error = "Library could not be verified";
					Log.e("Unverified Caller", error);
				}
			} catch (PackageManager.NameNotFoundException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			} catch (ClassNotFoundException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			} catch (NoSuchMethodException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			} catch (IllegalAccessException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			} catch (IllegalArgumentException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			} catch (InvocationTargetException e) {
				error = e.getMessage();
				Log.e("Unverified Caller", e.toString());
			}
			if (error != null) {
				Toast errorToast = Toast.makeText(MainActivity.this, "Error:\n"+error, Toast.LENGTH_LONG);
				errorToast.show();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new ButtonClick_Unverified());
		final Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new ButtonClick_Verified());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
