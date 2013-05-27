/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.cf.taste.impl.recommender;

import com.google.common.collect.Lists;
import org.apache.mahout.cf.taste.impl.TasteTestCase;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.CandidateItemsStrategy;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.MostSimilarItemsCandidateItemsStrategy;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/** <p>Tests {@link GenericItemBasedRecommender}.</p> */
public final class RepeatableGenericItemBasedRecommenderTest extends TasteTestCase {

  @Test
  public void testNonRepeatingRecommender() throws Exception {
    // User 1 already has two preferences for items 0 and 1.
    // Top recommendation is only other item 2.
    Recommender repeatingRecommender = buildRecommender(false);
    List<RecommendedItem> recommended = repeatingRecommender.recommend(1, 3);
    assertNotNull(recommended);
    assertEquals(1, recommended.size());
    RecommendedItem firstRecommended = recommended.get(0);
    assertEquals(2, firstRecommended.getItemID());
  }

  @Test
  public void testRepeatingRecommender() throws Exception {
    // User 1 already has two repeatable preferences for items 0 and 1.
    // Recommendation contains all three items.
    Recommender repeatingRecommender = buildRecommender(true);
    List<RecommendedItem> recommended = repeatingRecommender.recommend(1, 3);
    assertNotNull(recommended);
    assertEquals(3, recommended.size());
    RecommendedItem firstRecommended = recommended.get(0);
    assertEquals(1, firstRecommended.getItemID());
  }

  private static ItemBasedRecommender buildRecommender(boolean repeatable) {
    DataModel dataModel = getDataModel();
    Collection<GenericItemSimilarity.ItemItemSimilarity> similarities = Lists.newArrayList();
    similarities.add(new GenericItemSimilarity.ItemItemSimilarity(0, 1, 1.0));
    similarities.add(new GenericItemSimilarity.ItemItemSimilarity(0, 2, 0.5));
    similarities.add(new GenericItemSimilarity.ItemItemSimilarity(1, 2, 0.0));
    ItemSimilarity similarity = new GenericItemSimilarity(similarities);
    CandidateItemsStrategy candidateItemsStrategy = AbstractRecommender.getDefaultCandidateItemsStrategy();
    if (repeatable) {
      candidateItemsStrategy = new AddRepeatablePreferencesCandidateItemsStrategy(candidateItemsStrategy);
    }
    return new GenericItemBasedRecommender(dataModel, similarity,
        candidateItemsStrategy,
        GenericItemBasedRecommender
            .getDefaultMostSimilarItemsCandidateItemsStrategy());
  }

}
