SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */
DROP TABLE IF EXISTS project_info;


/* 项目表 */
CREATE TABLE project_info
(
	id varchar(64) NOT NULL COMMENT '编号',
	office_id varchar(64) COMMENT '归属部门',
	area_id varchar(64) COMMENT '归属区域',
	project_name varchar(200) COMMENT '项目名称',
	project_grade char(2) COMMENT '项目级别',
	primary_person varchar(64) COMMENT '项目负责人',
	team_members varchar(2000) COMMENT '项目小组成员',
	team_member_names varchar(2000) COMMENT '项目小组成员',
  industry_domain char(2) COMMENT '行业领域',
  main_business VARCHAR(255) COMMENT '主营业务',
	content text COMMENT '项目介绍',
  filepath varchar(2000) COMMENT '附件路径',
  annual_income VARCHAR(100) COMMENT '年收入',
  annual_net_profit VARCHAR(100) COMMENT '年净利润',
  project_progress char(2) COMMENT '项目进度',
	start_date datetime COMMENT '项目的开始时间',
	recommended_man VARCHAR(50) COMMENT '项目推荐人',
	recommended_date datetime COMMENT '项目的推荐时间',
	project_type char(2) COMMENT '项目类型',
  intended_money DECIMAL(20,2) DEFAULT NULL COMMENT '拟投金额',
  project_status char(1) DEFAULT '0' NOT NULL COMMENT '项目状态:0,推介人编辑;1,负责人管理;2暂停;3完成',

	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '项目表';

/* Create Indexes */
CREATE INDEX project_info_del_flag ON project_info (del_flag ASC);
CREATE INDEX project_info_create_by ON project_info (create_by ASC);
CREATE INDEX project_info_project_progress ON project_info (project_progress ASC);
CREATE INDEX project_info_primary_person ON project_info (primary_person ASC);

/* Drop Tables */
DROP TABLE IF EXISTS project_info_progress;
/* 项目进度变更表 */
CREATE TABLE project_info_progress
(
	id varchar(64) NOT NULL COMMENT '编号',
	project_info_id varchar(64) COMMENT '项目id',
  status_origin char(2) COMMENT '项目进度-更新前',
  status_current char(2) COMMENT '项目进度-更新后',
  filepath varchar(2000) COMMENT '附件路径',
	create_by varchar(64) NOT NULL COMMENT '进度更新者',
	create_date datetime NOT NULL COMMENT '进度更新时间',
	update_by varchar(64) NOT NULL COMMENT '进度更新者',
	update_date datetime NOT NULL COMMENT '进度更新时间',
	remarks varchar(255) COMMENT '进度更新备注',
	PRIMARY KEY (id)
) COMMENT = '项目进度变更表';
CREATE INDEX project_info_progress_projectInfo_id ON project_info_progress (project_info_id ASC);

/* 项目管理 菜单添加 */
INSERT INTO jeesite.sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('5c503ff8bdef4172a68233b2dfd8b048', '1', '0,1,', '燕园办公', 300, '', '', '', '1', '', '1', '2016-09-22 17:10:12', '1', '2016-09-22 17:13:12', '', '0'),
('bf267b11cd124363b33b5ab5128be3b2', 'e22d3ad2e03b415fa1ceb212cd76ed5a', '0,1,5c503ff8bdef4172a68233b2dfd8b048,e22d3ad2e03b415fa1ceb212cd76ed5a,', '项目列表', 30, '/project/projectInfo', '', '', '1', 'project:projectInfo:view', '1', '2016-09-22 17:16:49', '1', '2016-09-22 17:18:56', '', '0'),
('e22d3ad2e03b415fa1ceb212cd76ed5a', '5c503ff8bdef4172a68233b2dfd8b048', '0,1,5c503ff8bdef4172a68233b2dfd8b048,', '项目管理', 30, '', '', '', '1', '', '1', '2016-09-22 17:13:44', '1', '2016-09-22 17:16:07', '', '0');

/* 更新项目 行业领域 字典数据 */
DELETE from jeesite.sys_dict WHERE type='industryDomain';
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('4d7e982b28ac47249943b310a0e2476a', '0', '先进制造', 'industryDomain', '行业领域', 10, '0', '1', '2016-09-22 17:23:36', '1', '2016-09-22 17:23:36', '', '0');

/* 更新项目状态字典数据 */
DELETE from jeesite.sys_dict WHERE type='projectStatus';
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('1277ee67ceae466d89ae050175ffdba6', '3', '项目完成', 'projectStatus', '项目负责人设置项目完成状态', 40, '0', '1', '2016-09-23 09:55:47', '1', '2016-09-23 09:55:47', '项目负责人设置项目完成状态', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('1d8569f1e4964186b4c0441c04ba4d00', '0', '推介人编辑', 'projectStatus', '推介人编辑', 10, '0', '1', '2016-09-23 09:52:18', '1', '2016-09-23 09:52:18', '推介人编辑状态，项目的基础资料录入', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('2c9591ec55584b91a904d07635eee438', '2', '项目暂停', 'projectStatus', '项目负责人设置项目暂停状态', 30, '0', '1', '2016-09-23 09:55:26', '1', '2016-09-23 09:55:26', '项目负责人设置项目暂停状态', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('fdf311be46774255beb71b0da142df1a', '1', '负责人管理', 'projectStatus', '项目负责人管理状态', 20, '0', '1', '2016-09-23 09:53:25', '1', '2016-09-23 09:53:25', '项目负责人对项目进行管理维护', '0');

/* 更新项目类型字典数据 */
DELETE from jeesite.sys_dict WHERE type='projectType';
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('3bda1ee6467d41f788068efbc39a89b6', '1', 'A轮', 'projectType', 'A轮', 10, '0', '1', '2016-09-23 09:45:47', '1', '2016-09-23 09:45:47', '', '0');

/* 更新项目级别字典数据 */
DELETE from jeesite.sys_dict WHERE type='projectGrade';
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('402360a329df443bbbdccba4adb2bc7e', 'A3', 'A3', 'projectGrade', '研究阶段，经过至少一个投资部门预研认为具有投资价值，但未接触到企业或现有开发渠道不畅的项目', 30, '0', '1', '2016-10-27 15:55:22', '1', '2016-10-27 15:56:47', '研究阶段，经过至少一个投资部门预研认为具有投资价值，但未接触到企业或现有开发渠道不畅的项目', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('430c441cf97343719c48d47518e37dc2', 'A2', 'A2', 'projectGrade', '开发推进阶段，经过预研有明确投资价值、渠道明确、项目方决策人员与我方投资部门人员或大区负责人进行过交', 20, '0', '1', '2016-10-27 15:53:57', '1', '2016-10-27 15:56:55', '开发推进阶段，经过预研有明确投资价值、渠道明确、项目方决策人员与我方投资部门人员或大区负责人进行过交流，但尚未进场的项目', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('a15308e2158f4cd8aad47bf780b8e669', 'B', 'B', 'projectGrade', '储备阶段，投资价值不明确，仅为开发体系或其渠道推荐的项目', 40, '0', '1', '2016-10-27 15:55:43', '1', '2016-10-27 15:56:40', '储备阶段，投资价值不明确，仅为开发体系或其渠道推荐的项目', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('af01a6222f8346da97890890d6890cf5', 'C', 'C', 'projectGrade', '放弃项目，可明确判断当前阶段不具有可投资性的项目', 50, '0', '1', '2016-10-27 15:56:02', '1', '2016-10-27 15:56:33', '放弃项目，可明确判断当前阶段不具有可投资性的项目', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('c5c0266161c74f3bb7dd4f29c2af31da', 'A1', 'A1', 'projectGrade', '实施阶段，已经进场尽调但尚未最终完成投资的项目', 10, '0', '1', '2016-09-22 17:22:23', '1', '2016-10-27 15:57:01', '实施阶段，已经进场尽调但尚未最终完成投资的项目', '0');

/* 更新项目进度字典数据 */
DELETE from jeesite.sys_dict WHERE type='projectProgress';
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('24899e2a99574351a65bd6732a29768e', '6', '协议签署70%', 'projectProgress', '项目进度', 70, '0', '1', '2016-10-25 15:29:27', '1', '2016-10-25 15:29:27', '', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('6c586aedca9e4aae93525add16cc4e98', '1', '立项20%', 'projectProgress', '项目进度', 20, '0', '1', '2016-10-25 15:26:23', '1', '2016-10-25 15:26:23', '', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('952684d71ab840fe87cc52d6449251f8', '3', '公司评审会40%', 'projectProgress', '项目进度', 40, '0', '1', '2016-10-25 15:28:07', '1', '2016-10-25 15:28:16', '', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('9ecfe55388a24138a87f9a5b337e33d9', '9', '投后管理100%', 'projectProgress', '项目进度', 100, '0', '1', '2016-10-25 15:30:23', '1', '2016-10-25 15:30:23', '', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('b1db8a2e07814c5babf41c30202e9513', '7', '划款80%', 'projectProgress', '项目进度', 80, '0', '1', '2016-10-25 15:29:47', '1', '2016-10-25 15:29:47', '', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('c085c3058f5f4114ac4401c661f96cca', '8', '工商变更90%', 'projectProgress', '项目进度', 90, '0', '1', '2016-10-25 15:30:05', '1', '2016-10-25 15:30:05', '', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('d324e9a9e8824390a9a63c46ecf41f8c', '0', '预研10%', 'projectProgress', '项目进度', 10, '0', '1', '2016-09-22 17:24:22', '1', '2016-10-25 15:25:51', '', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('d62b4d239b2446b6b0c745caf7ed0295', '2', '尽调报告30%', 'projectProgress', '项目进度', 30, '0', '1', '2016-10-25 15:26:49', '1', '2016-10-25 15:26:49', '', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('d6f0531e59b442858fa2907c0f31cfb9', '4', '基金投决会50%', 'projectProgress', '项目进度', 50, '0', '1', '2016-10-25 15:28:42', '1', '2016-10-25 15:28:42', '', '0');
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('df07b237499945259447d0761ef0191b', '5', '投资协议60%', 'projectProgress', '项目进度', 60, '0', '1', '2016-10-25 15:29:03', '1', '2016-10-25 15:29:03', '', '0');





/* 插入 字典表: sys_station_type 角色类型 */
DELETE from jeesite.sys_dict WHERE type='sys_station_type';
INSERT INTO jeesite.sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('4d30063c6bc64be6a5020904d52cb977', '3', '基层', 'sys_station_type', '岗位类型', 30, '0', '1', '2016-09-30 16:46:52', '1', '2016-09-30 16:46:52', '', '0'),
('9de2afd5e8314f3981417e8b31036a7d', '2', '中层', 'sys_station_type', '岗位类型', 20, '0', '1', '2016-09-30 16:46:41', '1', '2016-09-30 16:46:41', '', '0'),
('e04859219e8b423e8bb02e6fd8f8f6b4', '1', '高层', 'sys_station_type', '岗位类型', 10, '0', '1', '2016-09-30 16:46:24', '1', '2016-09-30 16:46:24', '', '0');



/* 岗位管理表 */
DROP TABLE IF EXISTS sys_station;

CREATE TABLE `sys_station` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `parent_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) COLLATE utf8_bin NOT NULL COMMENT '所有父级编号',
  `office_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '归属机构',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '岗位名称',
  `enname` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '英文名称',
  `code` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '编码',
  `station_type` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '岗位类型',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `create_by` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_station_parent_id` (`parent_id`),
  KEY `sys_station_del_flag` (`del_flag`),
  KEY `sys_station_enname` (`enname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='岗位管理';


/* 岗位管理 菜单添加 */
insert into `jeesite`.`sys_menu` ( `id`, `create_date`, `parent_id`, `href`, `create_by`, `permission`, `update_date`, `update_by`, `is_show`, `target`, `icon`, `del_flag`, `parent_ids`, `sort`, `name`, `remarks`) values ( '8243aef876304ffa8d2505c0bf645dc2', '2016-10-19 15:01:05', '13', '/sys/station', '1', '', '2016-10-19 16:39:39', '1', '1', '', 'user-md', '0', '0,1,2,13,', '60', '岗位管理', '');
insert into `jeesite`.`sys_menu` ( `id`, `create_date`, `parent_id`, `href`, `create_by`, `permission`, `update_date`, `update_by`, `is_show`, `target`, `icon`, `del_flag`, `parent_ids`, `sort`, `name`, `remarks`) values ( '6b80d0af4eb5498b85262355e31d711f', '2016-10-19 15:06:29', '8243aef876304ffa8d2505c0bf645dc2', '', '1', 'sys:station:view', '2016-10-19 16:14:25', '1', '0', '', '', '0', '0,1,2,13,8243aef876304ffa8d2505c0bf645dc2,', '30', '查看', '');
insert into `jeesite`.`sys_menu` ( `id`, `create_date`, `parent_id`, `href`, `create_by`, `permission`, `update_date`, `update_by`, `is_show`, `target`, `icon`, `del_flag`, `parent_ids`, `sort`, `name`, `remarks`) values ( '2c8d9d9627124e698b17999306d9ccff', '2016-10-19 15:07:12', '8243aef876304ffa8d2505c0bf645dc2', '', '1', 'sys:station:edit', '2016-10-19 16:10:03', '1', '0', '', '', '0', '0,1,2,13,8243aef876304ffa8d2505c0bf645dc2,', '60', '修改', '');


/* 用户岗位 */
DROP TABLE IF EXISTS sys_user_station;

CREATE TABLE `sys_user_station` (
  `user_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户编号',
  `station_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '岗位编号',
  PRIMARY KEY (`user_id`,`station_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户-岗位';

/* 角色与项目进度关系表 */
DROP TABLE IF EXISTS sys_role_projectprogress;
CREATE TABLE `sys_role_projectprogress` (
  `role_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '角色编号',
  `dict_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '字典编号',
  PRIMARY KEY (`role_id`,`dict_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色与项目进度关系表';


/*
提供stateless restful api
HMAC（Hash-based Message Authentication Code）：基于散列的消息认证码，使用一个密钥和一个消息作为输入，生成它们的消息摘要。注意该密钥只有客户端和服务端知道，其他第三方是不知道的。访问时使用该消息摘要进行传播，服务端然后对该消息摘要进行验证
*/
drop table if exists hmac_client;
create table hmac_client (
    id varchar(64) NOT NULL COMMENT '编号',
    client_name varchar(200),
    client_id varchar(200),
    client_secret varchar(200),
    create_by varchar(64) NOT NULL COMMENT '创建者',
    create_date datetime NOT NULL COMMENT '创建时间',
    update_by varchar(64) NOT NULL COMMENT '更新者',
    update_date datetime NOT NULL COMMENT '更新时间',
    remarks varchar(255) COMMENT '备注信息',
    del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
    primary key(id)
) COMMENT = '客户端表' charset=utf8 ENGINE=InnoDB;
create index idx_hmac_client_client_id on hmac_client(client_id);

/* 增加关于HmacClient功能的菜单 */
INSERT INTO jeesite.sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('79db32fda9834d0985983403de405755', 'e4b50fba63f841beae33450a8f968578', '0,1,2,3,e4b50fba63f841beae33450a8f968578,', '修改', 60, '', '', '', '0', 'client:hmacClient:edit', '1', '2016-11-08 10:41:30', '1', '2016-11-08 10:42:16', '', '0');
INSERT INTO jeesite.sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('d81357f805224a57af6bfcc7be22702c', 'e4b50fba63f841beae33450a8f968578', '0,1,2,3,e4b50fba63f841beae33450a8f968578,', '查看', 30, '', '', '', '0', 'client:hmacClient:view', '1', '2016-11-08 10:41:16', '1', '2016-11-08 10:41:16', '', '0');
INSERT INTO jeesite.sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) VALUES ('e4b50fba63f841beae33450a8f968578', '3', '0,1,2,3,', 'HmacClient', 90, '/client/hmacClient', '', '', '1', '', '1', '2016-11-08 09:26:16', '1', '2016-11-08 09:47:50', '', '0');


/* 项目动态表 */
drop table if exists project_note;
CREATE TABLE project_note
(
	id varchar(64) NOT NULL COMMENT '编号',
	project_id varchar(64) COMMENT '项目编号',
	at_userids varchar(2000) COMMENT '通知人员id(多个人员id使用逗号分隔)',
	content text COMMENT '内容',

	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '项目动态表';

/* 新增 项目动态 的通告类型 */
insert into `jeesite`.`sys_dict` ( `id`, `description`, `create_date`, `label`, `update_date`, `create_by`, `parent_id`, `update_by`, `type`, `value`, `del_flag`, `sort`, `remarks`) values ( '06274b22621243f2b2af78d3ec8107db', '通知通告类型', '2016-11-09 15:27:51', '项目动态', '2016-11-09 15:27:51', '1', '0', '1', 'oa_notify_type', '4', '0', '40', '');

/* 升级用户密码的加密算法由sha1改为sha256;初始用户密码为123456 */
UPDATE sys_user SET `password`='fa3befd31f3e25439ede877aa89a1799e92e0556ffe6592aaa1eb3443ad73c8d8d44b4f212adf303';

/* 用户表增加姓名的拼音字段，以及简拼字段 */
ALTER TABLE sys_user ADD COLUMN `quanpin` varchar(255);
ALTER TABLE sys_user ADD COLUMN `jianpin` varchar(255);


/* 调整菜单 我的面板下的 二级菜单 */
update sys_menu set sort='120' where id=28 and name='个人信息';
update sys_menu set sort='90' where id=71 and name='文件管理';
/*调整通知通告二级菜单,放入一级菜单 我的面板下,并调整我的面板下的二级菜单排序*/
update sys_menu set sort='30',parent_id='27',parent_ids='0,1,27,' where id=88 and name='通知通告';
/*隐藏在线办公一级菜单*/
update sys_menu set is_show=0 where id=62 and name='在线办公';


/* 新增 项目进度更新 的通告类型 */
insert into `jeesite`.`sys_dict` ( `id`, `description`, `create_date`, `label`, `update_date`, `create_by`, `parent_id`, `update_by`, `type`, `value`, `del_flag`, `sort`, `remarks`) values ( '3dbb8b353daa43f5920cafed27dffd7f', '通知通告类型', '2016-12-07 11:46:36', '项目进度更新', '2016-12-07 11:46:36', '1', '0', '1', 'oa_notify_type', '5', '0', '50', '');