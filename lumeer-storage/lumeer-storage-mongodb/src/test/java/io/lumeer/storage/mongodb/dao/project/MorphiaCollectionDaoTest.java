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
package io.lumeer.storage.mongodb.dao.project;

import static org.assertj.core.api.Assertions.assertThat;

import io.lumeer.api.model.Attribute;
import io.lumeer.api.model.Collection;
import io.lumeer.api.model.Permission;
import io.lumeer.api.model.Project;
import io.lumeer.api.model.Role;
import io.lumeer.api.model.View;
import io.lumeer.storage.api.query.SearchQuery;
import io.lumeer.storage.mongodb.MongoDbTestBase;
import io.lumeer.storage.mongodb.model.MorphiaCollection;
import io.lumeer.storage.mongodb.model.embedded.MongoAttribute;
import io.lumeer.storage.mongodb.model.embedded.MongoPermission;
import io.lumeer.storage.mongodb.model.embedded.MongoPermissions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MorphiaCollectionDaoTest extends MongoDbTestBase {

   private static final String PROJECT_ID = "596e3b86d412bc5a3caaa22a";

   private static final String USER = "testUser";
   private static final String GROUP = "testGroup";

   private static final String CODE = "TCOLL";
   private static final String NAME = "Test collection";
   private static final String COLOR = "#0000ff";
   private static final String ICON = "fa-eye";
   private static final List<Attribute> ATTRIBUTES = new ArrayList<>();
   private static final Integer DOCUMENTS_COUNT = 0;
   private static final LocalDateTime LAST_TIME_USED = LocalDateTime.now();

   private static final MongoPermissions PERMISSIONS = new MongoPermissions();
   private static final MongoPermission USER_PERMISSION;
   private static final MongoPermission GROUP_PERMISSION;

   private static final String USER2 = "testUser2";
   private static final String GROUP2 = "testGroup2";

   private static final String CODE2 = "TCOLL2";
   private static final String CODE3 = "TCOLL3";

   static {
      // TODO add attributes

      USER_PERMISSION = new MongoPermission(USER, View.ROLES.stream().map(Role::toString).collect(Collectors.toSet()));
      PERMISSIONS.updateUserPermissions(USER_PERMISSION);

      GROUP_PERMISSION = new MongoPermission(GROUP, Collections.singleton(Role.READ.toString()));
      PERMISSIONS.updateGroupPermissions(GROUP_PERMISSION);
   }

   private MorphiaCollectionDao collectionDao;

   @Before
   public void initCollectionDao() {
      Project project = Mockito.mock(Project.class);
      Mockito.when(project.getId()).thenReturn(PROJECT_ID);

      collectionDao = new MorphiaCollectionDao();
      collectionDao.setDatabase(database);
      collectionDao.setDatastore(datastore);

      collectionDao.setProject(project);
      collectionDao.createCollectionsRepository(project);
   }

   private MorphiaCollection prepareCollection() {
      MorphiaCollection collection = new MorphiaCollection();
      collection.setCode(CODE);
      collection.setName(NAME);
      collection.setColor(COLOR);
      collection.setIcon(ICON);
      collection.setPermissions(new MongoPermissions(PERMISSIONS));
      collection.setAttributes(ATTRIBUTES);
      collection.setDocumentsCount(DOCUMENTS_COUNT);
      collection.setLastTimeUsed(LAST_TIME_USED);
      return collection;
   }

   @Test
   public void testGetCollections() {
      MorphiaCollection collection = prepareCollection();
      datastore.save(collectionDao.databaseCollection(), collection);

      MorphiaCollection collection2 = prepareCollection();
      collection2.setCode(CODE2);
      datastore.save(collectionDao.databaseCollection(), collection2);

      SearchQuery query = SearchQuery.createBuilder(USER).build();
      List<Collection> views = collectionDao.getCollections(query);
      assertThat(views).extracting(Collection::getCode).containsOnly(CODE, CODE2);
   }

   @Test
   public void testGetCollectionsNoReadRole() {
      MorphiaCollection collection = prepareCollection();
      Permission userPermission = new MongoPermission(USER2, Collections.singleton(Role.CLONE.toString()));
      collection.getPermissions().updateUserPermissions(userPermission);
      datastore.save(collectionDao.databaseCollection(), collection);

      MorphiaCollection collection2 = prepareCollection();
      collection2.setCode(CODE2);
      Permission groupPermission = new MongoPermission(GROUP2, Collections.singleton(Role.SHARE.toString()));
      collection2.getPermissions().updateGroupPermissions(groupPermission);
      datastore.save(collectionDao.databaseCollection(), collection2);

      SearchQuery query = SearchQuery.createBuilder(USER2).groups(Collections.singleton(GROUP2)).build();
      List<Collection> collections = collectionDao.getCollections(query);
      assertThat(collections).isEmpty();
   }

   @Test
   public void testGetCollectionsGroupsRole() {
      MorphiaCollection collection = prepareCollection();
      datastore.save(collectionDao.databaseCollection(), collection);

      MorphiaCollection collection2 = prepareCollection();
      collection2.setCode(CODE2);
      datastore.save(collectionDao.databaseCollection(), collection2);

      SearchQuery query = SearchQuery.createBuilder(USER2).groups(Collections.singleton(GROUP)).build();
      List<Collection> collections = collectionDao.getCollections(query);
      assertThat(collections).extracting(Collection::getCode).containsOnly(CODE, CODE2);
   }

   @Test
   public void testGetCollectionsByCollectionCodes() {
      MorphiaCollection collection = prepareCollection();
      collection.getPermissions().removeUserPermission(USER);
      Permission userPermission = new MongoPermission(USER2, Collections.singleton(Role.READ.toString()));
      collection.getPermissions().updateUserPermissions(userPermission);
      datastore.save(collectionDao.databaseCollection(), collection);

      MorphiaCollection collection2 = prepareCollection();
      collection2.setCode(CODE2);
      datastore.save(collectionDao.databaseCollection(), collection2);

      MorphiaCollection collection3 = prepareCollection();
      collection3.setCode(CODE3);
      datastore.save(collectionDao.databaseCollection(), collection3);

      SearchQuery query = SearchQuery.createBuilder(USER)
                                     .collectionCodes(new HashSet<>(Arrays.asList(CODE, CODE3)))
                                     .build();
      List<Collection> views = collectionDao.getCollections(query);
      assertThat(views).extracting(Collection::getCode).containsOnly(CODE3);
   }

}
