package com.patchwork.db.security;

import com.patchwork.db.UserLoader;

public class UserValidator {
	public static boolean validateNewUser(String username, String password, String confirm) {
		if (!password.equals(confirm))
			return false;
		if (UserLoader.checkUserExists(username))
			return false;
		return true;
	}
}
