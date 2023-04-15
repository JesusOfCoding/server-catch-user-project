insert into NOTICES(title, content, status) values ('공지 1', '내용 1', 'WAIT');
insert into NOTICES(title, content, status) values ('공지 2', '내용 2', 'WAIT');
insert into NOTICES(title, content, status) values ('공지 3', '내용 3', 'WAIT');

insert into FILE_INFO_LIST(type) values ('BANNER');
insert into FILE_INFO_LIST(type) values ('MENU');
insert into FILE_INFO_LIST(type) values ('STORE');
--
insert into BANNERS(file_info_id, start_time, end_time, status) values (1, '2021-01-01 00:01', '2023-01-01 00:01', 'WAIT');
-- insert into BANNERS(fileInfo, startTime, endTime, status) values (1, '2021-01-01T00:01', '2023-01-01T00:01', 'WAIT');
-- insert into BANNERS(fileInfo, startTime, endTime, status) values (1, '2021-01-01T00:01', '2023-01-01T00:01', 'WAIT');

-- insert into BANNER_ENTERPRISE_LIST(bannerSort, store) values (BannerSort, EnterpriseStoreInfo);
-- insert into BANNERS(fileInfo, startTime, endTime, status) values ('Image', '2021-01-01T00:00', '2022-01-01T12:00', 'WAIT');