-- Sample data for River City Resources

-- Insert a Transportation resource
INSERT INTO resource (id, version, name, description, services_offered, hours_of_operation, featured, active, view_count, category_id, date_created, last_updated) 
VALUES (1, 0, 'VIA Metropolitan Transit', 
'VIA offers accessible public transportation services including paratransit for individuals with disabilities who cannot use regular bus service. VIAtrans provides curb-to-curb service for eligible riders.',
'Paratransit service (VIAtrans) - door-to-door transportation
Accessible bus routes with wheelchair lifts
Reduced fare programs for seniors and people with disabilities
Travel training programs
Real-time bus tracking',
'VIAtrans: Monday-Saturday 5:00 AM - 1:00 AM, Sunday 5:00 AM - 12:00 AM
Regular bus service: Varies by route',
true, true, 0, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO contact (id, version, phone, email, address, city, state, zip_code, website, resource_id, date_created, last_updated)
VALUES (1, 0, '(210) 362-2020', 'customerservice@viainfo.net', '123 N Medina St', 'San Antonio', 'TX', '78207', 'https://www.viainfo.net', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO eligibility_requirement (id, version, requirement, type, resource_id, date_created, last_updated)
VALUES (1, 0, 'Must be unable to use regular fixed-route bus service due to disability', 'disability', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert an Employment resource
INSERT INTO resource (id, version, name, description, services_offered, hours_of_operation, featured, active, view_count, category_id, date_created, last_updated) 
VALUES (2, 0, 'Goodwill San Antonio', 
'Goodwill provides job training, employment placement services, and other community-based programs for people with disabilities and other barriers to employment.',
'Job readiness training
Career counseling and planning
Job placement assistance
Skills assessment
Resume writing workshops
Interview preparation
On-the-job training programs',
'Monday-Friday 8:00 AM - 5:00 PM',
true, true, 0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO contact (id, version, phone, email, address, city, state, zip_code, website, resource_id, date_created, last_updated)
VALUES (2, 0, '(210) 924-8581', 'info@goodwillsa.org', '406 W Commerce St', 'San Antonio', 'TX', '78207', 'https://www.goodwillsa.org', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert an Education resource
INSERT INTO resource (id, version, name, description, services_offered, hours_of_operation, featured, active, view_count, category_id, date_created, last_updated) 
VALUES (3, 0, 'Any Baby Can', 
'Any Baby Can provides early childhood intervention, family support services, and special education advocacy for children with disabilities and their families.',
'Early Childhood Intervention (ECI) services
Parent education and training
Special education advocacy
Respite care services
Case management
Developmental screenings
Family support groups',
'Monday-Friday 8:00 AM - 5:00 PM',
false, true, 0, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO contact (id, version, phone, email, address, city, state, zip_code, website, resource_id, date_created, last_updated)
VALUES (3, 0, '(210) 227-0170', 'info@anybabycansa.org', '217 Howard St', 'San Antonio', 'TX', '78212', 'https://www.anybabycansa.org', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO eligibility_requirement (id, version, requirement, type, resource_id, date_created, last_updated)
VALUES (2, 0, 'Children must be under 3 years old for ECI services', 'age', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
VALUES (3, 0, 'Must have a documented developmental delay or disability', 'disability', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Update sequences
UPDATE hibernate_sequence SET next_val = 4;