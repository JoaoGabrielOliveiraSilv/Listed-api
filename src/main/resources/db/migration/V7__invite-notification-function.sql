create or replace function inviteNotification()
returns trigger as
$inviteNotificationTrigger$
begin
	insert into tb_notification(nm_notification, ds_notification, dt_notification, sg_type_notification, id_read, cd_user)  
		values ('Novo convite!', 'Você recebeu um novo convite para trabalhar em um projeto', CURRENT_DATE, 'INV', FALSE, NEW.cd_user_invite);

	return NEW;
end;
$inviteNotificationTrigger$
LANGUAGE plpgsql