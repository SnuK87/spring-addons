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
package com.c4_soft.oauth2.rfc7519;

import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.c4_soft.oauth2.ModifiableClaimSet;
import com.c4_soft.oauth2.UnmodifiableClaimSet;

/**
 * <p>As per https://tools.ietf.org/html/rfc7519#section-3, the JWT is a claim-set only.
 * JOSE headers are a separate object.</p>
 *
 * <p>Might be extended to add public or private claim accessors</p>
 *
 * @author Jérôme Wacongne &lt;ch4mp#64;c4-soft.com&gt;
 *
 */
public class JwtClaimSet extends UnmodifiableClaimSet implements Principal {

	public JwtClaimSet(Map<String, Object> claims) {
		super(claims);
	}

	@Override
	public String getName() {
		return getSubject();
	}

	public String getIssuer() {
		return getAsString(JwtRegisteredClaimNames.ISSUER.value);
	}

	public String getSubject() {
		return getAsString(JwtRegisteredClaimNames.SUBJECT.value);
	}

	public Collection<String> getAudience() {
		return getAsStringSet(JwtRegisteredClaimNames.AUDIENCE.value);
	}

	public Instant getExpirationTime() {
		return getAsInstant(JwtRegisteredClaimNames.EXPIRATION_TIME.value);
	}

	public Instant getNotBefore() {
		return getAsInstant(JwtRegisteredClaimNames.NOT_BEFORE.value);
	}

	public Instant getIssuedAt() {
		return getAsInstant(JwtRegisteredClaimNames.ISSUED_AT.value);
	}

	public String getJwtId() {
		return getAsString(JwtRegisteredClaimNames.JWT_ID.value);
	}

	public static Builder<?> builder() {
		return new Builder<>();
	}

	public static class Builder<T extends Builder<T>> extends ModifiableClaimSet {
		private static final long serialVersionUID = -7716737980247084105L;

		public T issuer(String issuer) {
			claim(JwtRegisteredClaimNames.ISSUER.value, issuer);
			return downcast();
		}

		public T subject(String subject) {
			claim(JwtRegisteredClaimNames.SUBJECT.value, subject);
			return downcast();
		}

		public T audience(Stream<String> audience) {
			claim(JwtRegisteredClaimNames.AUDIENCE.value, audience.collect(Collectors.toSet()));
			return downcast();
		}

		public T audience(String... audience) {
			return audience(Stream.of(audience));
		}

		public T audience(Collection<String> audience) {
			return audience(audience.stream());
		}

		public T expirationTime(Instant expirationTime) {
			claim(JwtRegisteredClaimNames.EXPIRATION_TIME.value, expirationTime.getEpochSecond());
			return downcast();
		}

		public T expiresIn(long seconds) {
			expirationTime(Instant.now().plus(Duration.ofSeconds(seconds)));
			return downcast();
		}

		public T notBefore(Instant notBefore) {
			claim(JwtRegisteredClaimNames.NOT_BEFORE.value, notBefore.getEpochSecond());
			return downcast();
		}

		public T issuedAt(Instant issuedAt) {
			claim(JwtRegisteredClaimNames.ISSUED_AT.value, issuedAt.getEpochSecond());
			return downcast();
		}

		public T jwtId(String jwtId) {
			claim(JwtRegisteredClaimNames.JWT_ID.value, jwtId);
			return downcast();
		}

		public JwtClaimSet build() {
			return build(this);
		}

		public JwtClaimSet build(Map<String, Object> claims) {
			return new JwtClaimSet(claims);
		}

		@SuppressWarnings("unchecked")
		protected T downcast() {
			return (T) this;
		}
	}
}
