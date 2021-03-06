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
package io.lumeer.engine.api.batch;

/**
 * Represents a batch operation on a collection.
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public abstract class AbstractCollectionBatch implements Batch {

   /**
    * Name of the collection to run batch on.
    */
   protected String collectionCode;

   /**
    * When false, the original attributes are removed from the documents.
    */
   protected boolean keepOriginal = false;

   @Override
   public String getCollectionCode() {
      return collectionCode;
   }

   public void setCollectionCode(final String collectionCode) {
      this.collectionCode = collectionCode;
   }

   public boolean isKeepOriginal() {
      return keepOriginal;
   }

   public void setKeepOriginal(final boolean keepOriginal) {
      this.keepOriginal = keepOriginal;
   }
}
