import googletrans
from googletrans import Translator
import pandas as pd
from bs4 import BeautifulSoup
from urllib.request import urlopen
import urllib.request
import urllib.parse

translator = Translator()





list = [
        '경복궁', '명동성당', '이순신_동상', '63빌딩', '탑골공원팔각정', '독립문',
        '남산타워', '롯데타워', '코엑스', '서대문_형무소', '구서울역'
        ]



# kor_dict = {}

for i in list:
    print(i)
    search = i
    encode = urllib.parse.quote_plus(search)

    response = urlopen('https://ko.wikipedia.org/wiki/' + encode)
    soup = BeautifulSoup(response, 'html.parser')

    for anchor in soup.select("div.mw-parser-output > p", limit=1):
        # 한글
        print(anchor.text)
        # 영어
        print(translator.translate(anchor.text, src='ko', dest='en').text)
        # 일본
        print(translator.translate(anchor.text, src='ko', dest='ja').text)
        # 중국
        print(translator.translate(anchor.text, src='ko', dest='zh-cn').text)
    #kor_dict[i] = anchor.text


eng_dict = {}
jpn_dict = {}
chn_dict = {}

