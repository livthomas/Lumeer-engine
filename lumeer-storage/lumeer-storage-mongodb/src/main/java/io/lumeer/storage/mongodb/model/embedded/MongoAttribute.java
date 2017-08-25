/*
 * -----------------------------------------------------------------------\
 * Lumeer
 *  
 * Copyright (C) since 2016 the original author or authors.
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
 * -----------------------------------------------------------------------/
 */
package io.lumeer.storage.mongodb.model.embedded;

import io.lumeer.api.model.Attribute;
import io.lumeer.engine.api.LumeerConst;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Embedded
public class MongoAttribute implements Attribute {

   public static final String NAME = LumeerConst.Collection.ATTRIBUTE_NAME;
   public static final String FULL_NAME = LumeerConst.Collection.ATTRIBUTE_FULL_NAME;
   public static final String CONSTRAINTS = LumeerConst.Collection.ATTRIBUTE_CONSTRAINTS;
   public static final String USAGE_COUNT = LumeerConst.Collection.ATTRIBUTE_COUNT;

   @Property(NAME)
   private String name;

   @Property(FULL_NAME)
   private String fullName;

   @Property(CONSTRAINTS)
   private Set<String> constraints;

   @Property(USAGE_COUNT)
   private Integer usageCount;

   public MongoAttribute() {
   }

   public MongoAttribute(Attribute attribute) {
      this.name = attribute.getName();
      this.fullName = attribute.getFullName();
      this.constraints = new HashSet<>(attribute.getConstraints());
      this.usageCount = attribute.getUsageCount();
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public String getFullName() {
      return fullName;
   }

   @Override
   public Set<String> getConstraints() {
      return constraints;
   }

   @Override
   public Integer getUsageCount() {
      return usageCount;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof Attribute)) {
         return false;
      }

      final Attribute that = (Attribute) o;

      return getFullName() != null ? getFullName().equals(that.getFullName()) : that.getFullName() == null;
   }

   @Override
   public int hashCode() {
      return getFullName() != null ? getFullName().hashCode() : 0;
   }

   @Override
   public String toString() {
      return "MongoAttribute{" +
            "name='" + name + '\'' +
            ", fullName='" + fullName + '\'' +
            ", constraints=" + constraints +
            ", usageCount=" + usageCount +
            '}';
   }

   public static MongoAttribute convert(Attribute attribute) {
      return attribute instanceof MongoAttribute ? (MongoAttribute) attribute : new MongoAttribute(attribute);
   }

   public static List<MongoAttribute> convert(List<Attribute> attributes) {
      return attributes.stream()
                       .map(MongoAttribute::convert)
                       .collect(Collectors.toList());
   }
}
