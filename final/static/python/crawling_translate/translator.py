# import googletrans
# from googletrans import Translator
#
# # 사용가능한 언어
#
# print(googletrans.LANGUAGES)
#
#
# translator = Translator()
#
# # translate
# #print("한국어에서 영어로 : ", translator.translate(text2).text)
#
#
# text = "숭례문 또는 서울 숭례문은 조선의 수도였던 한양의 4대문 중의 하나로 남쪽의 대문이다." \
#        " 흔히 남대문이라고도 부르는데, 이것은 일제강점기 시절에 일본이 붙인 명칭이다."
# # 한글에서 영어로
# print(translator.translate(text).text)
# # 한글에서 일본어
# print(translator.translate(text, src='ko', dest='ja').text)

from googletrans import Translator

translator = Translator()
results =translator.translate('안녕하세요')
print(results.text)