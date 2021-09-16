import pandas as pd
import googlemaps
import folium
import pymysql
from haversine import haversine

hotel_seoul = pd.read_csv('../csv/호텔.csv', encoding='cp949')

# del hotel_seoul['메인 키']
# del hotel_seoul['분류1']
# del hotel_seoul['분류2']
# del hotel_seoul['분류4']
# del hotel_seoul['검색어']
# del hotel_seoul['지번 주소']
# del hotel_seoul['도로명 주소']

# hotel_seoul['명칭'][120] = 'IMI호텔'
# hotel_seoul['주소'] = hotel_seoul['행정 시'] + hotel_seoul['행정 구'] + hotel_seoul['행정 동'] + hotel_seoul['명칭']
#
# gmaps_key = "AIzaSyAhweuxR5E_UeX7-TXtr7GqjwtX2MraStY"
# gmaps = googlemaps.Client(key=gmaps_key)
#
# lat_list = []
# lng_list = []
#
# for name in hotel_seoul['주소']:
#     # print(name)
#
#     item = gmaps.geocode(name, language='ko')
#     # print(item[0]['geometry']['location']['lat'])
#     try:
#         lat_list.append(item[0]['geometry']['location']['lat'])
#
#         # print(item[0]['geometry']['location']['lng'])
#         lng_list.append(item[0]['geometry']['location']['lng'])
#
#     except:
#         lat_list.append('error!')
#         lng_list.append('error!')

# hotel_seoul['lat'] = lat_list
# hotel_seoul['lng'] = lng_list
#
# seoul = folium.Map(location=[37.5665, 126.9780], zoom_start=13)
#
# for i in range(len(hotel_seoul)):
#     lat = hotel_seoul['lat'][i]
#     lng = hotel_seoul['lng'][i]
#
#     folium.Marker([lat, lng], popup=hotel_seoul['명칭'][i],
#                   icon=folium.Icon(icon='cloud')).add_to(seoul)
#
# landmark_list =[
#     '경복궁', '명동성당', '이순신 동상', '63빌딩', '탑골공원팔각정', '독립문',
#     '남산타워', '서울롯데타워', '코엑스', '서대문 형무소', '구서울역'
# ]
#
# landmark_lat_list=[]
# landmark_lng_list=[]
# for name in landmark_list:
#     item = gmaps.geocode(name, language='ko')
#     landmark_lat_list.append(item[0]['geometry']['location']['lat'])
#     landmark_lng_list.append(item[0]['geometry']['location']['lng'])
#
# for i in range(len(landmark_list)):
#     lat = landmark_lat_list[i]
#     lng = landmark_lng_list[i]
#
#     folium.Marker([lat, lng], popup=landmark_list[i],
#                   icon=folium.Icon(color='red', icon='cloud')).add_to(seoul)
#
# my_dict={}
#
# for i, name in enumerate(hotel_seoul['명칭']):
#     my_dict[name] = (lat_list[i], lng_list[i])
#
# my_dict2={}
#
# for i, name in enumerate(landmark_list):
#     my_dict2[name] = (landmark_lat_list[i], landmark_lng_list[i])
#
# def merge_two_dicts(x, y):
#     z = x.copy()   # start with keys and values of x
#     z.update(y)    # modifies z with keys and values of y
#     return z
#
# my_dict_final = merge_two_dicts(my_dict, my_dict2)
#
# # for k, v in my_dict2.items():
# #     for i, j in my_dict.items():
# #         print('{}에서 {}까지의 거리'.format(k, i))
# #         print(haversine(v, j, unit='km'))
#
# import itertools
#
#
# # 제일가까운 5개 호텔 찾기
# def find_shortest_hotel(landmark):
#     # dis = []
#     dis = {}
#     dis_5 = []
#
#     for i, j in my_dict.items():
#         # 모든 호텔과의 거리(1)
#         # dis.append(haversine(my_dict2[landmark], j, unit='km'))
#         # 가장가까운 5개 호텔 이름(2)
#         dis[i] = haversine(my_dict2[landmark], j, unit='km')
#         # (1) 짧은거리
#     # dis_5 = sorted(dis)[:5]
#     # (2)
#     A = {k: v for k, v in sorted(dis.items(), key=lambda item: item[1])}
#     hotel = dict(itertools.islice(A.items(), 5))
#
#     return hotel
#
#
# find_shortest_hotel('서울롯데타워')

