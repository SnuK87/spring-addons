/*
 * Copyright 2019 Jérôme Wacongne
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.c4_soft.springaddons.test.security.support.introspection;

import java.util.Map;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;

import com.c4_soft.oauth2.rfc7662.IntrospectionClaimSet;
import com.c4_soft.springaddons.security.oauth2.server.resource.authentication.OAuth2ClaimSetAuthentication;
import com.c4_soft.springaddons.test.security.web.reactive.server.AuthenticationConfigurer;

public class IntrospectionClaimSetAuthenticationWebTestClientConfigurer
		extends
		IntrospectionClaimSetAuthenticationTestingBuilder<IntrospectionClaimSet, IntrospectionClaimSetAuthenticationWebTestClientConfigurer>
		implements
		AuthenticationConfigurer<OAuth2ClaimSetAuthentication<IntrospectionClaimSet>> {

	public IntrospectionClaimSetAuthenticationWebTestClientConfigurer(
			Converter<Map<String, Object>, Set<GrantedAuthority>> authoritiesConverter) {
		super(authoritiesConverter, claimsMap -> new IntrospectionClaimSet(claimsMap));
	}
}