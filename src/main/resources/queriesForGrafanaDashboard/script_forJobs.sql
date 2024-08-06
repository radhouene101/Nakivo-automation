-------------------------------------Inspect: jobs Availabilities (pie chart )-------------------------------------
--A:1 rows
SELECT count(status) as available_jobs_running FROM nakivo.jobs where status="GREEN" LIMIT 50
--B:1 rows
SELECT count(status) as stopped_jobs FROM nakivo.jobs where status = "GRAY" LIMIT 50

----------------------------------------Inspect: job types (pie chart)-------------------------------------------
--A:1 rows
SELECT count(job_type) as backup FROM nakivo.jobs where job_type="BACKUP" LIMIT 50
--B:1 rows
select count(job_type) as site_recovery from nakivo.jobs where job_type = "SITE_RECOVERY"


--------------------------------------------Inspect: Jobs Data-------------------------------------------------------