package com.agenda.handler;

import com.agenda.entity.UsersEntity;

public interface UsersInterface {
	 UsersEntity postLogin(String username, String password);
}
