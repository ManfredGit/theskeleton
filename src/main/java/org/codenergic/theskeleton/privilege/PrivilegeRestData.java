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
package org.codenergic.theskeleton.privilege;

import javax.annotation.Nullable;

import org.codenergic.theskeleton.core.data.RestData;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

@SuppressWarnings("serial")
@AutoValue
@JsonDeserialize(builder = AutoValue_PrivilegeRestData.Builder.class)
public abstract class PrivilegeRestData implements RestData {
	@Nullable
	abstract String getId();
	@Nullable
	abstract String getName();
	@Nullable
	abstract String getDescription();

	public static Builder builder() {
		return new AutoValue_PrivilegeRestData.Builder();
	}

	PrivilegeEntity toPrivilegeEntity() {
		return new PrivilegeEntity()
			.setId(getId())
			.setName(getName())
			.setDescription(getDescription());
	}

	@AutoValue.Builder
	@JsonPOJOBuilder(withPrefix = "")
	public interface Builder {
		Builder id(String id);
		Builder name(String id);
		Builder description(String description);

		PrivilegeRestData build();

		default Builder fromPrivilegeEntity(PrivilegeEntity privilege) {
			return id(privilege.getId())
				.name(privilege.getName())
				.description(privilege.getDescription());
		}
	}
}
