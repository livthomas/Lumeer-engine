/*
 * Lumeer: Modern Data Definition and Processing Platform
 *
 * Copyright (C) since 2017 Lumeer.io, s.r.o. and/or its affiliates.
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
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class LinkAttributeFilter extends AttributeFilter {

   private final String linkTypeId;

   @JsonCreator
   public LinkAttributeFilter(@JsonProperty("linkTypeId") final String linkTypeId,
         @JsonProperty("attributeId") final String attributeId,
         @JsonProperty("operator") final String operator,
         @JsonProperty("value") final Object value) {
      super(attributeId, operator, value);
      this.linkTypeId = linkTypeId;
   }

   public String getLinkTypeId() {
      return linkTypeId;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof LinkAttributeFilter)) {
         return false;
      }
      if (!super.equals(o)) {
         return false;
      }
      final LinkAttributeFilter that = (LinkAttributeFilter) o;
      return Objects.equals(getAttributeId(), that.getAttributeId()) &&
            Objects.equals(getOperator(), that.getOperator()) &&
            Objects.equals(getValue(), that.getValue()) &&
            Objects.equals(getLinkTypeId(), that.getLinkTypeId());
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), getAttributeId(), getOperator(), getValue(), getLinkTypeId());
   }

   @Override
   public String toString() {
      return "LinkAttributeFilter{" +
            "linkTypeId='" + getLinkTypeId() + '\'' +
            ", attributeId='" + getAttributeId() + '\'' +
            ", operator='" + getOperator() + '\'' +
            ", value=" + getValue() +
            '}';
   }

}
