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
package org.springframework.security.oauth2.server.resource.authentication.embedded;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.PrincipalGrantedAuthoritiesService;
import org.springframework.util.Assert;

import com.c4soft.oauth2.TokenProperties;

/**
 * @author Jérôme Wacongne &lt;ch4mp#64;c4-soft.com&gt;
 *
 */
public class AuthoritiesClaimGrantedAuthoritiesService implements PrincipalGrantedAuthoritiesService {
	public static final String AUTHORITIES_CLAIM_NAME = "authorities";

	@Override
	public Collection<GrantedAuthority> getAuthorities(Principal principal) {
		Assert.isTrue(
				principal instanceof TokenProperties,
				"principal must be an instance of TokenProperties (was " + principal == null ? "null" : principal.getClass().getName() + ")");

		final TokenProperties claims = (TokenProperties) principal;
		final Set<String> authoritiesClaim = claims.getAsStringSet(AUTHORITIES_CLAIM_NAME);

		Assert.notNull(authoritiesClaim, "principal has no \"" + AUTHORITIES_CLAIM_NAME + "\" claim");

		return authoritiesClaim.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
	}

}