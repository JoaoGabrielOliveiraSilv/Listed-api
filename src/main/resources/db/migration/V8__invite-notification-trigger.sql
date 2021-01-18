create trigger inviteNotification_trigger
after insert on tb_invite
for each row
execute procedure inviteNotification();