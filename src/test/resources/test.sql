select c.*, count(cb.id)  from commodity c left outer join commodity_branch cb on (c.id = cb.commodity_id) left outer join commodity_image ci on (c.id = ci.commodity_id)  group by c.id ;
select * from commodity;
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