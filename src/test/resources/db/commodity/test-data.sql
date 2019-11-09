insert into commodity_type (name, description, VERSION, id) values ('TypeTest', 'test', '1', 1);

insert into commodity_attribute (id, name, type_id,	data_type, attribute_measure) values (2, 'size', 1,	'String', null);
insert into commodity_attribute (id, name, type_id,	data_type, attribute_measure) values (8, 'color', 1, 'String', null);

insert into commodity_attribute_value (id, string, attribute_id, integer, real, text) values (3, 's', 2, null, null, null);
insert into commodity_attribute_value (id, string, attribute_id, integer, real, text) values (9, '#00fc12', 8,  null, null, null);

insert into commodity (id, type_id, name, short_description, overview, date_of_creation, VERSION) values (4, 1,	't-shirt', 'test description',	'Overview t-shirt test',	'2019-08-25 18:46:23.918',	1);


insert into commodity_image (image_order,	commodity_id, uri,	width,	height)
    values(0, 4, 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/5d/Duke_%28Java_mascot%29_waving.svg/800px-Duke_%28Java_mascot%29_waving.svg.png', null, null);
insert into commodity_branch (id, commodity_id, amount,	price, VERSION,	currency)
    values (5, 4, 5, 3500, 1, 'EUR');

insert into commodity_branch_attribute_set (id,	branch_id, attribute_value_id, attribute_id) values (7, 5, 3, 2);








