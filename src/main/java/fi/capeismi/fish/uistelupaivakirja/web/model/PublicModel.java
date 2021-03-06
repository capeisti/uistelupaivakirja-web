/*
 * Copyright (C) 2011 Samuli Penttilä <samuli.penttila@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fi.capeismi.fish.uistelupaivakirja.web.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.capeismi.fish.uistelupaivakirja.web.controller.LoginService;
import fi.capeismi.fish.uistelupaivakirja.web.dao.DAOStore;
import fi.capeismi.fish.uistelupaivakirja.web.dao.User;

/**
 *
 * @author Samuli Penttilä <samuli.penttila@gmail.com>
 */
@Component
public class PublicModel {

	@Autowired
	private DAOStore daoStore = null;

	public void addUser(User user) {
		if (this.daoStore.getUser(user.getUsername()) != null) {
			throw new RestfulException("user already exists");
		}

		user.setSalt(LoginService.generateSalt());
		String hash = LoginService.getMD5Hash(user.getPlaintextpassword(), user.getSalt());
		user.setPassword(hash);
		this.daoStore.addUser(user);
	}
}
