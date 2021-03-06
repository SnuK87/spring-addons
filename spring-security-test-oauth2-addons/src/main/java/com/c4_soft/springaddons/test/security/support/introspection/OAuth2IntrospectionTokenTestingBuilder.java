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
package com.c4_soft.springaddons.test.security.support.introspection;

import com.c4_soft.oauth2.rfc6749.TokenType;
import com.c4_soft.springaddons.test.security.support.Defaults;
import com.c4_soft.springaddons.test.security.support.missingpublicapi.OAuth2IntrospectionToken;
import com.c4_soft.springaddons.test.security.support.missingpublicapi.OAuth2IntrospectionToken.OAuth2IntrospectionTokenBuilder;

/**
 * Builder with test default values for {@link OAuth2IntrospectionToken}
 *
 * @author Jérôme Wacongne &lt;ch4mp#64;c4-soft.com&gt;
 */
public class OAuth2IntrospectionTokenTestingBuilder extends OAuth2IntrospectionTokenBuilder<OAuth2IntrospectionTokenTestingBuilder> {
	public OAuth2IntrospectionTokenTestingBuilder() {
		attributes(claims -> claims.subject(Defaults.AUTH_NAME).username(Defaults.AUTH_NAME).tokenType(TokenType.BEARER));
		value(Defaults.BEARER_TOKEN_VALUE);
	}

	@Override
	public OAuth2IntrospectionToken build() {
		super.attributes(claimsBuilder -> {
			final var claims = claimsBuilder.build();
			if(claims.getScope() == null || claims.getScope().isEmpty()) {
				claimsBuilder.scopes(Defaults.SCOPES);
			}
		});
		return super.build();
	}
}
