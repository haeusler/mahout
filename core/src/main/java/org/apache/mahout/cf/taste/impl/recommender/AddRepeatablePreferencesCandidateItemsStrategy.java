package org.apache.mahout.cf.taste.impl.recommender;

import java.util.Collection;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.CandidateItemsStrategy;

public class AddRepeatablePreferencesCandidateItemsStrategy implements
    CandidateItemsStrategy {

  private final CandidateItemsStrategy candidateItemsStrategy;
  
  public AddRepeatablePreferencesCandidateItemsStrategy(CandidateItemsStrategy candidateItemsStrategy) {
    this.candidateItemsStrategy = candidateItemsStrategy;
  }
  
  @Override
  public void refresh(Collection<Refreshable> alreadyRefreshed) {
    candidateItemsStrategy.refresh(alreadyRefreshed);
  }

  @Override
  public FastIDSet getCandidateItems(long userID,
      PreferenceArray preferencesFromUser, DataModel dataModel)
      throws TasteException {
    FastIDSet candidates = candidateItemsStrategy.getCandidateItems(userID, preferencesFromUser, dataModel);
    for (Preference preference : preferencesFromUser) {
      if (preference.isRepeatable()) {
        candidates.add(preference.getItemID());
      }
    }
    return candidates;
  }

}
