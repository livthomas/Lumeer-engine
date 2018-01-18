/*
 * Lumeer: Modern Data Definition and Processing Platform
 *
 * Copyright (C) since 2017 Answer Institute, s.r.o. and/or its affiliates.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.lumeer.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Collections;
import java.util.List;
import javax.annotation.concurrent.Immutable;

@Immutable
public class SmartDocTemplate {

   private final String id;
   private final String collectionCode;
   private final List<SmartDocTemplatePart> parts;

   @JsonCreator
   public SmartDocTemplate(final String id, final String collectionCode, final List<SmartDocTemplatePart> parts) {
      this.id = id;
      this.collectionCode = collectionCode;
      this.parts = parts;
   }

   public String getId() {
      return id;
   }

   public String getCollectionCode() {
      return collectionCode;
   }

   public List<SmartDocTemplatePart> getParts() {
      return Collections.unmodifiableList(parts);
   }

   @Override
   public String toString() {
      return "SmartDocTemplate{" +
            "id='" + id + '\'' +
            ", collectionCode='" + collectionCode + '\'' +
            ", parts=" + parts +
            '}';
   }
}
