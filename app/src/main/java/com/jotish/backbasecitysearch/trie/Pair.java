package com.jotish.backbasecitysearch.trie;

/**
 * Created by jotishsuthar on 13/06/17.
 */

public class Pair<T> {

  String key;
  TrieNode<T> trie;

  public Pair(final String key, final TrieNode<T> trie) {
    this.key = key;
    this.trie = trie;
  }

  public String getKey() {
    return key;
  }

  public TrieNode<T> getValue() {
    return trie;
  }
}
