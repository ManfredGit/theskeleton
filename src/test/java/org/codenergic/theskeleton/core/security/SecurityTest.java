/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenergic.theskeleton.core.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.codenergic.theskeleton.client.OAuth2ClientService;
import org.codenergic.theskeleton.core.security.SecurityTest.SecurityTestConfiguration;
import org.codenergic.theskeleton.user.UserEntity;
import org.codenergic.theskeleton.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SecurityTestConfiguration.class }, webEnvironment = WebEnvironment.MOCK)
public class SecurityTest {
	@Test
	public void testConvertUserAuthentication1() {
		UserAuthenticationConverter authenticationConverter = new UserAccessTokenAuthenticationConverter();
		Authentication authentication = mock(Authentication.class);
		Map<String, ?> response = authenticationConverter.convertUserAuthentication(authentication);
		assertThat(response).hasSize(1);
		assertThat(response).containsKey("user_name");
		verify(authentication).getPrincipal();
	}

	@Test
	public void testConvertUserAuthentication2() {
		UserAuthenticationConverter authenticationConverter = new UserAccessTokenAuthenticationConverter();
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(new UserEntity().setId("123"));
		Map<String, ?> response = authenticationConverter.convertUserAuthentication(authentication);
		assertThat(response).hasSize(2);
		assertThat(response).containsKey("user_name");
		assertThat(response).containsKey("user_id");
		assertThat(response.get("user_id")).isEqualTo("123");
		verify(authentication, times(2)).getPrincipal();
	}

	@Test
	public void testExtractAuthentication() {
		UserAuthenticationConverter authenticationConverter = new UserAccessTokenAuthenticationConverter();
		Map<String, Object> map = new HashMap<>();
		map.put("user_name", "username");
		assertThat(authenticationConverter.extractAuthentication(map)).isNull();
		map.put("user_id", "123");
		Authentication authentication = authenticationConverter.extractAuthentication(map);
		assertThat(authentication.getPrincipal()).isInstanceOf(UserEntity.class);
		map.remove("user_name");
		assertThat(authenticationConverter.extractAuthentication(map)).isNull();
	}

	@Configuration
	@EnableAutoConfiguration
	@ComponentScan(basePackageClasses = SecurityTest.class)
	public static class SecurityTestConfiguration {
		@Bean
		public ApprovalStore mockApprovalStore() {
			return mock(ApprovalStore.class);
		}

		@Bean
		public UserService mockUserService() {
			return mock(UserService.class);
		}

		@Bean
		public OAuth2ClientService mockOAuth2ClientService() {
			return mock(OAuth2ClientService.class);
		}
	}
}
