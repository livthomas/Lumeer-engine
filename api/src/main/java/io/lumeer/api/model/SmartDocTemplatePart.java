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
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.concurrent.Immutable;

@Immutable
public class SmartDocTemplatePart {

   private final String type;

   private final String textHtml;
   private final Object textData;

   private final String linkTypeId;
   private final String perspective;
   private final String templateId;

   @JsonCreator
   public SmartDocTemplatePart(
         @JsonProperty("type") final String type,
         @JsonProperty("textHtml") final String textHtml,
         @JsonProperty("textData") final Object textData,
         @JsonProperty("linkTypeId") final String linkTypeId,
         @JsonProperty("perspective") final String perspective,
         @JsonProperty("templateId") final String templateId) {
      this.type = type;
      this.textHtml = textHtml;
      this.textData = textData;
      this.linkTypeId = linkTypeId;
      this.perspective = perspective;
      this.templateId = templateId;
   }

   public String getType() {
      return type;
   }

   public String getTextHtml() {
      return textHtml;
   }

   public Object getTextData() {
      return textData;
   }

   public String getLinkTypeId() {
      return linkTypeId;
   }

   public String getPerspective() {
      return perspective;
   }

   public String getTemplateId() {
      return templateId;
   }

   @Override
   public String toString() {
      return "SmartDocTemplatePart{" +
            "type='" + type + '\'' +
            ", textHtml='" + textHtml + '\'' +
            ", textData=" + textData +
            ", linkTypeId='" + linkTypeId + '\'' +
            ", perspective='" + perspective + '\'' +
            ", templateId='" + templateId + '\'' +
            '}';
   }
}
