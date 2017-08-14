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
package io.lumeer.storage.mongodb.model;

import io.lumeer.engine.api.LumeerConst;
import io.lumeer.api.model.Organization;
import io.lumeer.api.model.Permissions;
import io.lumeer.storage.mongodb.model.embedded.MongoPermissions;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;

@Entity(LumeerConst.Organization.COLLECTION_NAME)
public class MongoOrganization extends MorphiaEntity implements Organization {

   public static final String CODE = LumeerConst.Organization.ATTR_ORG_CODE;
   public static final String NAME = LumeerConst.Organization.ATTR_ORG_NAME;
   public static final String ICON = LumeerConst.Organization.ATTR_META_ICON;
   public static final String COLOR = LumeerConst.Organization.ATTR_META_COLOR;
   public static final String PERMISSIONS = "permissions";

   @Property(CODE)
   @Indexed(options = @IndexOptions(unique = true))
   private String code;

   @Property(NAME)
   private String name;

   @Property(ICON)
   private String icon;

   @Property(COLOR)
   private String color;

   @Embedded(PERMISSIONS)
   private MongoPermissions permissions;

   public MongoOrganization() {
   }

   public MongoOrganization(final Organization organization) {
      super(organization.getId());

      this.code = organization.getCode();
      this.name = organization.getName();
      this.icon = organization.getIcon();
      this.color = organization.getColor();
      this.permissions = new MongoPermissions(organization.getPermissions());
   }

   @Override
   public String getCode() {
      return code;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public String getIcon() {
      return icon;
   }

   @Override
   public String getColor() {
      return color;
   }

   @Override
   public Permissions getPermissions() {
      return permissions;
   }

   public void setCode(final String code) {
      this.code = code;
   }

   public void setName(final String name) {
      this.name = name;
   }

   public void setIcon(final String icon) {
      this.icon = icon;
   }

   public void setColor(final String color) {
      this.color = color;
   }

   public void setPermissions(final MongoPermissions permissions) {
      this.permissions = permissions;
   }
}
