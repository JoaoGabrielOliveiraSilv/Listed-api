create table tb_user(
	cd_user bigserial not null,
	nm_user varchar(50),
	nm_username varchar(21) unique,
	nm_email varchar(120) unique,
	nm_password varchar(255),
	nm_tag varchar(20),
	ds_biography varchar(255),
	im_user varchar(255),

	constraint pk_user
		primary key(cd_user)
);

create table tb_notification (
	cd_notification bigserial not null,
	nm_notification varchar(60),
	ds_notification varchar(255),
	dt_notification date,
	sg_type_notification varchar(3),
	id_read boolean,
	cd_user bigint,
	
	constraint pk_notification
		primary key(cd_notification),

	constraint fk_notification_user
		foreign key (cd_user)
			references tb_user(cd_user)
);

create table tb_password_recovery (
	cd_password_recovery bigserial not null,
	nm_email varchar(120),
	nm_username_hash varchar(255),
	ds_token varchar(255),
	cd_user bigint,
	
	constraint pk_passwordRecovery
		primary key(cd_password_recovery),
	
	constraint fk_passwordRecovery_user
		foreign key(cd_user)
			references tb_user(cd_user)
);

create table tb_project(
	cd_project bigserial not null,
	nm_project varchar(100),
	ds_project varchar(255),
	dt_start date,
	dt_end date,
	id_concluded boolean,
	qt_week_sprint int,
	
	constraint pk_project
		primary key(cd_project)
);

create table tb_category(
	cd_category bigserial not null,
	nm_category varchar(24),

	constraint pk_category
		primary key(cd_category)
);



create table tb_backlog(
	cd_backlog bigserial not null,
	nm_backlog varchar(100),
	ds_backlog varchar(255),
	qt_difficulty int,
	qt_priority int,
	cd_project bigint,

	constraint pk_backlog
		primary key(cd_backlog),

	constraint fk_backlog_project
		foreign key(cd_project)
			references tb_project(cd_project)
);

create table tb_sprint(
	cd_sprint bigserial not null,
	nm_sprint varchar(100),
	ds_sprint varchar(255),
	ds_meta_sprint varchar(120),
	dt_start date,
	dt_end date,
	id_concluded boolean,
	qt_percentage int,
	cd_project bigint,

	constraint pk_sprint
		primary key(cd_sprint),

	constraint fk_sprint_project
		foreign key(cd_project)
			references tb_project(cd_project)
);
	
create table tb_access(
	cd_access bigserial not null,
	cd_user bigint,
	cd_category bigint,
	cd_project bigint,

	constraint pk_acess
		primary key(cd_access),

	constraint fk_access_user
		foreign key(cd_user)
			references tb_user(cd_user),

	constraint fk_access_category
		foreign key(cd_category)
			references tb_category(cd_category),
			
	constraint fk_access_project
		foreign key(cd_project)
			references tb_project(cd_project)
);
	
create table tb_invite (
	cd_invite bigserial not null,
	nm_invite varchar(80),
	ds_invite varchar(255),
	dt_invite date,
	nm_status varchar(8),
	nm_role varchar(13),
	cd_user_invite bigint,
	cd_access_sender bigint,
	
	
	constraint pk_invite
		primary key(cd_invite),

	constraint fk_invite_user
		foreign key(cd_user_invite)
			references tb_user(cd_user),
			
	constraint fk_invite_access
		foreign key (cd_access_sender)
			references tb_access(cd_access)
);	
	
create table tb_task(
	cd_task bigserial not null,
	nm_task varchar(100),
	id_concluded boolean,
	cd_backlog bigint,
	cd_access bigint,

	constraint pk_task
		primary key(cd_task),

	constraint fk_task_backlog
		foreign key(cd_backlog)
			references tb_backlog(cd_backlog),
			
	constraint fk_task_access
		foreign key(cd_access)
			references tb_access(cd_access)
);

create table sprint_backlog(
	cd_sprint_backlog bigserial not null,
	cd_sprint bigint not null,
	cd_backlog bigint not null,
	id_concluded boolean,
	
	constraint pk_sprint_backlog
		primary key(cd_sprint_backlog),

	constraint fk_item_sprint
		foreign key(cd_sprint)
			references tb_sprint(cd_sprint),
	
	constraint fk_item_backlog
		foreign key(cd_backlog)
			references tb_backlog(cd_backlog)
);