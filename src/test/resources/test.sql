select c.*, count(cb.id)  from commodity c left outer join commodity_branch cb on (c.id = cb.commodity_id) left outer join commodity_image ci on (c.id = ci.commodity_id)  group by c.id ;
select * from commodity customerOrder by id desc;
select * from commodity_branch;
select * from commodity_attribute;
select * from commodity_attribute_value;
UPDATE commodity_attribute set attribute_measure = 'kg' where id=105;
UPDATE commodity_attribute_value set value='38ff38' where id=9;
select * from commodity_branch where commodity_id=172;
select * from commodity_branch_attribute_set  join commodity_attribute on (commodity_attribute.id=commodity_branch_attribute_set.attribute_id) where (commodity_branch_attribute_set.branch_id=177);
select * from shopping_cart;
select * from commodity_branch join commodity cm on(cm.id=commodity_branch.commodity_id) where commodity_branch.id=163;
select * from shopping_cart_set;

select * from commodity_branch_attribute_set cbas join commodity_branch cb on (cbas.branch_id = cb.id) where commodity_id=158 and (cbas.attribute_value_id=80 or cbas.attribute_value_id=118);

select * from commodity_branch_attribute_set cbas


select commodity0_.id as id1_0_0_, commodity0_.date_of_creation as date_of_2_0_0_, commodity0_.name as name3_0_0_, commodity0_.overview as overview4_0_0_, commodity0_.short_description as short_de5_0_0_, commodity0_.type_id as type_id7_0_0_, commodity0_.version as version6_0_0_, images1_.commodity_id as commodit2_5_1_, images1_.id as id1_5_1_, images1_.id as id1_5_2_, images1_.commodity_id as commodit2_5_2_, images1_.height as height3_5_2_, images1_.uri as uri4_5_2_, images1_.width as width5_5_2_, commodityt2_.id as id1_6_3_, commodityt2_.description as descript2_6_3_, commodityt2_.name as name3_6_3_, commodityt2_.version as version4_6_3_ from commodity commodity0_ left outer join commodity_image images1_ on commodity0_.id=images1_.commodity_id inner join commodity_type commodityt2_ on commodity0_.type_id=commodityt2_.id where commodity0_.id=333