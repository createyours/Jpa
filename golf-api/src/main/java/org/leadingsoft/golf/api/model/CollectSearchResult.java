package org.leadingsoft.golf.api.model;

import java.util.List;
import java.util.Map;

public class CollectSearchResult {
  private boolean hasPrePage;

  private boolean hasNextPage;

  private String searchPlayDate;

  private List<Map<String, Object>> searchResultList;

  public boolean isHasPrePage() {
    return hasPrePage;
  }

  public void setHasPrePage(boolean hasPrePage) {
    this.hasPrePage = hasPrePage;
  }

  public boolean isHasNextPage() {
    return hasNextPage;
  }

  public void setHasNextPage(boolean hasNextPage) {
    this.hasNextPage = hasNextPage;
  }

  public String getSearchPlayDate() {
    return searchPlayDate;
  }

  public void setSearchPlayDate(String searchPlayDate) {
    this.searchPlayDate = searchPlayDate;
  }

  public List<Map<String, Object>> getSearchResultList() {
    return searchResultList;
  }

  public void setSearchResultList(List<Map<String, Object>> searchResultList) {
    this.searchResultList = searchResultList;
  }
}
