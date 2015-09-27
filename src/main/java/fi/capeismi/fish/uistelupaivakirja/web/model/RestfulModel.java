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

import fi.capeismi.fish.uistelupaivakirja.web.controller.LoginService;
import fi.capeismi.fish.uistelupaivakirja.web.dao.Collection;
import fi.capeismi.fish.uistelupaivakirja.web.dao.DAOStore;
import fi.capeismi.fish.uistelupaivakirja.web.dao.Type;
import fi.capeismi.fish.uistelupaivakirja.web.dao.User;

/**
 *
 * @author Samuli Penttilä <samuli.penttila@gmail.com>
 */
public class RestfulModel {

	private DAOStore daoStore;
	private User user;

	public RestfulModel(String user) {
		this.daoStore = new DAOStore();
		this.user = daoStore.getUser(user);
		if (user == null) {
			throw new RestfulException("no such user");
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		User template = getUser();
		if (user.getPlaintextpassword() != null) {
			user.setSalt(LoginService.generateSalt());
			String hash = LoginService.getMD5Hash(user.getPlaintextpassword(), user.getSalt());
			user.setPassword(hash);
		} else {
			user.setSalt(template.getSalt());
			user.setPassword(template.getPassword());
		}

		if (user.getPublishfish() == null) {
			user.setPublishfish(template.getPublishfish());
		}

		if (user.getPublishlocation() == null) {
			user.setPublishlocation(template.getPublishlocation());
		}

		if (user.getPublishlure() == null) {
			user.setPublishlure(template.getPublishlocation());
		}

		if (user.getPublishplace() == null) {
			user.setPublishplace(template.getPublishplace());
		}

		if (user.getPublishtrip() == null) {
			user.setPublishtrip(template.getPublishtrip());
		}

		user.setId(getUser().getId());
		user.setUsername(getUser().getUsername());

		this.daoStore.setUser(user);
	}

	public Collection getTrollingObjects(String type) {
		Collection dao = this.daoStore.getCollection(type, getUser());
		return dao;
	}

	public Type getType(String typename) {
		return DAOStore.getType(typename);
	}

	public Integer appendTrollingObjects(Collection objects) {
		this.daoStore.appendCollection(objects, getUser());
		return new Integer(objects.getRevision());
	}

	public Integer setTrollingObjects(Collection objects) {
		this.daoStore.setCollection(objects, getUser());
		return new Integer(objects.getRevision());
	}

	public void deleteTrollingObject(int identifier, int revision, String type) {
		this.daoStore.deleteObject(identifier, revision, type, getUser());
	}
}
