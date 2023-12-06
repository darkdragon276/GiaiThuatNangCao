from __future__ import annotations

import json
import time
import re
import unidecode
from typing import List

exceptrionData = ["quận","h", "f", "thànhphố", "hyện", "x", "hzuyện", "tx", "tp", "xã", "tỉnhv", "huyen", "tphố", "thịxã", "tỉnwh", "tphw", "tỉnh", "huyện", "t0p", "thànhmphố", "huyn", "tphố", "tt", "t"]
province_dict = dict()
district_dict = dict()
ward_dict = dict()

class Node:
    # Trie node class
    def __init__(self):
        self.children = dict()
        self.leafRefId = ""
        self.content = ""

    def getChild(self, char:str) -> Node:
        return self.children.get(char)

    def setChild(self, char:str, newNode:Node):
        if char not in self.children:
            self.children[char] = newNode

    def getChildren(self) -> List[Node]:
        return self.children

    def getLeafRefId(self) -> str:
        return self.leafRefId

class Trie:
    def __init__(self):
        self.root = Node()

    def insertReverse(self, word:str, id:str):
        if word == "": return
        pCrawl = self.root
        for i in reversed(range(len(word))):
            pCrawl.setChild(word[i], Node())
            pCrawl = pCrawl.getChild(word[i])
        pCrawl.leafRefId = id

    def searchReverse(self, word:str) -> List[str]:
        pCrawl = self.root
        tempWord = ""
        tempChildren = dict()
        retListRefId = []
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

        if tempWord == "":
            if pCrawl.getLeafRefId() != "":
                retListRefId.append(pCrawl.getLeafRefId())
        else:
            for key, child in tempChildren.items():
                pCrawlSubString = child
                for i in reversed(range(len(tempWord))):
                    if not child.getChild(tempWord[i]):
                        break
                    pCrawlSubString = child.getChild(tempWord[i])

                if pCrawlSubString.getLeafRefId() != "":
                    retListRefId.append(pCrawlSubString.getLeafRefId())

        return retListRefId

    def insertException(self, word:str, id:str):
        if word == "": return
        pCrawl = self.root
        for i in range(len(word)):
            pCrawl.setChild(word[i], Node())
            pCrawl = pCrawl.getChild(word[i])
        pCrawl.leafRefId = id

    def searchException(self, word:str) -> int:
        pCrawl = self.root
        index = 0
        for i in range(len(word)):
            if not pCrawl.getChild(word[i]):
                if pCrawl.getLeafRefId() == "common_nouns":
                    index = i
                return index
            pCrawl = pCrawl.getChild(word[i])

        if pCrawl.getLeafRefId() == "common_nouns":
            index = len(word) - 1

        return index

    def searchNormalException(self, word:str) -> bool:
        pCrawl = self.root
        if len(word) > 1:
            for i in range(len(word)):
                if not pCrawl.getChild(word[i]):
                    return False
                pCrawl = pCrawl.getChild(word[i])
        else:
            if not pCrawl.getChild(word[0]):
                return False
            else:
                pCrawl = pCrawl.getChild(word[0])
        return pCrawl.getLeafRefId() == "common_nouns"

exceptionTrie = Trie()

