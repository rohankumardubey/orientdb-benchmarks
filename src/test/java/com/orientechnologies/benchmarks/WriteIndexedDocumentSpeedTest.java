/*
 *
 *  *  Copyright 2014 Orient Technologies LTD (info(at)orientechnologies.com)
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  *
 *  * For more information: http://www.orientechnologies.com
 *  
 */
package com.orientechnologies.benchmarks;

import com.orientechnologies.benchmarks.base.AbstractDocumentBenchmark;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;

public class WriteIndexedDocumentSpeedTest extends AbstractDocumentBenchmark {
  public WriteIndexedDocumentSpeedTest() {
    super("IndexedDocuments");
  }

  public void test() {
    {
      // USE WAL
      OGlobalConfiguration.USE_WAL.setValue(true);

      final ODatabaseDocumentTx db = createDatabase();

      step("createMultipleClustersIndexedHash8", new Step() {
        @Override
        public void execute(final long items) {
          db.declareIntent(new OIntentMassiveInsert());
          createIndex(db, "MultipleClustersIndexedHash8", OClass.INDEX_TYPE.UNIQUE_HASH_INDEX);
          createMultipleClusters(db, items, "MultipleClustersIndexedHash8", 8, 1, 0, 0);
          db.declareIntent(null);
        }
      });

      step("createMultipleClustersIndexedSBTree8", new Step() {
        @Override
        public void execute(final long items) {
          db.declareIntent(new OIntentMassiveInsert());
          createIndex(db, "MultipleClustersIndexedSBTree8", OClass.INDEX_TYPE.UNIQUE);
          createMultipleClusters(db, items, "MultipleClustersIndexedSBTree8", 8, 1, 0, 0);
          db.declareIntent(null);
        }
      });

      dropDatabase();
    }

    end();

  }

  protected void createIndex(final ODatabaseDocumentTx db, final String iClassName, final OClass.INDEX_TYPE iType) {
    final OClass cls = db.getMetadata().getSchema().createClass(iClassName);
    cls.createProperty("key", OType.LONG).createIndex(iType);
  }

}