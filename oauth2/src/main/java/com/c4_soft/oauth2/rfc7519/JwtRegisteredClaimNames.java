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

/**
 * Claim names as defined by https://tools.ietf.org/html/rfc7519#section-4.1
 *
 * @author Jérôme Wacongne &lt;ch4mp#64;c4-soft.com&gt;
 *
 */
public enum JwtRegisteredClaimNames {
	ISSUER("iss"),
	SUBJECT("sub"),
	AUDIENCE("aud"),
	EXPIRATION_TIME("exp"),
	NOT_BEFORE("nbf"),
	ISSUED_AT("iat"),
	JWT_ID("jti");

	public final String value;

	JwtRegisteredClaimNames(String name){
		this.value = name;
	}

	@Override
	public String toString() {
		return value;
	}
}
