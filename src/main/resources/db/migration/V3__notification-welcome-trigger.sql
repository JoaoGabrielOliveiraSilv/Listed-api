create trigger notificationWelcome_trigger
after insert on tb_user
for each row
execute procedure welcomeNotification();