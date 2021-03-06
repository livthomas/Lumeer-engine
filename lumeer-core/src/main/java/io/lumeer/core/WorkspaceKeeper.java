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
package io.lumeer.core;

import io.lumeer.api.SelectedWorkspace;
import io.lumeer.api.model.Organization;
import io.lumeer.api.model.Project;
import io.lumeer.api.model.ServiceLimits;
import io.lumeer.core.cache.WorkspaceCache;

import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class WorkspaceKeeper implements SelectedWorkspace {

   private String organizationId;
   private String projectId;

   @Inject
   private WorkspaceCache workspaceCache;

   @Override
   public Optional<Organization> getOrganization() {
      if (organizationId == null) {
         return Optional.empty();
      }
      return Optional.of(workspaceCache.getOrganization(organizationId));
   }

   @Override
   public Optional<Project> getProject() {
      if (projectId == null) {
         return Optional.empty();
      }
      return Optional.of(workspaceCache.getProject(projectId));
   }

   public void setOrganization(String organizationId) {
      this.organizationId = organizationId;
   }

   public void setProject(String projectId) {
      this.projectId = projectId;
   }

   public void setWorkspace(String organizationId, String projectId) {
      setOrganization(organizationId);
      setProject(projectId);
   }

   public void setServiceLimits(final Organization organization, final ServiceLimits serviceLimits) {
      if (organization != null) {
         workspaceCache.setServiceLimits(organization.getId(), serviceLimits);
      }
   }

   public ServiceLimits getServiceLimits(final Organization organization) {
      if (organization != null) {
         return workspaceCache.getServiceLimits(organization.getId());
      }

      return null;
   }

   public void clearServiceLimits(final Organization organization) {
      if (organization != null) {
         workspaceCache.removeServiceLimits(organization.getId());
      }
   }
}
