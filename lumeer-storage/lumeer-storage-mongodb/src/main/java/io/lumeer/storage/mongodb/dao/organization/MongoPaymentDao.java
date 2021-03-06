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
package io.lumeer.storage.mongodb.dao.organization;

import io.lumeer.api.model.Organization;
import io.lumeer.api.model.Payment;
import io.lumeer.api.model.ResourceType;
import io.lumeer.engine.api.event.CreateOrUpdatePayment;
import io.lumeer.storage.api.dao.PaymentDao;
import io.lumeer.storage.api.exception.ResourceNotFoundException;
import io.lumeer.storage.api.exception.StorageException;
import io.lumeer.storage.mongodb.codecs.PaymentCodec;
import io.lumeer.storage.mongodb.dao.system.SystemScopedDao;
import io.lumeer.storage.mongodb.util.MongoFilters;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@RequestScoped
public class MongoPaymentDao extends SystemScopedDao implements PaymentDao {

   private static final String PREFIX = "payments_o-";

   @Inject
   private Event<CreateOrUpdatePayment> createOrUpdatePaymentEvent;

   @Override
   public Payment createPayment(final Organization organization, final Payment payment) {
      try {
         databaseCollection(organization).insertOne(payment);
         if (createOrUpdatePaymentEvent != null) {
            createOrUpdatePaymentEvent.fire(new CreateOrUpdatePayment(organization, payment));
         }
         return payment;
      } catch (MongoException ex) {
         throw new StorageException("Cannot create payment " + payment, ex);
      }
   }

   @Override
   public List<Payment> getPayments(final Organization organization) {
      return databaseCollection(organization).find().sort(Sorts.descending(Payment.DATE)).into(new ArrayList<>());
   }

   @Override
   public Payment updatePayment(final Organization organization, final String id, final Payment payment) {
      return updatePayment(organization, payment, MongoFilters.idFilter(id));
   }

   @Override
   public Payment updatePayment(final Organization organization, final Payment payment) {
      return updatePayment(organization, payment, paymentIdFilter(payment.getPaymentId()));
   }

   private Payment updatePayment(final Organization organization, final Payment payment, final Bson filter) {
      FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
      try {
         Bson update = new Document("$set", payment).append("$inc", new Document(PaymentCodec.VERSION, 1L));
         final Payment returnedPayment = databaseCollection(organization).findOneAndUpdate(filter, update, options);
         if (returnedPayment == null) {
            throw new StorageException("Payment '" + payment.getId() + "' has not been updated.");
         }
         if (createOrUpdatePaymentEvent != null) {
            createOrUpdatePaymentEvent.fire(new CreateOrUpdatePayment(organization, returnedPayment));
         }
         return returnedPayment;
      } catch (MongoException ex) {
         throw new StorageException("Cannot update payment " + payment, ex);
      }
   }

   @Override
   public Payment getPayment(final Organization organization, final String paymentId) {
      return databaseCollection(organization).find(paymentIdFilter(paymentId)).first();
   }

   @Override
   public Payment getPaymentByDbId(final Organization organization, final String id) {
      return databaseCollection(organization).find(MongoFilters.idFilter(id)).first();
   }

   @Override
   public Payment getLatestPayment(final Organization organization) {
      return databaseCollection(organization).find(paymentStateFilter(Payment.PaymentState.PAID.ordinal()))
                                             .sort(Sorts.descending(PaymentCodec.VALID_UNTIL)).limit(1).first();
   }

   @Override
   public Payment getPaymentAt(final Organization organization, final Date date) {
      return databaseCollection(organization)
            .find(Filters.and(paymentStateFilter(Payment.PaymentState.PAID.ordinal()),
                  paymentValidUntilFilter(date), paymentStartFilter(date)))
            .sort(Sorts.descending(PaymentCodec.VALID_UNTIL)).limit(1).first();
   }

   private Bson paymentIdFilter(final String paymentId) {
      return Filters.eq(Payment.PAYMENT_ID, paymentId);
   }

   private Bson paymentStateFilter(final int stateId) {
      return Filters.eq(Payment.STATE, stateId);
   }

   private Bson paymentValidUntilFilter(final Date date) {
      return Filters.gte(Payment.VALID_UNTIL, date);
   }

   private Bson paymentStartFilter(final Date date) {
      return Filters.lte(Payment.START, date);
   }

   @Override
   public void createPaymentRepository(final Organization organization) {
      database.createCollection(databaseCollectionName(organization));

      MongoCollection<Document> groupCollection = database.getCollection(databaseCollectionName(organization));
      groupCollection.createIndex(Indexes.ascending(PaymentCodec.PAYMENT_ID), new IndexOptions().unique(false));
      groupCollection.createIndex(Indexes.descending(PaymentCodec.DATE), new IndexOptions().unique(true));
      groupCollection.createIndex(Indexes.descending(PaymentCodec.START), new IndexOptions().unique(true));
      groupCollection.createIndex(Indexes.descending(PaymentCodec.VALID_UNTIL), new IndexOptions().unique(true));
   }

   @Override
   public void deletePaymentRepository(final Organization organization) {
      database.getCollection(databaseCollectionName(organization)).drop();
   }

   private MongoCollection<Payment> databaseCollection(final Organization organization) {
      return database.getCollection(databaseCollectionName(organization), Payment.class);
   }

   private String databaseCollectionName(final Organization organization) {
      if (organization == null) {
         throw new ResourceNotFoundException(ResourceType.ORGANIZATION);
      }
      return PREFIX + organization.getId();
   }
}
