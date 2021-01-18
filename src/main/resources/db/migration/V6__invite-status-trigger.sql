create trigger inviteStatus_trigger
after update on tb_invite
for each row
execute procedure statusInvite();