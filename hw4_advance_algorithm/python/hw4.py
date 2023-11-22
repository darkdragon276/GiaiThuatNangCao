from __future__ import annotations

import json
import time
import re
import unidecode
from typing import List
class Node:
    # Trie node class
    def __init__(self):
        self.children = [None]*26
        self.leafRefId = ""
        self.content = ""

    def _at(self, ch:str) -> int:
        return ord(ch)-ord('a')
    
    def getChild(self, char:str) -> Node:
        return self.children[self._at(char)]
    
    def setChild(self, char:str, newNode:Node):
        if self._at(char) in range(len(self.children)):
            if not self.children[self._at(char)]:
                self.children[self._at(char)] = newNode
    
    def getChildren(self) -> List[Node]:
        return self.children
    
    def getLeafRefId(self) -> str:
        return self.leafRefId

class Trie:
    def __init__(self):
        self.root = Node()
 
    def getNode(self) -> Node:
        return Node()
 
    def insertReverse(self, word:str, id:str):
        if word == "": return
        pCrawl = self.root
        for i in reversed(range(len(word))):
            pCrawl.setChild(word[i], self.getNode())
            pCrawl = pCrawl.getChild(word[i])
        pCrawl.leafRefId = id
 
    def searchReverse(self, word:str) -> List[str]:
        pCrawl = self.root
        tempWord = ""
        tempChildren = List[Node]
        retListRefId = List[str]
        for i in reversed(range(len(word))):
            if not pCrawl.getChild(word[i]):
            # check to can access word.charAt(i - 1)
                if (i - 1) >= 0:
                    # for case not extend but wrong current character
                    if not pCrawl.getChild(word[i-1]):
                        tempChildren = pCrawl.getChildren()
                        tempWord = word[0:i]
                        break
                    # for case extend a character
                    else:
                        i -= 1
                        pCrawl = pCrawl.getChild(word[i])
                # for case not extend but wrong current character
                else:
                    tempChildren = pCrawl.getChildren()
                    tempWord = word[0:i]
                    break
            # true character, continue searching
            else:
                pCrawl = pCrawl.getChild(word[i])
            
            # return all refId when reach the leaf node. for case redundant trailing character
            if pCrawl.getLeafRefId() != "":
                retListRefId.append(pCrawl.getLeafRefId())
            
        if not tempWord:
            if pCrawl.getLeafRefId() != "":
                retListRefId.append(pCrawl.getLeafRefId())
        else:
            for child in tempChildren:
                pCrawlSubString = child
                for i in reversed(range(len(tempWord))):
                    if not child.getChild(tempWord[i]):
                        break
                    pCrawlSubString = child.getChild(tempWord[i])
                
                if pCrawlSubString.getLeafRefId() != "":
                    retListRefId.append(pCrawlSubString.getLeafRefId())

        return retListRefId

class Utils:
    def __init__(self) -> None:
        pass

    @staticmethod
    def process_data(string: str) -> str:
        return unidecode.unidecode(string).replace(" ", "").lower().replace("-", "")

class Solution:
    def __init__(self):
        self.province_path = 'list_province.txt'
        self.district_path = 'list_district.txt'
        self.ward_path = 'list_ward.txt'
        self.province_trie = Trie()
        self.district_trie = Trie()
        self.ward_trie = Trie()
        self.buildTrie(self.province_path, self.province_trie)
        self.buildTrie(self.district_path, self.district_trie)
        self.buildTrie(self.ward_path, self.ward_trie)

    def buildTrie(self, path:str, trie:Trie):
        with open(path, encoding="utf8") as file:
            content = file.read()
            lists = content.split('\n')
            print(lists)
            for idx, word in enumerate(lists):
                trie.insertReverse(Utils.process_data(word), idx)
                print(word)
        file.close()

    def process(self, s:str):

        return {
            "province": "",
            "district": "",
            "ward": "",
        }
    
solution = Solution()