/*
 * Copyright 2019 Jérôme Wacongne.
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
package com.c4_soft.springaddons.test.security.context.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import com.c4_soft.springaddons.test.security.context.support.StringAttribute.BooleanParser;
import com.c4_soft.springaddons.test.security.context.support.StringAttribute.DoubleParser;
import com.c4_soft.springaddons.test.security.context.support.StringAttribute.FloatParser;
import com.c4_soft.springaddons.test.security.context.support.StringAttribute.InstantParser;
import com.c4_soft.springaddons.test.security.context.support.StringAttribute.IntegerParser;
import com.c4_soft.springaddons.test.security.context.support.StringAttribute.LongParser;
import com.c4_soft.springaddons.test.security.context.support.StringAttribute.NoOpParser;
import com.c4_soft.springaddons.test.security.context.support.StringAttribute.SpacedSeparatedStringsParser;
import com.c4_soft.springaddons.test.security.context.support.StringAttribute.StringListParser;
import com.c4_soft.springaddons.test.security.context.support.StringAttribute.StringSetParser;
import com.c4_soft.springaddons.test.security.context.support.StringAttribute.UrlParser;
import com.c4_soft.springaddons.test.security.context.support.WithMockJwt.Factory;
import com.c4_soft.springaddons.test.security.support.jwt.JwtAuthenticationTokenTestingBuilder;

/**
 * <p>
 * A lot like {@link WithMockUser @WithMockUser}: when used with {@link WithSecurityContextTestExecutionListener} this
 * annotation can be added to a test method to emulate running with a mocked {@link JwtAuthenticationToken}.
 * </p>
 * <p>
 * Main steps are:
 * </p>
 * <ul>
 * <li>A {@link Jwt JWT} is created as per this annotation {@code name} (forces {@code subject} claim), {@code headers}
 * and {@code claims}</li>
 * <li>A {@link JwtAuthenticationToken JwtAuthenticationToken} is then created and fed with this new JWT token</li>
 * <li>An empty {@link SecurityContext} is instantiated and populated with this {@code JwtAuthenticationToken}</li>
 * </ul>
 * <p>
 * As a result, the {@link Authentication} {@link MockMvc} gets from security context will have the following
 * properties:
 * </p>
 * <ul>
 * <li>{@link Authentication#getPrincipal() getPrincipal()} returns a {@link Jwt}</li>
 * <li>{@link Authentication#getName() getName()} returns the JWT {@code subject} claim, set from this annotation
 * {@code name} value ({@code "user"} by default)</li>
 * <li>{@link Authentication#getAuthorities() authorities} will be a collection of {@link SimpleGrantedAuthority} as
 * defined by this annotation {@link #scopes()} ({@code "SCOPE_USER" } by default)</li>
 * </ul>
 *
 * Sample Usage:
 *
 * <pre>
 * &#64;WithMockJwt
 * &#64;Test
 * public void testSomethingWithDefaultJwtAuthentication() {
 *   //identified as "user" granted with [ROLE_USER]
 *   //claims contain "sub" (subject) with "ch4mpy" as value
 *   //headers can't be empty, so a default one is set
 *   ...
 * }
 *
 * &#64;WithMockJwt({"ROLE_USER", "ROLE_ADMIN"})
 * &#64;Test
 * public void testSomethingWithCustomJwtAuthentication() {
 *   //identified as "user" granted with [ROLE_USER, ROLE_ADMIN]
 *   ...
 * }
 *
 * &#64;WithMockJwt(claims = &#64;StringAttribute(name = "scp", value = "message:read message:write"), scopesClaimeName = "scp")
 * &#64;Test
 * public void testSomethingWithCustomJwtAuthentication() {
 *   //identified as "user" granted with [SCOPE_message:read, SCOPE_message:write]
 *   ...
 * }
 * </pre>
 *
 * To help testing with custom claims as per last sample, many parsers are provided to parse String values:
 * <ul>
 * <li>{@link BooleanParser}</li>
 * <li>{@link DoubleParser}</li>
 * <li>{@link FloatParser}</li>
 * <li>{@link InstantParser}</li>
 * <li>{@link IntegerParser}</li>
 * <li>{@link LongParser}</li>
 * <li>{@link NoOpParser}</li>
 * <li>{@link SpacedSeparatedStringsParser}</li>
 * <li>{@link StringListParser}</li>
 * <li>{@link StringSetParser}</li>
 * <li>{@link UrlParser}</li>
 * </ul>
 *
 * @see StringAttribute
 * @see AttributeValueParser
 *
 * @author Jérôme Wacongne &lt;ch4mp&#64;c4-soft.com&gt;
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = Factory.class)
public @interface WithMockJwt {

	@AliasFor("authorities")
	String[] value() default {};

	@AliasFor("value")
	String[] authorities() default {};

	StringAttribute[] claims() default {};

	/**
	 * Of little use at unit test time...
	 * @return JWT headers
	 */
	StringAttribute[] headers() default {};

	@AliasFor("subject")
	String name() default "";

	@AliasFor("name")
	String subject() default "";

	String[] scopes() default {};

	String tokenValue() default "";

	/**
	 * Determines when the {@link SecurityContext} is setup. The default is before
	 * {@link TestExecutionEvent#TEST_METHOD} which occurs during
	 * {@link org.springframework.test.context.TestExecutionListener#beforeTestMethod(TestContext)}
	 * @return the {@link TestExecutionEvent} to initialize before
	 */
	@AliasFor(annotation = WithSecurityContext.class)
	TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_METHOD;

	public final class Factory implements WithSecurityContextFactory<WithMockJwt> {
		private final StringAttributeParserSupport parsingSupport = new StringAttributeParserSupport();

		private final Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter;

		@Autowired
		public Factory(Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter) {
			this.authoritiesConverter = authoritiesConverter;
		}

		@Override
		public SecurityContext createSecurityContext(WithMockJwt annotation) {
			final SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authentication(annotation));

			return context;
		}

		public JwtAuthenticationToken authentication(WithMockJwt annotation) {
			final var authenticationBuilder = new JwtAuthenticationTokenTestingBuilder<>(authoritiesConverter)
					.token(jwt -> jwt
							.headers(headers -> headers.putAll(this.parsingSupport.parse(annotation.headers())))
							.claims(claims -> this.parsingSupport.parse(annotation.claims()).forEach(claims::claim)));

			if(StringUtils.hasLength(annotation.tokenValue())) {
				authenticationBuilder.token(jwt -> jwt.tokenValue(annotation.tokenValue()));
			}
			if(StringUtils.hasLength(annotation.name())) {
				authenticationBuilder.name(annotation.name());
			}
			final var scopes = Stream.of(annotation.scopes()).collect(Collectors.joining(" "));
			if(StringUtils.hasLength(scopes)) {
				authenticationBuilder.token(jwt -> jwt.claim("scope", scopes));
			}
			if(annotation.authorities().length > 0) {
				authenticationBuilder.authorities(annotation.authorities());
			}

			return authenticationBuilder.build();
		}
	}
}
