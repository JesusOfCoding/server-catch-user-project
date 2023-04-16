insert into NOTICES(title, content, status) values ('공지 1', '내용 1', 'WAIT');
insert into NOTICES(title, content, status) values ('공지 2', '내용 2', 'WAIT');
insert into NOTICES(title, content, status) values ('공지 3', '내용 3', 'WAIT');

insert into FILE_INFO_LIST(type) values ('BANNER');
insert into FILE_INFO_LIST(type) values ('MENU');
insert into FILE_INFO_LIST(type) values ('STORE');

insert into FILES(file_info_id, file_name, file_url, status) values (1, '8.jpg', 'https://news.samsungdisplay.com/wp-content/uploads/2018/08/8.jpg', 'ACTIVE');
insert into FILES(file_info_id, file_name, file_url, status) values (2, 'aa.jpg', 'https://news.dbhasjuhwuha.com/wp-content/uploads/2021/08/aa.jpg', 'ACTIVE');
insert into FILES(file_info_id, file_name, file_url, status) values (3, 'bb8.jpg', 'https://news.ahjsbaghuwssplay.com/wp-content/uploads/2011/08/bb8.jpg', 'ACTIVE');

insert into BANNERS(file_info_id, start_time, end_time, status) values (1, '2021-01-01 00:01', '2023-01-01 00:01', 'ACTIVE');
insert into BANNERS(file_info_id, start_time, end_time, status) values (2, '2022-02-02 00:02', '2023-01-01 00:02', 'ACTIVE');
insert into BANNERS(file_info_id, start_time, end_time, status) values (3, '2023-03-03 00:03', '2023-01-01 00:03', 'ACTIVE');

insert into BANNER_SORT_LIST(banner_id, name, color) values (1, '서울에서', '#228B22');
insert into BANNER_SORT_LIST(banner_id, name, color) values (1, '서울이 아닌 곳에서', '#228B22');
insert into BANNER_SORT_LIST(banner_id, name, color) values (2, '1~50위', '#C0C0C0');

insert into ENTERPRISE_LIST(username, password, role, email, tel, status)
values ('ssar', '1234', 'USER', 'ssar@nate.com', '010-1111-1111', 'ACTIVE');

insert into ENTERPRISE_STORE_INFO_LIST(enterprise_id, name, address, reservation_price, reservation_term, reservation_cancel_day, lat, lon, file_info_id, status)
values (1, '매장이름1', '매장주소1', 1000, '1일', '2021-01-01 00:01', 123.123, 123.123, 1, 'OPEN');

insert into BANNER_ENTERPRISE_LIST(banner_sort_id, store_id) values (1, 1);
insert into BANNER_ENTERPRISE_LIST(banner_sort_id, store_id) values (2, 1);
insert into BANNER_ENTERPRISE_LIST(banner_sort_id, store_id) values (1, 1);

insert into MAGAZINE_LIST(title, content, file_info_id, store_id, status) values ('FUEGO 푸에고', '모든 메뉴를 숯불에 구워내는 우드 그릴 전문 레스토랑.', 1, 1, 'ACTIVE');
insert into MAGAZINE_LIST(title, content, file_info_id, store_id, status) values ('영 셰프의 뉴웨이브 퀴진', '익숙한 재료의 색다른 요리.', 1, 1, 'ACTIVE');
insert into MAGAZINE_LIST(title, content, file_info_id, store_id, status) values ('한식은 도전이다.', '새우장 밥 & 성게장 범벅.', 1, 1, 'ACTIVE');

insert into USERS(username, password, role, email, tel, status) values ('권경렬', '1234', 'USER', 'ssar@nate.com', '010-1234-5678', 'ACTIVE');

insert into RESERVATIONS(user_id, qty, store_id, reservation_term, reservation_cancel_day, push_time, active_time, reservation_price, status)
values (1, 3, 1, '1일', '7일', '2023-03-03 00:03', '2023-03-03 01:01', 10000, 'WAIT');