class Utils:
    def __init__(self) -> None:
        pass

    @staticmethod
    def buildTrie(path:str, trie:Trie, accent_dict: dict):
        with open(path, encoding="utf8") as file:
            content = file.read()
            lists = content.split('\n')
            for idx, word in enumerate(lists):
                # remove [-\s], lower, remove accent
                processedString = unidecode.unidecode(word).replace(" ", "").lower().replace("-", "")
                trie.insertReverse(processedString, str(idx))
                accent_dict[str(idx)] = word
        file.close()

    @staticmethod
    def buidExceptionTrie(arrays: List[str], trie:Trie):
        for exceptStr in arrays:
            trie.insertException(exceptStr, "common_nouns")

    @staticmethod
    def preProcess(string: str) -> List[str]:
        splitArray = re.split(r'[-,\s]', string.lower())
        nonEmptyArray = [s for s in splitArray if s and s.strip()]
        unDashArray = list(map(lambda s: re.sub(r'[.-]', "", s), nonEmptyArray))
        removedTwinException = Utils.filterException(unDashArray)
        removedSingleException = [s for s in removedTwinException if not exceptionTrie.searchNormalException(s)]
        lowerUnAccents = list(map(lambda s: unidecode.unidecode(s), removedSingleException))
        return lowerUnAccents

    @staticmethod
    def filterException(listWord: List[str]) -> List[str]:
        removedPrefixList = []
        i = 0
        while i < len(listWord):
            twinWord = (listWord[i] + listWord[i+1]) if i < (len(listWord) - 1) else listWord[i]
            resultSearchValue = exceptionTrie.searchException(twinWord)
            if resultSearchValue <= 3:
                removedPrefixList.append(listWord[i])
            elif resultSearchValue < len(twinWord):
                removedPrefixList.append(twinWord[resultSearchValue:])
                i += 1
            else:
                i+= 1
            i += 1
        return removedPrefixList

class Solution:
    def __init__(self):
        self.province_path = 'list_province.txt'
        self.district_path = 'list_district.txt'
        self.ward_path = 'list_ward.txt'
        self.province_trie = Trie()
        self.district_trie = Trie()
        self.ward_trie = Trie()
        Utils.buildTrie(self.province_path, self.province_trie, province_dict)
        Utils.buildTrie(self.district_path, self.district_trie, district_dict)
        Utils.buildTrie(self.ward_path, self.ward_trie, ward_dict)
        Utils.buidExceptionTrie(exceptrionData, exceptionTrie)

    def process(self, text:str) -> dict():
        province = ""
        district = ""
        ward = ""

        inputString = ""
        listAddress = Utils.preProcess(text)
        i = len(listAddress) - 1
        while i >= 0:
            inputString = listAddress[i] + inputString
            refIds = self.province_trie.searchReverse(inputString)
            if len(refIds) == 0 and len(listAddress) - i <= 4:
                if i == 0:
                    i = len(listAddress) - 1
                    inputString = ""
                    break
                i = i - 1
                continue
            elif len(refIds) == 0 and len(listAddress) - i > 4:
                i = len(listAddress) - 1
                inputString = ""
                break
            else:
                province = province_dict.get(refIds[0])
                inputString = ""
                i = i - 1
                break
            # continue loop
            i = i - 1
        iStore = i
        inputString = ""

        while i >= 0:
            inputString = listAddress[i] + inputString
            refIds = self.district_trie.searchReverse(inputString)
            if len(refIds) == 0 and iStore - i <= 5:
                if i == 0:
                    i = iStore
                    inputString = ""
                    break
                i = i - 1
                continue
            elif len(refIds) == 0 and iStore - i > 5:
                i = iStore
                inputString = ""
                break
            else:
                for idx in refIds:
                  if len(district) < len(district_dict.get(idx)):
                      district = district_dict.get(idx)
                district = district_dict.get(refIds[0])
                # listAddress[i] = inputString[:len(inputString) - len(district) + 1]
                i = i - 1
                inputString = ""
                break
            # continue loop
            i = i - 1

        iStore = i
        inputString = ""

        while i >= 0:
            inputString = listAddress[i] + inputString
            refIds = self.ward_trie.searchReverse(inputString)
            if len(refIds) == 0 and iStore - i <= 4:
                i = i - 1
                continue
            elif len(refIds) == 0 and iStore - i > 4:
                break
            else:
                for idx in refIds:
                    if len(ward) < len(ward_dict.get(idx)):
                        ward = ward_dict.get(idx)
            # continue loop
            i = i - 1
        return {
            "district": district,
            "ward": ward,
            "province": province,
        }

solution = Solution()