-- Initial database setup for River City Resources
-- This file is executed when the MySQL container is first created

-- Ensure the database exists
CREATE DATABASE IF NOT EXISTS rivercityresources;
USE rivercityresources;

-- Grant all privileges to the application user
GRANT ALL PRIVILEGES ON rivercityresources.* TO 'grails'@'%';
FLUSH PRIVILEGES;

-- Create indexes for better performance (these will be created after Grails creates the tables)
-- You can uncomment and modify these after the initial run
-- CREATE INDEX idx_resource_category ON resource(category_id);
-- CREATE INDEX idx_resource_active ON resource(active);
-- CREATE INDEX idx_resource_featured ON resource(featured);
-- CREATE INDEX idx_eligibility_resource ON eligibility_requirement(resource_id);