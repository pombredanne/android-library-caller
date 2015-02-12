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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * This verification library is provided by the library developer and has to stay unchanged.
 * The library checks the hash sum of this verification, and denies the loading if it does not perform correct verification. 
 * @author titze
 */
public class Verifier {

	public static String bytesToHex(byte[] in) {
		final StringBuilder builder = new StringBuilder();
		for(byte b : in) {
			builder.append(String.format("%02x", b).toUpperCase());
			builder.append(":");
		}
		return builder.substring(0, builder.length()-1);
	}

	/**
	 * Verification function
	 * @param context The context of this app (to easily get the library install location)
	 * @param packageName The package name of the library
	 * @return
	 */
	public static boolean verify(Context context, String packageName) {
		//this is the DEBUG_KEY. Change to production signature.
		final String valid_signature = "98:68:E5:CD:85:8B:E0:AB:93:9D:63:2A:05:F5:34:9C:6C:9B:10:13:D5:76:CD:1A:B4:DB:83:8A:FD:CC:43:8A";
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);

			for (final Signature sig : packageInfo.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA256");
				md.update(sig.toByteArray());
				String key = bytesToHex(md.digest());
				// at least one key matches, the library is ok.
				if (key.equals(valid_signature)) {
					return true;
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}

}
