/*
 * Copyright 2018 original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codenergic.theskeleton.role;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.codenergic.theskeleton.core.web.User;
import org.codenergic.theskeleton.user.UserEntity;
import org.codenergic.theskeleton.user.UserRestData;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{username}/roles")
public class UserRoleRestController {
	private final RoleService roleService;

	public UserRoleRestController(RoleService roleService) {
		this.roleService = roleService;
	}

	@PutMapping
	public UserRestData addRoleToUser(@User UserEntity user, @RequestBody Map<String, String> body) {
		return UserRestData.builder(roleService.addRoleToUser(user.getUsername(), body.get("role"))).build();
	}

	@GetMapping
	public Set<RoleRestData> findRolesByUserUsername(@User UserEntity user) {
		return roleService.findRolesByUserUsername(user.getUsername()).stream()
			.map(r -> RoleRestData.builder(r).build())
			.collect(Collectors.toSet());
	}

	@DeleteMapping
	public UserRestData removeRoleFromUser(@User UserEntity user, @RequestBody Map<String, String> body) {
		return UserRestData.builder(roleService.removeRoleFromUser(user.getUsername(), body.get("role"))).build();
	}
}
