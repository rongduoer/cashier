-- 必须指定字符集是 utf8mb4，否则中文就错了（因为默认字符集是 latin 字符集，不支持中文）
-- 如果没有加字符集，删掉库，重新建库建表（如果非要改，需要改库、表、每个字段都得改）
create database cashier charset utf8mb4;

-- 创建用户表（主键、用户名（唯一）、密码）
create table cashier.users (
    user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password char(60) not null
) comment '用户表，表示货管和收银两种角色，不区分';

-- 创建商品表（主键、关系字段、名称、介绍、库存、单位、价格、折扣）
create table cashier.products (
    product_id int primary key auto_increment,
    user_id int not null comment '这是用户上架商品的一对多的关系字段',
    name varchar(100) not null comment '名称',
    introduce varchar(200) not null comment '介绍',
    stock int not null comment '库存数量',
    unit varchar(10) not null comment '显示单位',
    price int not null comment '价格，单位是分，也就是 100 表示 1 块钱',
    discount int not null comment '折扣，取值范围应该是 (0, 100]'
) comment '商品表';

-- 创建订单表（主键、关系字段、订单编号（uuid）、下单时间、完成时间、状态）
create table cashier.orders (
    order_id int primary key auto_increment,
    user_id int not null comment '这个是用户创建订单的一对多的关系字段',
    uuid char(32) not null unique comment '对外显示的不连续的订单编号，不会重复',
    created_at datetime not null comment '订单的下单时间',
    finished_at datetime null default null comment '订单的完成时间',
    payable int not null comment '本次订单的应付金额，单位是分，提升显示方便性做的冗余字段',
    actual int not null comment '本次订单的实付金额，单位是分，提升显示方便性做的冗余字段',
    status int not null comment '进行中(1) | 已完成(2)'
) comment '订单的总体信息';

-- 创建订单和商品的关系表 —— 订单项表（主键、两个关系字段、为了防止商品下架后信息丢失，将商品信息备份一份）
create table cashier.order_items (
    id int primary key auto_increment,
    order_id int not null comment '属于哪个订单',
    product_id int not null comment '订单中的哪个商品',
    -- 商品的冗余备份
    product_name varchar(100) not null,
    product_introduce varchar(200) not null,
    product_number int not null comment '本次订单购买这个商品多少份',
    product_unit varchar(10) not null,
    product_price int not null,
    product_discount int not null
) comment '订单项，记录本次订单的每一份商品信息';