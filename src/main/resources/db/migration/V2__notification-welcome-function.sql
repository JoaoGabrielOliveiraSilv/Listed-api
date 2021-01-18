create or replace function welcomeNotification() 
returns trigger as 
$notificationTrigger$
begin
	insert into tb_notification(nm_notification, ds_notification, dt_notification, sg_type_notification, id_read, cd_user)  values ('Bem vindo ao Listed', 'A Devinno deseja boa sorte com seus projetos!', CURRENT_DATE, 'MSG', FALSE, NEW.cd_user);
	return NEW;
end;
$notificationTrigger$
LANGUAGE plpgsql