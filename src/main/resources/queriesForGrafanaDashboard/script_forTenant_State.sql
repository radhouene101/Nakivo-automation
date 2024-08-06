-------------------------------------------Tenants State------------------------------------
--B:1 rows
SELECT
  count(state) as OK
FROM
  nakivo.tenants
WHERE state="OK"

--C:1 rows
SELECT count(state) as Warning FROM nakivo.tenants where state="WARNING" LIMIT 50

                                                   --D:1 rows
SELECT count(state) as Failed_Error FROM nakivo.tenants where state = "ERROR"

--E:1 rows
SELECT COUNT(state) as INACCESSIBLE FROM nakivo.tenants where state = "INACCESSIBLE" LIMIT 50
------------------------------------------- tenants infos------------------------------------
--A:2 rows
SELECT name, state, used_vms, allocated_backup_storage, used_backup_storage, contact_email FROM nakivo.tenants LIMIT 50

------------------------------------------- tenants infos 2------------------------------------
A:2 rows
SELECT name, enabled, remote_tenant, type, state, connected FROM nakivo.tenants LIMIT 50
