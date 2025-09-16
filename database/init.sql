-- incidents baza
CREATE DATABASE IF NOT EXISTS db_incidents;
CREATE USER IF NOT EXISTS 'incidents_user'@'%' IDENTIFIED BY 'incident_pass';
GRANT ALL PRIVILEGES ON db_incidents.* TO 'incidents_user'@'%';

-- moderacija baza
CREATE DATABASE IF NOT EXISTS db_moderation;
CREATE USER IF NOT EXISTS 'moderation_user'@'%' IDENTIFIED BY 'moderation_pass';
GRANT ALL PRIVILEGES ON db_moderation.* TO 'moderation_user'@'%';

-- analitika baza
CREATE DATABASE IF NOT EXISTS db_incident_analytics;
CREATE USER IF NOT EXISTS 'analytics_user'@'%' IDENTIFIED BY 'analytics_pass';
GRANT ALL PRIVILEGES ON db_incident_analytics.* TO 'analytics_user'@'%';

FLUSH PRIVILEGES;