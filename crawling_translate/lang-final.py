import googletrans
from googletrans import Translator
import pandas as pd
from bs4 import BeautifulSoup
from urllib.request import urlopen
import urllib.request
import urllib.parse

translator = Translator()



# list = ['행주대첩비', '행주성당', '남한산성', '흥국사_대방', '궁궐관아_궁궐_팔달문', '궁궐관아_궁궐_화서문(수원화성)',
#        '용문사정지국사탑비', '명성황후생가', '유엔군화장장시설', '오산외삼미동고인돌', '누정_봉황각', '구벨기에 영사관'
#        '역사사건시설_독립문', '근대정치국방_구서대문형무소', '사당_종묘_정전', '성곽시설_흥인지문', '사당_종묘_영녕전', '창경궁팔각칠층석탑'
#        '창덕궁금천교', '성곽시설_창의문', '성곽_한양도성', '제사유적_제사터_사직단', '궁궐관아_사당_종묘', '청계천유적', '탑골공원팔각정'
#        '명동성당', '한국은행', '교통통신시설_구서울역사', '구국회의사당', '구서울특별시청사', '구대법원청사', '구미국문회원', '감은사지서삼층석탑',
#        '불국사다보탑', '삼층석탑', '관천대_첨성대', '청운교및백운고', '법당_석굴암_석굴', '석굴암삼층석탑', '불국사_대웅전', '사찰불국사',
#        '궁궐관아_궁궐_화성행궁', '흥국사_대방',
#        '경주시_전망대_첨성대', '제주시_센터43기념관', '경주시_경주타워']

list = ['행주대첩비', '행주성당']



# kor_dict = {}

for i in list:
    print(i)
    search = i
    encode = urllib.parse.quote_plus(search)

    response = urlopen('https://ko.wikipedia.org/wiki/' + encode)
    soup = BeautifulSoup(response, 'html.parser')

    for anchor in soup.select("div.mw-parser-output > p", limit=1):
        print(anchor.text)

        # # 영어
        # print(translator.translate(anchor, src='ko', dest='en').text)
        # # 일본
        # print(translator.translate(anchor, src='ko', dest='ja').text)
        # # 중국
        # print(translator.translate(anchor, src='ko', dest='zh-cn').text)
    #kor_dict[i] = anchor.text


eng_dict = {}
jpn_dict = {}
chn_dict = {}

