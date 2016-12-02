/*
 * -----------------------------------------------------------------------\
 * Lumeer
 *  
 * Copyright (C) 2016 - 2017 the original author or authors.
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
package io.lumeer.engine.api.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a database query.
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class Query implements Serializable {

   private static final long serialVersionUID = -9119920083922834392L;

   private Set<String> collections = new HashSet<>();

   private DataDocument filters = new DataDocument();

   private DataDocument projections = new DataDocument();

   private DataDocument sorting = new DataDocument();

   private Integer limit = null;

   private Integer skip = null;

   public Query() {
   }

   public Query(final DataDocument filters) {
      this.filters = filters;
   }

   public Query(final Set<String> collections, final DataDocument filters) {
      this.collections = collections;
      this.filters = filters;
   }

   public Query(final DataDocument filters, final DataDocument projections, final DataDocument sorting) {
      this.filters = filters;
      this.projections = projections;
      this.sorting = sorting;
   }

   public Query(final Set<String> collections, final DataDocument filters, final DataDocument projections, final DataDocument sorting) {
      this.collections = collections;
      this.filters = filters;
      this.projections = projections;
      this.sorting = sorting;
   }

   public Query(final Set<String> collections, final DataDocument filters, final DataDocument projections, final DataDocument sorting, final Integer limit, final Integer skip) {
      this.collections = collections;
      this.filters = filters;
      this.projections = projections;
      this.sorting = sorting;
      this.limit = limit;
      this.skip = skip;
   }

   public Set<String> getCollections() {
      return collections;
   }

   public void setCollections(final Set<String> collections) {
      this.collections = collections;
   }

   public DataDocument getFilters() {
      return filters;
   }

   public void setFilters(final DataDocument filters) {
      this.filters = filters;
   }

   public DataDocument getProjections() {
      return projections;
   }

   public void setProjections(final DataDocument projections) {
      this.projections = projections;
   }

   public DataDocument getSorting() {
      return sorting;
   }

   public void setSorting(final DataDocument sorting) {
      this.sorting = sorting;
   }

   public Integer getLimit() {
      return limit;
   }

   public void setLimit(final Integer limit) {
      this.limit = limit;
   }

   public Integer getSkip() {
      return skip;
   }

   public void setSkip(final Integer skip) {
      this.skip = skip;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      final Query query = (Query) o;

      if (collections != null ? !collections.equals(query.collections) : query.collections != null) {
         return false;
      }
      if (filters != null ? !filters.equals(query.filters) : query.filters != null) {
         return false;
      }
      if (projections != null ? !projections.equals(query.projections) : query.projections != null) {
         return false;
      }
      if (sorting != null ? !sorting.equals(query.sorting) : query.sorting != null) {
         return false;
      }
      if (limit != null ? !limit.equals(query.limit) : query.limit != null) {
         return false;
      }
      return skip != null ? skip.equals(query.skip) : query.skip == null;

   }

   @Override
   public int hashCode() {
      int result = collections != null ? collections.hashCode() : 0;
      result = 31 * result + (filters != null ? filters.hashCode() : 0);
      result = 31 * result + (projections != null ? projections.hashCode() : 0);
      result = 31 * result + (sorting != null ? sorting.hashCode() : 0);
      result = 31 * result + (limit != null ? limit.hashCode() : 0);
      result = 31 * result + (skip != null ? skip.hashCode() : 0);
      return result;
   }

   @Override
   public String toString() {
      return "Query{" +
            "collections=" + collections +
            ", filters=" + filters +
            ", projections=" + projections +
            ", sorting=" + sorting +
            ", limit=" + limit +
            ", skip=" + skip +
            '}';
   }
}