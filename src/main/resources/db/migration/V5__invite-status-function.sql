create or replace function statusInvite()
returns trigger as
$inviteTrigger$
declare
	cdCategory bigint;
	project RECORD;
begin

	select into project * from tb_project as p, tb_access as a 
							where a.cd_access = NEW.cd_access_sender
									and p.cd_project = a.cd_project;

	if NEW.nm_role = 'Product owner' then
		cdCategory := 1;
	end if;
	if NEW.nm_role = 'Scrum Master' then
		cdCategory := 2;
	end if;
	if NEW.nm_role = 'desenvolvedor' then
		cdCategory := 3;
	end if;
	
	if new.nm_status = 'aceito' then
		insert into tb_access(cd_user, cd_category, cd_project) 
			values (NEW.cd_user_invite, cdCategory, project.cd_project);
	end if;
	
	return NEW;
end;
$inviteTrigger$
LANGUAGE plpgsql