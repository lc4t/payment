CREATE DATABASE PaymentSysDb DEFAULT CHARACTER SET utf8;

USE PaymentSysDb;

CREATE TABLE create_order_id(
    id                  int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_time         varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE orders(
    order_id            varchar(20) NOT NULL PRIMARY KEY,
    imei                varchar(40),
    u_id                varchar(20),
    app_id              varchar(80),
    channel             varchar(20),
    device_type         varchar(20),
    device_id           varchar(20),
    gversion            varchar(20),
    osversion           varchar(20),
    screen_size         varchar(20),
    sign                varchar(40),
    item_id             varchar(40),
    item_num            int,
    amount              float,
    item_price          varchar(20),
    ex_info             text,
    k_status            int,
    c_status            int,
    e_status            int,
    iscallback          int,
    create_time         varchar(20),
    update_time         varchar(20),
    complete_time       varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE callback(
    order_id            varchar(20) NOT NULL PRIMARY KEY,
    callback_url        varchar(255),
    callback_status     int,
    server_status       int,
    callback_time       varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE DATABASE PaymentLogDb DEFAULT CHARACTER SET utf8;
USE PaymentLogDb;
DROP TABLE IF EXISTS game_logs;
CREATE TABLE game_logs (
  id int(10) unsigned NOT NULL auto_increment,
  imei varchar(40) default NULL,
  u_id varchar(20) default NULL,
  app_id varchar(80) default NULL,
  begin_time varchar(20) default NULL,
  charge double default NULL,
  unit varchar(10) default NULL,
  end_time varchar(20) default NULL,
  screen varchar(20) default NULL,
  model varchar(20) default NULL,
  ip varchar(20) default NULL,
  platform varchar(20) default NULL,
  osversion varchar(20) default NULL,
  gversion varchar(20) default NULL,
  channel varchar(20) default NULL,
  create_time varchar(20) default NULL,
  action varchar(20) default NULL,
  item_id varchar(20) default NULL,
  item_info varchar(20) default NULL,
  item_num int(11) default NULL,
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS logs;
CREATE TABLE logs (
	id INT NOT NULL AUTO_INCREMENT,
	user_id INT NOT NULL,
	item_id VARCHAR(50),
	original_transaction_id VARCHAR(50),
	bvrs VARCHAR(50),
	product_id VARCHAR(50),
	purchase_date VARCHAR(50),
	quantity VARCHAR(50),
	bid VARCHAR(50),
	original_purchase_date VARCHAR(50),
	transaction_id VARCHAR(50),
	status INT,
	original_receipt TEXT,
	receipt_id INT,
	price INT,
	log_time VARCHAR(20),
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS receipts;
CREATE TABLE receipts (
	id INT NOT NULL AUTO_INCREMENT,
	user_id INT NOT NULL,
	original_receipt TEXT, 
	log_time VARCHAR(20),
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
