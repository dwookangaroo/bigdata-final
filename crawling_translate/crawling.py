from bs4 import BeautifulSoup
from urllib.request import urlopen
import urllib.request
import urllib.parse

# value = '한글'
# #parse모듈을 사용해 한글("한글")변수를 유니코드로 치환
# encode = urllib.parse.quote_plus(value)
# #한글을 제외한 주소를 url1에 저장
# url = "주소'+encode"

# 검색어 encoding
search='오산외삼미동고인돌'
encode = urllib.parse.quote_plus(search)

response = urlopen('https://ko.wikipedia.org/wiki/'+ encode)
soup = BeautifulSoup(response, 'html.parser')



for anchor in soup.select("div.mw-parser-output > p", limit=1):
    print(anchor.text)

#mw-content-text > div.mw-parser-output > p:nth-child(3)
#mw-content-text > div.mw-parser-output > p:nth-child(2)
#mw-content-text > div.mw-parser-output > p:nth-child(3)