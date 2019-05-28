/*
 * Copyright 2019 Jérôme Wacongne
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.c4soft.springaddons.showcase;

import java.security.Principal;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.PrincipalGrantedAuthoritiesService;

/**
 * @author Jérôme Wacongne &lt;ch4mp#64;c4-soft.com&gt;
 *
 */
public class JpaGrantedAuthoritiesService implements PrincipalGrantedAuthoritiesService {

	private final UserRepository authoritiesRepo;

	@Autowired
	public JpaGrantedAuthoritiesService(UserRepository authoritiesRepo) {
		this.authoritiesRepo = authoritiesRepo;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities(Principal principal) {
		return authoritiesRepo.findAuthoritiesById(UUID.fromString(principal.getName()))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
	}

}