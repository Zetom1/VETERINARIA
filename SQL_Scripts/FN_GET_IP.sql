create or replace  function FN_GET_IP
return varchar2
is begin
    return nvl(sys_context('userenv','ip_adress'),sys_context('userenv','host'));
    exception
when others then return 'localhost';

end FN_GET_IP;