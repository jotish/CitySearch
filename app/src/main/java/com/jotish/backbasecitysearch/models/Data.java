package com.jotish.backbasecitysearch.models;

import com.jotish.backbasecitysearch.trie.TrieMap;
import java.util.List;

/**
 * Created by jotishsuthar on 13/06/17.
 */

public class Data {

  public List<City> getCities() {
    return cities;
  }

  public TrieMap<City> getSearchTree() {
    return searchTree;
  }

  public void setCities(final List<City> cities) {
    this.cities = cities;
  }

  public void setSearchTree(
      final TrieMap<City> searchTree) {
    this.searchTree = searchTree;
  }

  private List<City> cities;
  private TrieMap<City> searchTree;

  public Data(final List<City> cities, final TrieMap<City> searchTree) {
    this.cities = cities;
    this.searchTree = searchTree;
  }
}
