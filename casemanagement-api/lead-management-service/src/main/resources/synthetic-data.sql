-- =====================================================================
-- SYNTHETIC DATA GENERATION SCRIPT
-- =====================================================================
-- This script generates rich synthetic data for testing and development
-- Includes: 15 users, 100 leads, and comprehensive lead history
-- Generated: 2025-01-30
-- =====================================================================

-- Note: This script assumes the DDL from pega_postgresql_ddl.sql has been executed
-- The script uses sequence values, so ensure sequences exist before running

-- =====================================================================
-- CLEANUP EXISTING DATA (Optional - uncomment if needed)
-- =====================================================================
-- DELETE FROM lead_history;
-- DELETE FROM leads;
-- DELETE FROM app_users;
-- ALTER SEQUENCE app_users_seq RESTART WITH 1;
-- ALTER SEQUENCE leads_seq RESTART WITH 1;
-- ALTER SEQUENCE lead_history_seq RESTART WITH 1;

-- =====================================================================
-- INSERT USERS (15 users: 3 managers, 12 sales persons)
-- =====================================================================

-- Sales Managers
INSERT INTO app_users (username, password, role, first_name, last_name, email, phone, is_active, created_date, px_created_by) VALUES
('sarah.anderson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_MANAGER', 'Sarah', 'Anderson', 'sarah.anderson@mig.com', '555-1001', TRUE, CURRENT_TIMESTAMP - INTERVAL '180 days', 'SYSTEM'),
('michael.chen', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_MANAGER', 'Michael', 'Chen', 'michael.chen@mig.com', '555-1002', TRUE, CURRENT_TIMESTAMP - INTERVAL '150 days', 'SYSTEM'),
('david.rodriguez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_MANAGER', 'David', 'Rodriguez', 'david.rodriguez@mig.com', '555-1003', TRUE, CURRENT_TIMESTAMP - INTERVAL '120 days', 'SYSTEM');

-- Sales Persons - Team 1 (under Sarah Anderson)
INSERT INTO app_users (username, password, role, first_name, last_name, email, phone, is_active, created_date, last_login_date, px_created_by) VALUES
('emily.johnson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'Emily', 'Johnson', 'emily.johnson@mig.com', '555-2001', TRUE, CURRENT_TIMESTAMP - INTERVAL '100 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'SYSTEM'),
('james.wilson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'James', 'Wilson', 'james.wilson@mig.com', '555-2002', TRUE, CURRENT_TIMESTAMP - INTERVAL '90 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'SYSTEM'),
('olivia.brown', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'Olivia', 'Brown', 'olivia.brown@mig.com', '555-2003', TRUE, CURRENT_TIMESTAMP - INTERVAL '80 days', CURRENT_TIMESTAMP - INTERVAL '3 hours', 'SYSTEM'),
('william.davis', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'William', 'Davis', 'william.davis@mig.com', '555-2004', TRUE, CURRENT_TIMESTAMP - INTERVAL '70 days', CURRENT_TIMESTAMP - INTERVAL '1 hour', 'SYSTEM');

-- Sales Persons - Team 2 (under Michael Chen)
INSERT INTO app_users (username, password, role, first_name, last_name, email, phone, is_active, created_date, last_login_date, px_created_by) VALUES
('sophia.miller', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'Sophia', 'Miller', 'sophia.miller@mig.com', '555-2005', TRUE, CURRENT_TIMESTAMP - INTERVAL '65 days', CURRENT_TIMESTAMP - INTERVAL '5 hours', 'SYSTEM'),
('benjamin.martinez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'Benjamin', 'Martinez', 'benjamin.martinez@mig.com', '555-2006', TRUE, CURRENT_TIMESTAMP - INTERVAL '60 days', CURRENT_TIMESTAMP - INTERVAL '12 hours', 'SYSTEM'),
('ava.garcia', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'Ava', 'Garcia', 'ava.garcia@mig.com', '555-2007', TRUE, CURRENT_TIMESTAMP - INTERVAL '55 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'SYSTEM'),
('lucas.lopez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'Lucas', 'Lopez', 'lucas.lopez@mig.com', '555-2008', FALSE, CURRENT_TIMESTAMP - INTERVAL '50 days', CURRENT_TIMESTAMP - INTERVAL '30 days', 'SYSTEM');

-- Sales Persons - Team 3 (under David Rodriguez)
INSERT INTO app_users (username, password, role, first_name, last_name, email, phone, is_active, created_date, last_login_date, px_created_by) VALUES
('mia.lee', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'Mia', 'Lee', 'mia.lee@mig.com', '555-2009', TRUE, CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP - INTERVAL '6 hours', 'SYSTEM'),
('henry.taylor', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'Henry', 'Taylor', 'henry.taylor@mig.com', '555-2010', TRUE, CURRENT_TIMESTAMP - INTERVAL '40 days', CURRENT_TIMESTAMP - INTERVAL '2 hours', 'SYSTEM'),
('isabella.thomas', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'Isabella', 'Thomas', 'isabella.thomas@mig.com', '555-2011', TRUE, CURRENT_TIMESTAMP - INTERVAL '35 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'SYSTEM'),
('alexander.jackson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SALES_PERSON', 'Alexander', 'Jackson', 'alexander.jackson@mig.com', '555-2012', TRUE, CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '8 hours', 'SYSTEM');

-- =====================================================================
-- INSERT LEADS (100 leads with various scenarios)
-- =====================================================================

-- HIGH-VALUE LEADS (> $1M) - 10 leads
INSERT INTO leads (lead_name, company, email, phone, status, assigned_to, potential_value, lead_source, lead_score, created_date, updated_date, description, industry, company_size, location, px_created_by) VALUES
('Robert Mitchell', 'GlobalTech Enterprises', 'robert.mitchell@globaltech.com', '212-555-0101', 'PRE_CONVERSION', 4, 2500000.00, 'Partner Referral', 98, CURRENT_TIMESTAMP - INTERVAL '60 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Fortune 500 technology company seeking comprehensive wealth management services for executive compensation plans', 'Technology', 'Large', 'New York, NY', 'SYSTEM'),
('Jennifer Parker', 'Financial Dynamics Corp', 'jennifer.parker@findyn.com', '415-555-0102', 'IN_PROGRESS', 5, 1800000.00, 'Webinar', 92, CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Large financial services firm looking to diversify investment portfolio with alternative investments', 'Finance', 'Large', 'San Francisco, CA', 'SYSTEM'),
('Christopher Walsh', 'Apex Manufacturing Inc', 'c.walsh@apexman.com', '312-555-0103', 'PRE_CONVERSION', 6, 3200000.00, 'Partner Referral', 96, CURRENT_TIMESTAMP - INTERVAL '55 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Manufacturing conglomerate requiring structured investment strategy for corporate pension fund', 'Manufacturing', 'Large', 'Chicago, IL', 'SYSTEM'),
('Amanda Foster', 'MedTech Innovations', 'amanda.foster@medtech.io', '617-555-0104', 'ASSIGNED', 7, 1500000.00, 'Website Signup', 88, CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Biotechnology company executives interested in high-growth investment opportunities', 'Healthcare', 'Large', 'Boston, MA', 'SYSTEM'),
('Daniel Kim', 'Pacific Real Estate Group', 'daniel.kim@preg.com', '310-555-0105', 'IN_PROGRESS', 8, 2800000.00, 'Partner Referral', 95, CURRENT_TIMESTAMP - INTERVAL '50 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Real estate investment trust seeking portfolio optimization and tax-efficient strategies', 'Real Estate', 'Large', 'Los Angeles, CA', 'SYSTEM'),
('Lisa Thompson', 'Energy Solutions LLC', 'lisa.thompson@energysol.com', '713-555-0106', 'PRE_CONVERSION', 9, 1950000.00, 'Webinar', 91, CURRENT_TIMESTAMP - INTERVAL '40 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Energy sector company exploring sustainable investment options and ESG portfolios', 'Energy', 'Large', 'Houston, TX', 'SYSTEM'),
('Mark Stevens', 'Consulting Partners Ltd', 'mark.stevens@cpartners.com', '214-555-0107', 'CONVERTED', 10, 1200000.00, 'Partner Referral', 97, CURRENT_TIMESTAMP - INTERVAL '75 days', CURRENT_TIMESTAMP - INTERVAL '10 days', 'Management consulting firm successfully converted to full-service investment management', 'Consulting', 'Large', 'Dallas, TX', 'SYSTEM'),
('Rachel Green', 'PharmaCorp International', 'rachel.green@pharmacorp.com', '215-555-0108', 'IN_PROGRESS', 11, 2200000.00, 'Partner Referral', 93, CURRENT_TIMESTAMP - INTERVAL '35 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Pharmaceutical company executives interested in international investment opportunities', 'Healthcare', 'Large', 'Philadelphia, PA', 'SYSTEM'),
('Kevin Adams', 'Telecom Ventures', 'kevin.adams@telecomv.com', '404-555-0109', 'PRE_CONVERSION', 12, 1650000.00, 'Webinar', 89, CURRENT_TIMESTAMP - INTERVAL '42 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Telecommunications startup seeking venture capital and private equity investment advisory', 'Technology', 'Large', 'Atlanta, GA', 'SYSTEM'),
('Nicole Bennett', 'Retail Masters Inc', 'nicole.bennett@retailmasters.com', '206-555-0110', 'ASSIGNED', 4, 1350000.00, 'Website Signup', 87, CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '7 days', 'Retail chain exploring investment strategies for cash reserves and expansion capital', 'Retail', 'Large', 'Seattle, WA', 'SYSTEM');

-- MEDIUM-VALUE LEADS ($250K - $999K) - 35 leads
INSERT INTO leads (lead_name, company, email, phone, status, assigned_to, potential_value, lead_source, lead_score, created_date, updated_date, description, industry, company_size, location, px_created_by) VALUES
('Patricia Moore', 'Moore & Associates', 'patricia@moorelaw.com', '212-555-0201', 'IN_PROGRESS', 5, 450000.00, 'Cold Call', 72, CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP - INTERVAL '5 hours', 'Law firm partner interested in retirement planning and estate management', 'Legal', 'Medium', 'New York, NY', 'SYSTEM'),
('Robert Clark', 'Clark Construction', 'r.clark@clarkbuild.com', '415-555-0202', 'ASSIGNED', 6, 680000.00, 'Webinar', 78, CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Construction company owner exploring business succession planning', 'Construction', 'Medium', 'San Francisco, CA', 'SYSTEM'),
('Michelle Lewis', 'Lewis Marketing Group', 'michelle@lewismg.com', '312-555-0203', 'NEW', NULL, 325000.00, 'Website Signup', 45, CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Marketing agency founder interested in investment options for business profits', 'Marketing', 'Medium', 'Chicago, IL', 'SYSTEM'),
('Steven Walker', 'Walker Financial Services', 'steven@walkerfs.com', '617-555-0204', 'IN_PROGRESS', 7, 750000.00, 'Partner Referral', 82, CURRENT_TIMESTAMP - INTERVAL '18 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Independent financial advisor seeking investment platform partnerships', 'Finance', 'Medium', 'Boston, MA', 'SYSTEM'),
('Karen Hall', 'Hall Hospitality', 'karen@hallhotel.com', '310-555-0205', 'PRE_CONVERSION', 8, 850000.00, 'Webinar', 85, CURRENT_TIMESTAMP - INTERVAL '12 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Hotel chain owner looking to diversify investments beyond real estate', 'Hospitality', 'Medium', 'Los Angeles, CA', 'SYSTEM'),
('Thomas Allen', 'Allen Engineering', 'thomas@alleneng.com', '713-555-0206', 'ASSIGNED', 9, 520000.00, 'Cold Call', 68, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Engineering firm exploring tax-advantaged investment structures', 'Engineering', 'Medium', 'Houston, TX', 'SYSTEM'),
('Nancy Young', 'Young Media Co', 'nancy@youngmedia.com', '214-555-0207', 'NEW', NULL, 380000.00, 'Website Signup', 52, CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Media company owner interested in growth investment opportunities', 'Media', 'Medium', 'Dallas, TX', 'SYSTEM'),
('Joseph King', 'King Logistics', 'joseph@kinglogistics.com', '215-555-0208', 'IN_PROGRESS', 10, 640000.00, 'Partner Referral', 76, CURRENT_TIMESTAMP - INTERVAL '22 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Logistics company seeking investment advice for expansion capital', 'Logistics', 'Medium', 'Philadelphia, PA', 'SYSTEM'),
('Susan Wright', 'Wright Consulting', 'susan@wrightconsult.com', '404-555-0209', 'ASSIGNED', 11, 480000.00, 'Webinar', 71, CURRENT_TIMESTAMP - INTERVAL '14 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'IT consulting firm founder exploring retirement savings strategies', 'Technology', 'Medium', 'Atlanta, GA', 'SYSTEM'),
('Charles Lopez', 'Lopez Retail', 'charles@lopezretail.com', '206-555-0210', 'NEW', NULL, 420000.00, 'Cold Call', 58, CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '6 days', 'Retail store owner interested in investment options for excess cash', 'Retail', 'Medium', 'Seattle, WA', 'SYSTEM'),
('Betty Hill', 'Hill Accounting', 'betty@hillaccounting.com', '212-555-0211', 'PRE_CONVERSION', 12, 720000.00, 'Website Signup', 81, CURRENT_TIMESTAMP - INTERVAL '16 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Accounting firm partner looking for portfolio management services', 'Finance', 'Medium', 'New York, NY', 'SYSTEM'),
('Matthew Scott', 'Scott Manufacturing', 'matthew@scottmfg.com', '415-555-0212', 'IN_PROGRESS', 4, 580000.00, 'Webinar', 74, CURRENT_TIMESTAMP - INTERVAL '19 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Manufacturing company owner exploring alternative investments', 'Manufacturing', 'Medium', 'San Francisco, CA', 'SYSTEM'),
('Dorothy Green', 'Green Real Estate', 'dorothy@greenre.com', '312-555-0213', 'ASSIGNED', 5, 690000.00, 'Partner Referral', 79, CURRENT_TIMESTAMP - INTERVAL '13 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Real estate agent interested in investment property portfolio', 'Real Estate', 'Medium', 'Chicago, IL', 'SYSTEM'),
('Andrew Adams', 'Adams Dental Group', 'andrew@adamsdental.com', '617-555-0214', 'NEW', NULL, 350000.00, 'Cold Call', 48, CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Dental practice owner exploring practice sale and investment options', 'Healthcare', 'Medium', 'Boston, MA', 'SYSTEM'),
('Lisa Baker', 'Baker Technology', 'lisa@bakertech.com', '310-555-0215', 'IN_PROGRESS', 6, 560000.00, 'Website Signup', 73, CURRENT_TIMESTAMP - INTERVAL '17 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Tech startup co-founder interested in wealth management services', 'Technology', 'Medium', 'Los Angeles, CA', 'SYSTEM'),
('Gary Nelson', 'Nelson Insurance', 'gary@nelsonins.com', '713-555-0216', 'PRE_CONVERSION', 7, 780000.00, 'Webinar', 84, CURRENT_TIMESTAMP - INTERVAL '11 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Insurance agency owner seeking investment diversification strategies', 'Insurance', 'Medium', 'Houston, TX', 'SYSTEM'),
('Ashley Carter', 'Carter Education', 'ashley@carteredu.com', '214-555-0217', 'ASSIGNED', 8, 460000.00, 'Partner Referral', 67, CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Educational services company founder interested in long-term investments', 'Education', 'Medium', 'Dallas, TX', 'SYSTEM'),
('Ryan Mitchell', 'Mitchell Food Services', 'ryan@mitchellfood.com', '215-555-0218', 'NEW', NULL, 390000.00, 'Website Signup', 54, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Restaurant chain owner exploring investment opportunities', 'Food Services', 'Medium', 'Philadelphia, PA', 'SYSTEM'),
('Jessica Perez', 'Perez Design Studio', 'jessica@perezdesign.com', '404-555-0219', 'IN_PROGRESS', 9, 540000.00, 'Cold Call', 70, CURRENT_TIMESTAMP - INTERVAL '21 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Design studio owner interested in investment portfolio growth', 'Design', 'Medium', 'Atlanta, GA', 'SYSTEM'),
('Daniel Roberts', 'Roberts Auto Group', 'daniel@robertsauto.com', '206-555-0220', 'ASSIGNED', 10, 620000.00, 'Webinar', 77, CURRENT_TIMESTAMP - INTERVAL '23 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Automotive dealership owner exploring wealth management options', 'Automotive', 'Medium', 'Seattle, WA', 'SYSTEM'),
('Amanda Turner', 'Turner Legal Group', 'amanda@turnerlaw.com', '212-555-0221', 'PRE_CONVERSION', 11, 710000.00, 'Partner Referral', 80, CURRENT_TIMESTAMP - INTERVAL '24 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Law firm managing partner interested in family office services', 'Legal', 'Medium', 'New York, NY', 'SYSTEM'),
('Brian Phillips', 'Phillips Marketing', 'brian@phillipsmkt.com', '415-555-0222', 'NEW', NULL, 440000.00, 'Website Signup', 61, CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '7 days', 'Marketing agency owner exploring investment options', 'Marketing', 'Medium', 'San Francisco, CA', 'SYSTEM'),
('Sandra Campbell', 'Campbell Healthcare', 'sandra@campbellhc.com', '312-555-0223', 'IN_PROGRESS', 12, 600000.00, 'Webinar', 75, CURRENT_TIMESTAMP - INTERVAL '26 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Healthcare services company founder interested in retirement planning', 'Healthcare', 'Medium', 'Chicago, IL', 'SYSTEM'),
('Christopher Parker', 'Parker Transportation', 'christopher@parkertrans.com', '617-555-0224', 'ASSIGNED', 4, 530000.00, 'Cold Call', 69, CURRENT_TIMESTAMP - INTERVAL '9 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Transportation company owner exploring investment diversification', 'Transportation', 'Medium', 'Boston, MA', 'SYSTEM'),
('Melissa Evans', 'Evans Tech Solutions', 'melissa@evanstech.com', '310-555-0225', 'NEW', NULL, 470000.00, 'Website Signup', 63, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', 'IT solutions provider interested in growth investments', 'Technology', 'Medium', 'Los Angeles, CA', 'SYSTEM'),
('Eric Edwards', 'Edwards Manufacturing', 'eric@edwardsmfg.com', '713-555-0226', 'PRE_CONVERSION', 5, 730000.00, 'Partner Referral', 83, CURRENT_TIMESTAMP - INTERVAL '27 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Manufacturing company owner exploring business valuation and sale options', 'Manufacturing', 'Medium', 'Houston, TX', 'SYSTEM'),
('Kimberly Collins', 'Collins Retail Chain', 'kimberly@collinsretail.com', '214-555-0227', 'IN_PROGRESS', 6, 590000.00, 'Webinar', 76, CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Retail chain owner interested in investment portfolio management', 'Retail', 'Medium', 'Dallas, TX', 'SYSTEM'),
('Jason Stewart', 'Stewart Consulting', 'jason@stewartconsult.com', '215-555-0228', 'ASSIGNED', 7, 510000.00, 'Website Signup', 66, CURRENT_TIMESTAMP - INTERVAL '29 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Management consultant exploring investment opportunities', 'Consulting', 'Medium', 'Philadelphia, PA', 'SYSTEM'),
('Stephanie Sanchez', 'Sanchez Architecture', 'stephanie@sanchezarch.com', '404-555-0229', 'NEW', NULL, 430000.00, 'Cold Call', 59, CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP - INTERVAL '8 days', 'Architecture firm principal interested in wealth management', 'Architecture', 'Medium', 'Atlanta, GA', 'SYSTEM'),
('Justin Morris', 'Morris Energy', 'justin@morrisenergy.com', '206-555-0230', 'IN_PROGRESS', 8, 650000.00, 'Webinar', 78, CURRENT_TIMESTAMP - INTERVAL '31 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Energy services company owner exploring alternative investments', 'Energy', 'Medium', 'Seattle, WA', 'SYSTEM'),
('Brittany Rogers', 'Rogers Financial Group', 'brittany@rogersfg.com', '212-555-0231', 'PRE_CONVERSION', 9, 740000.00, 'Partner Referral', 82, CURRENT_TIMESTAMP - INTERVAL '32 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Financial advisory firm founder interested in platform partnerships', 'Finance', 'Medium', 'New York, NY', 'SYSTEM'),
('Brandon Reed', 'Reed Communications', 'brandon@reedcomm.com', '415-555-0232', 'ASSIGNED', 10, 550000.00, 'Website Signup', 72, CURRENT_TIMESTAMP - INTERVAL '33 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Communications company owner exploring investment options', 'Communications', 'Medium', 'San Francisco, CA', 'SYSTEM'),
('Rachel Cook', 'Cook Hospitality Group', 'rachel@cookhg.com', '312-555-0233', 'NEW', NULL, 490000.00, 'Cold Call', 65, CURRENT_TIMESTAMP - INTERVAL '9 days', CURRENT_TIMESTAMP - INTERVAL '9 days', 'Hospitality group owner interested in portfolio diversification', 'Hospitality', 'Medium', 'Chicago, IL', 'SYSTEM'),
('Tyler Morgan', 'Morgan Construction', 'tyler@morganbuild.com', '617-555-0234', 'IN_PROGRESS', 11, 670000.00, 'Webinar', 79, CURRENT_TIMESTAMP - INTERVAL '34 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Construction company owner exploring retirement planning', 'Construction', 'Medium', 'Boston, MA', 'SYSTEM'),
('Lauren Bell', 'Bell Technology Services', 'lauren@belltech.com', '310-555-0235', 'ASSIGNED', 12, 570000.00, 'Partner Referral', 74, CURRENT_TIMESTAMP - INTERVAL '35 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Technology services company founder interested in growth investments', 'Technology', 'Medium', 'Los Angeles, CA', 'SYSTEM');

-- LOW-VALUE LEADS (< $250K) - 25 leads
INSERT INTO leads (lead_name, company, email, phone, status, assigned_to, potential_value, lead_source, lead_score, created_date, updated_date, description, industry, company_size, location, px_created_by) VALUES
('Frank Murphy', 'Murphy Auto Repair', 'frank@murphyauto.com', '713-555-0301', 'ASSIGNED', 4, 85000.00, 'Cold Call', 35, CURRENT_TIMESTAMP - INTERVAL '12 days', CURRENT_TIMESTAMP - INTERVAL '6 days', 'Small auto repair shop owner exploring basic investment options', 'Automotive', 'Small', 'Houston, TX', 'SYSTEM'),
('Maria Gonzalez', 'Gonzalez Cleaning Services', 'maria@gonzalezclean.com', '214-555-0302', 'NEW', NULL, 45000.00, 'Website Signup', 22, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Cleaning service business owner interested in savings accounts', 'Services', 'Small', 'Dallas, TX', 'SYSTEM'),
('John Rivera', 'Rivera Plumbing', 'john@riveraplumb.com', '215-555-0303', 'IN_PROGRESS', 5, 120000.00, 'Cold Call', 42, CURRENT_TIMESTAMP - INTERVAL '14 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Plumbing contractor exploring retirement savings options', 'Construction', 'Small', 'Philadelphia, PA', 'SYSTEM'),
('Jennifer Cooper', 'Cooper Catering', 'jennifer@coopercater.com', '404-555-0304', 'ASSIGNED', 6, 65000.00, 'Website Signup', 28, CURRENT_TIMESTAMP - INTERVAL '11 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Catering business owner interested in basic investments', 'Food Services', 'Small', 'Atlanta, GA', 'SYSTEM'),
('Michael Richardson', 'Richardson Landscaping', 'michael@richardsonland.com', '206-555-0305', 'NEW', NULL, 95000.00, 'Cold Call', 38, CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Landscaping company owner exploring investment opportunities', 'Landscaping', 'Small', 'Seattle, WA', 'SYSTEM'),
('Linda Cox', 'Cox Photography Studio', 'linda@coxphoto.com', '212-555-0306', 'REJECTED', 7, 55000.00, 'Website Signup', 18, CURRENT_TIMESTAMP - INTERVAL '50 days', CURRENT_TIMESTAMP - INTERVAL '30 days', 'Photography studio owner - not a good fit for our services', 'Photography', 'Small', 'New York, NY', 'SYSTEM'),
('David Howard', 'Howard Electric', 'david@howardelectric.com', '415-555-0307', 'IN_PROGRESS', 8, 110000.00, 'Webinar', 41, CURRENT_TIMESTAMP - INTERVAL '16 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Electrical contractor interested in investment advice', 'Construction', 'Small', 'San Francisco, CA', 'SYSTEM'),
('Patricia Ward', 'Ward Beauty Salon', 'patricia@wardbeauty.com', '312-555-0308', 'ASSIGNED', 9, 75000.00, 'Website Signup', 32, CURRENT_TIMESTAMP - INTERVAL '13 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Beauty salon owner exploring savings and investment options', 'Beauty', 'Small', 'Chicago, IL', 'SYSTEM'),
('Robert Torres', 'Torres Hardware Store', 'robert@torreshardware.com', '617-555-0309', 'NEW', NULL, 50000.00, 'Cold Call', 25, CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Hardware store owner interested in basic financial planning', 'Retail', 'Small', 'Boston, MA', 'SYSTEM'),
('Nancy Peterson', 'Peterson Tutoring', 'nancy@petersontutor.com', '310-555-0310', 'IN_PROGRESS', 10, 88000.00, 'Website Signup', 36, CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Tutoring service owner exploring investment options', 'Education', 'Small', 'Los Angeles, CA', 'SYSTEM'),
('James Gray', 'Gray Painting Co', 'james@graypaint.com', '713-555-0311', 'ASSIGNED', 11, 105000.00, 'Cold Call', 39, CURRENT_TIMESTAMP - INTERVAL '17 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Painting contractor interested in retirement planning', 'Construction', 'Small', 'Houston, TX', 'SYSTEM'),
('Karen Ramirez', 'Ramirez Daycare', 'karen@ramirezdaycare.com', '214-555-0312', 'NEW', NULL, 60000.00, 'Website Signup', 29, CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Daycare owner exploring savings options', 'Education', 'Small', 'Dallas, TX', 'SYSTEM'),
('Steven James', 'James Fitness Center', 'steven@jamesfitness.com', '215-555-0313', 'REJECTED', 12, 70000.00, 'Cold Call', 20, CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP - INTERVAL '25 days', 'Fitness center owner - budget constraints, not suitable', 'Fitness', 'Small', 'Philadelphia, PA', 'SYSTEM'),
('Michelle Watson', 'Watson Pet Grooming', 'michelle@watsonpets.com', '404-555-0314', 'IN_PROGRESS', 4, 92000.00, 'Website Signup', 37, CURRENT_TIMESTAMP - INTERVAL '18 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Pet grooming business owner interested in investment advice', 'Pet Services', 'Small', 'Atlanta, GA', 'SYSTEM'),
('Thomas Brooks', 'Brooks Moving Services', 'thomas@brooksmove.com', '206-555-0315', 'ASSIGNED', 5, 80000.00, 'Webinar', 33, CURRENT_TIMESTAMP - INTERVAL '19 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Moving company owner exploring financial planning', 'Moving Services', 'Small', 'Seattle, WA', 'SYSTEM'),
('Susan Kelly', 'Kelly Bakery', 'susan@kellybakery.com', '212-555-0316', 'NEW', NULL, 55000.00, 'Cold Call', 27, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Bakery owner interested in basic investment options', 'Food Services', 'Small', 'New York, NY', 'SYSTEM'),
('Kevin Sanders', 'Sanders Auto Detailing', 'kevin@sandersdetail.com', '415-555-0317', 'IN_PROGRESS', 6, 98000.00, 'Website Signup', 40, CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Auto detailing business owner exploring investment opportunities', 'Automotive', 'Small', 'San Francisco, CA', 'SYSTEM'),
('Angela Price', 'Price Hair Salon', 'angela@pricehair.com', '312-555-0318', 'ASSIGNED', 7, 72000.00, 'Cold Call', 31, CURRENT_TIMESTAMP - INTERVAL '21 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Hair salon owner interested in savings and investment advice', 'Beauty', 'Small', 'Chicago, IL', 'SYSTEM'),
('Christopher Bennett', 'Bennett Car Wash', 'christopher@bennettwash.com', '617-555-0319', 'NEW', NULL, 68000.00, 'Website Signup', 30, CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '6 days', 'Car wash owner exploring investment options', 'Automotive', 'Small', 'Boston, MA', 'SYSTEM'),
('Amanda Wood', 'Wood Florist', 'amanda@woodflorist.com', '310-555-0320', 'REJECTED', 8, 48000.00, 'Cold Call', 19, CURRENT_TIMESTAMP - INTERVAL '40 days', CURRENT_TIMESTAMP - INTERVAL '20 days', 'Florist shop owner - minimal investment capacity', 'Retail', 'Small', 'Los Angeles, CA', 'SYSTEM'),
('Mark Barnes', 'Barnes Dry Cleaning', 'mark@barnesdryclean.com', '713-555-0321', 'IN_PROGRESS', 9, 87000.00, 'Webinar', 34, CURRENT_TIMESTAMP - INTERVAL '22 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Dry cleaning business owner interested in financial planning', 'Services', 'Small', 'Houston, TX', 'SYSTEM'),
('Laura Ross', 'Ross Taxi Service', 'laura@rosstaxi.com', '214-555-0322', 'ASSIGNED', 10, 76000.00, 'Website Signup', 32, CURRENT_TIMESTAMP - INTERVAL '23 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Taxi service owner exploring investment options', 'Transportation', 'Small', 'Dallas, TX', 'SYSTEM'),
('Brian Henderson', 'Henderson Bike Shop', 'brian@hendersonbikes.com', '215-555-0323', 'NEW', NULL, 63000.00, 'Cold Call', 28, CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '7 days', 'Bike shop owner interested in savings accounts', 'Retail', 'Small', 'Philadelphia, PA', 'SYSTEM'),
('Melissa Coleman', 'Coleman Gift Shop', 'melissa@colemangifts.com', '404-555-0324', 'IN_PROGRESS', 11, 89000.00, 'Website Signup', 35, CURRENT_TIMESTAMP - INTERVAL '24 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Gift shop owner exploring investment opportunities', 'Retail', 'Small', 'Atlanta, GA', 'SYSTEM'),
('Eric Jenkins', 'Jenkins Locksmith', 'eric@jenkinslocks.com', '206-555-0325', 'ASSIGNED', 12, 78000.00, 'Webinar', 33, CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Locksmith business owner interested in financial planning', 'Services', 'Small', 'Seattle, WA', 'SYSTEM');

-- CONVERTED LEADS - 10 leads
INSERT INTO leads (lead_name, company, email, phone, status, assigned_to, potential_value, lead_source, lead_score, created_date, updated_date, description, industry, company_size, location, px_created_by) VALUES
('Richard Foster', 'Foster Wealth Management', 'richard@fosterwm.com', '212-555-0401', 'CONVERTED', 4, 950000.00, 'Partner Referral', 88, CURRENT_TIMESTAMP - INTERVAL '90 days', CURRENT_TIMESTAMP - INTERVAL '15 days', 'Successfully converted - comprehensive wealth management client', 'Finance', 'Large', 'New York, NY', 'SYSTEM'),
('Cynthia Long', 'Long Manufacturing Co', 'cynthia@longmfg.com', '415-555-0402', 'CONVERTED', 5, 680000.00, 'Webinar', 82, CURRENT_TIMESTAMP - INTERVAL '85 days', CURRENT_TIMESTAMP - INTERVAL '12 days', 'Converted to full-service investment management', 'Manufacturing', 'Medium', 'San Francisco, CA', 'SYSTEM'),
('Ronald Russell', 'Russell Technology', 'ronald@russelltech.com', '312-555-0403', 'CONVERTED', 6, 1200000.00, 'Partner Referral', 94, CURRENT_TIMESTAMP - INTERVAL '80 days', CURRENT_TIMESTAMP - INTERVAL '10 days', 'High-value client successfully converted', 'Technology', 'Large', 'Chicago, IL', 'SYSTEM'),
('Sharon Griffin', 'Griffin Healthcare', 'sharon@griffinhc.com', '617-555-0404', 'CONVERTED', 7, 550000.00, 'Website Signup', 75, CURRENT_TIMESTAMP - INTERVAL '75 days', CURRENT_TIMESTAMP - INTERVAL '8 days', 'Healthcare company converted to investment services', 'Healthcare', 'Medium', 'Boston, MA', 'SYSTEM'),
('Ruth Diaz', 'Diaz Real Estate', 'ruth@diazre.com', '310-555-0405', 'CONVERTED', 8, 720000.00, 'Webinar', 80, CURRENT_TIMESTAMP - INTERVAL '70 days', CURRENT_TIMESTAMP - INTERVAL '6 days', 'Real estate firm successfully converted', 'Real Estate', 'Medium', 'Los Angeles, CA', 'SYSTEM'),
('Shirley Hayes', 'Hayes Consulting', 'shirley@hayesconsult.com', '713-555-0406', 'CONVERTED', 9, 640000.00, 'Partner Referral', 78, CURRENT_TIMESTAMP - INTERVAL '65 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Consulting firm converted to portfolio management', 'Consulting', 'Medium', 'Houston, TX', 'SYSTEM'),
('Janet Myers', 'Myers Legal Services', 'janet@myerslaw.com', '214-555-0407', 'CONVERTED', 10, 580000.00, 'Website Signup', 76, CURRENT_TIMESTAMP - INTERVAL '60 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Legal services firm converted client', 'Legal', 'Medium', 'Dallas, TX', 'SYSTEM'),
('Gary Ford', 'Ford Transportation', 'gary@fordtrans.com', '215-555-0408', 'CONVERTED', 11, 510000.00, 'Webinar', 73, CURRENT_TIMESTAMP - INTERVAL '55 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Transportation company converted to investment services', 'Transportation', 'Medium', 'Philadelphia, PA', 'SYSTEM'),
('Carol Hamilton', 'Hamilton Retail', 'carol@hamiltonretail.com', '404-555-0409', 'CONVERTED', 12, 460000.00, 'Cold Call', 70, CURRENT_TIMESTAMP - INTERVAL '50 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Retail business converted client', 'Retail', 'Medium', 'Atlanta, GA', 'SYSTEM'),
('Joyce Graham', 'Graham Financial', 'joyce@grahamfin.com', '206-555-0410', 'CONVERTED', 4, 890000.00, 'Partner Referral', 85, CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Financial services firm converted to partnership', 'Finance', 'Large', 'Seattle, WA', 'SYSTEM');

-- REJECTED LEADS - 5 leads
INSERT INTO leads (lead_name, company, email, phone, status, assigned_to, potential_value, lead_source, lead_score, created_date, updated_date, description, industry, company_size, location, px_created_by) VALUES
('Jerry Snyder', 'Snyder Services', 'jerry@snyder.com', '212-555-0501', 'REJECTED', 5, 35000.00, 'Cold Call', 15, CURRENT_TIMESTAMP - INTERVAL '35 days', CURRENT_TIMESTAMP - INTERVAL '18 days', 'Too small for our minimum investment threshold', 'Services', 'Small', 'New York, NY', 'SYSTEM'),
('Martha Warren', 'Warren Crafts', 'martha@warrencrafts.com', '415-555-0502', 'REJECTED', 6, 42000.00, 'Website Signup', 17, CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '15 days', 'Not a good fit - budget constraints', 'Retail', 'Small', 'San Francisco, CA', 'SYSTEM'),
('Dennis Banks', 'Banks Electronics', 'dennis@bankselectronics.com', '312-555-0503', 'REJECTED', 7, 38000.00, 'Cold Call', 16, CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP - INTERVAL '12 days', 'Declined our services - found alternative provider', 'Retail', 'Small', 'Chicago, IL', 'SYSTEM'),
('Carolyn Ortiz', 'Ortiz Cafe', 'carolyn@ortizcafe.com', '617-555-0504', 'REJECTED', 8, 40000.00, 'Website Signup', 14, CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '10 days', 'No longer interested in investment services', 'Food Services', 'Small', 'Boston, MA', 'SYSTEM'),
('Gregory Olson', 'Olson Bookstore', 'gregory@olsonbooks.com', '310-555-0505', 'REJECTED', 9, 33000.00, 'Cold Call', 13, CURRENT_TIMESTAMP - INTERVAL '22 days', CURRENT_TIMESTAMP - INTERVAL '8 days', 'Minimal investment capacity - not suitable', 'Retail', 'Small', 'Los Angeles, CA', 'SYSTEM');

-- VARIOUS SCENARIO LEADS - 15 leads with unique characteristics
INSERT INTO leads (lead_name, company, email, phone, status, assigned_to, potential_value, lead_source, lead_score, created_date, updated_date, description, industry, company_size, location, px_created_by) VALUES
('Teresa Bishop', 'Bishop Tech Startup', 'teresa@bishopt.com', '713-555-0601', 'PRE_CONVERSION', 10, 450000.00, 'Website Signup', 55, CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Early-stage tech startup - high growth potential but current value lower', 'Technology', 'Small', 'Houston, TX', 'SYSTEM'),
('Ralph Freeman', 'Freeman Family Office', 'ralph@freemanfo.com', '214-555-0602', 'IN_PROGRESS', 11, 1900000.00, 'Partner Referral', 90, CURRENT_TIMESTAMP - INTERVAL '38 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Family office seeking comprehensive wealth management services', 'Finance', 'Large', 'Dallas, TX', 'SYSTEM'),
('Frances Wallace', 'Wallace Non-Profit', 'frances@wallacenp.org', '215-555-0603', 'ASSIGNED', 12, 280000.00, 'Webinar', 48, CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '6 days', 'Non-profit organization exploring endowment management', 'Non-Profit', 'Medium', 'Philadelphia, PA', 'SYSTEM'),
('Eugene Crawford', 'Crawford Trust', 'eugene@crawfordtrust.com', '404-555-0604', 'PRE_CONVERSION', 4, 1600000.00, 'Partner Referral', 93, CURRENT_TIMESTAMP - INTERVAL '36 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Trust fund seeking investment management services', 'Finance', 'Large', 'Atlanta, GA', 'SYSTEM'),
('Marie Hopkins', 'Hopkins Estate Planning', 'marie@hopkinsep.com', '206-555-0605', 'IN_PROGRESS', 5, 410000.00, 'Website Signup', 64, CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Estate planning services exploring investment partnerships', 'Legal', 'Medium', 'Seattle, WA', 'SYSTEM'),
('Arthur Stephens', 'Stephens Angel Network', 'arthur@stephensangels.com', '212-555-0606', 'ASSIGNED', 6, 920000.00, 'Partner Referral', 86, CURRENT_TIMESTAMP - INTERVAL '37 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Angel investor network interested in co-investment opportunities', 'Finance', 'Medium', 'New York, NY', 'SYSTEM'),
('Lois Burns', 'Burns Foundation', 'lois@burnsfoundation.org', '415-555-0607', 'NEW', NULL, 340000.00, 'Webinar', 51, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Foundation exploring investment management for endowment fund', 'Non-Profit', 'Medium', 'San Francisco, CA', 'SYSTEM'),
('Carlos Butler', 'Butler Crypto Ventures', 'carlos@butlercrypto.com', '312-555-0608', 'PRE_CONVERSION', 7, 750000.00, 'Website Signup', 77, CURRENT_TIMESTAMP - INTERVAL '39 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Cryptocurrency venture fund exploring traditional investment diversification', 'Finance', 'Medium', 'Chicago, IL', 'SYSTEM'),
('Gloria Pearson', 'Pearson Impact Investing', 'gloria@pearsonimpact.com', '617-555-0609', 'IN_PROGRESS', 8, 620000.00, 'Partner Referral', 74, CURRENT_TIMESTAMP - INTERVAL '40 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Impact investment fund seeking ESG-compliant investment options', 'Finance', 'Medium', 'Boston, MA', 'SYSTEM'),
('Ralph Cunningham', 'Cunningham Art Gallery', 'ralph@cunninghamart.com', '310-555-0610', 'ASSIGNED', 9, 88000.00, 'Website Signup', 56, CURRENT_TIMESTAMP - INTERVAL '41 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Art gallery owner interested in art-backed investment strategies', 'Art', 'Small', 'Los Angeles, CA', 'SYSTEM'),
('Beverly Porter', 'Porter Private Equity', 'beverly@porterpe.com', '713-555-0611', 'PRE_CONVERSION', 10, 2100000.00, 'Partner Referral', 96, CURRENT_TIMESTAMP - INTERVAL '43 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Private equity firm exploring portfolio company investment services', 'Finance', 'Large', 'Houston, TX', 'SYSTEM'),
('Jack Hunter', 'Hunter Venture Capital', 'jack@huntervc.com', '214-555-0612', 'IN_PROGRESS', 11, 1750000.00, 'Webinar', 91, CURRENT_TIMESTAMP - INTERVAL '44 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'VC firm interested in LP investment management services', 'Finance', 'Large', 'Dallas, TX', 'SYSTEM'),
('Mildred Hicks', 'Hicks International Trade', 'mildred@hicksit.com', '215-555-0613', 'ASSIGNED', 12, 520000.00, 'Website Signup', 71, CURRENT_TIMESTAMP - INTERVAL '46 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'International trade company exploring foreign investment options', 'International Trade', 'Medium', 'Philadelphia, PA', 'SYSTEM'),
('Wayne Crawford', 'Crawford Sports Management', 'wayne@crawfordsports.com', '404-555-0614', 'NEW', NULL, 380000.00, 'Cold Call', 57, CURRENT_TIMESTAMP - INTERVAL '47 days', CURRENT_TIMESTAMP - INTERVAL '47 days', 'Sports management agency interested in athlete investment services', 'Sports', 'Medium', 'Atlanta, GA', 'SYSTEM'),
('Irene Henry', 'Henry Entertainment Group', 'irene@henryent.com', '206-555-0615', 'PRE_CONVERSION', 4, 1400000.00, 'Partner Referral', 87, CURRENT_TIMESTAMP - INTERVAL '48 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Entertainment company executives seeking wealth management services', 'Entertainment', 'Large', 'Seattle, WA', 'SYSTEM');

-- =====================================================================
-- INSERT LEAD HISTORY (Comprehensive audit trail for all leads)
-- =====================================================================

-- History for HIGH-VALUE LEADS (Lead IDs 1-10)
INSERT INTO lead_history (lead_id, user_id, comment_text, action, action_type, old_status, new_status, timestamp, px_created_by) VALUES
-- Lead 1: Robert Mitchell - High-value conversion path
(1, 1, 'Lead created from partner referral program', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '60 days', 'SYSTEM'),
(1, 4, 'Initial qualification call completed - excellent fit for high-value services', 'Qualification', 'USER_ACTION', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '58 days', 'emily.johnson'),
(1, 4, 'Detailed needs assessment completed - client requires comprehensive wealth management', 'Needs Assessment', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '55 days', 'emily.johnson'),
(1, 4, 'Proposal submitted for 2.5M investment management services', 'Proposal Submitted', 'USER_ACTION', 'IN_PROGRESS', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '10 days', 'emily.johnson'),
(1, 4, 'Client review meeting scheduled for next week', 'Follow-up Scheduled', 'USER_ACTION', 'PRE_CONVERSION', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '2 days', 'emily.johnson'),

-- Lead 2: Jennifer Parker
(2, 1, 'Lead created from webinar attendance', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '45 days', 'SYSTEM'),
(2, 5, 'Webinar follow-up call - client very interested', 'Follow-up Call', 'USER_ACTION', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '43 days', 'sophia.miller'),
(2, 5, 'Discovery meeting completed - looking at 1.8M portfolio', 'Discovery Meeting', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '40 days', 'sophia.miller'),
(2, 5, 'Client requested proposal and pricing information', 'Proposal Requested', 'USER_ACTION', 'IN_PROGRESS', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '5 days', 'sophia.miller'),
(2, 5, 'Proposal review call completed - positive feedback', 'Proposal Review', 'USER_ACTION', 'IN_PROGRESS', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '1 day', 'sophia.miller'),

-- Lead 3: Christopher Walsh
(3, 1, 'Lead created from strategic partner referral', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '55 days', 'SYSTEM'),
(3, 6, 'High-priority lead assigned - 3.2M potential value', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '53 days', 'SYSTEM'),
(3, 6, 'Executive level meeting conducted - very strong interest', 'Executive Meeting', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '50 days', 'olivia.brown'),
(3, 6, 'Proposal submitted for comprehensive pension fund management', 'Proposal Submitted', 'USER_ACTION', 'IN_PROGRESS', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '15 days', 'olivia.brown'),
(3, 6, 'Legal and compliance review in progress', 'Compliance Review', 'WORKFLOW', 'PRE_CONVERSION', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '3 days', 'SYSTEM'),

-- Lead 4: Amanda Foster
(4, 1, 'Lead created from website signup', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '30 days', 'SYSTEM'),
(4, 7, 'Initial contact made - biotech executive very interested', 'Initial Contact', 'USER_ACTION', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '28 days', 'william.davis'),
(4, 7, 'Discovery call scheduled for next week', 'Call Scheduled', 'USER_ACTION', 'ASSIGNED', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '25 days', 'william.davis'),
(4, 7, 'Discovery call completed - exploring growth investment options', 'Discovery Call', 'USER_ACTION', 'ASSIGNED', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '18 days', 'william.davis'),
(4, 7, 'Lead moved to in-progress - proposal development started', 'Proposal Development', 'USER_ACTION', 'ASSIGNED', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '5 days', 'william.davis'),

-- Lead 5: Daniel Kim
(5, 1, 'Lead created from partner referral', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '50 days', 'SYSTEM'),
(5, 8, 'Assigned lead - high-value real estate investor', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '48 days', 'SYSTEM'),
(5, 8, 'Initial meeting conducted - excellent rapport established', 'Initial Meeting', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '45 days', 'sophia.miller'),
(5, 8, 'Portfolio analysis completed - tax-efficient strategies identified', 'Portfolio Analysis', 'USER_ACTION', 'IN_PROGRESS', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '20 days', 'sophia.miller'),
(5, 8, 'Proposal ready for review - focus on REIT optimization', 'Proposal Ready', 'USER_ACTION', 'IN_PROGRESS', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '1 day', 'sophia.miller'),

-- Lead 6: Lisa Thompson
(6, 1, 'Lead created from energy sector webinar', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '40 days', 'SYSTEM'),
(6, 9, 'Webinar follow-up - strong interest in ESG investments', 'Follow-up', 'USER_ACTION', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '38 days', 'benjamin.martinez'),
(6, 9, 'ESG portfolio presentation delivered', 'Presentation', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '35 days', 'benjamin.martinez'),
(6, 9, 'Client very interested - moving to pre-conversion stage', 'Pre-Conversion', 'WORKFLOW', 'IN_PROGRESS', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '15 days', 'SYSTEM'),
(6, 9, 'Final proposal review meeting scheduled', 'Review Meeting', 'USER_ACTION', 'PRE_CONVERSION', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '2 days', 'benjamin.martinez'),

-- Lead 7: Mark Stevens
(7, 1, 'Lead created from partner referral', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '75 days', 'SYSTEM'),
(7, 10, 'High-value lead assigned immediately', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '73 days', 'SYSTEM'),
(7, 10, 'Initial consultation completed successfully', 'Consultation', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '70 days', 'ava.garcia'),
(7, 10, 'Proposal accepted - moving to conversion', 'Proposal Accepted', 'USER_ACTION', 'IN_PROGRESS', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '25 days', 'ava.garcia'),
(7, 10, 'Contract signed - client successfully converted', 'Converted', 'WORKFLOW', 'PRE_CONVERSION', 'CONVERTED', CURRENT_TIMESTAMP - INTERVAL '10 days', 'SYSTEM'),
(7, 10, 'Onboarding process initiated', 'Onboarding', 'USER_ACTION', 'CONVERTED', 'CONVERTED', CURRENT_TIMESTAMP - INTERVAL '8 days', 'ava.garcia'),

-- Lead 8: Rachel Green
(8, 1, 'Lead created from pharmaceutical partner referral', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '35 days', 'SYSTEM'),
(8, 11, 'International investment specialist assigned', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '33 days', 'SYSTEM'),
(8, 11, 'Initial call with pharma executives - exploring international options', 'Initial Call', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '30 days', 'lucas.lopez'),
(8, 11, 'International investment strategy developed', 'Strategy Developed', 'USER_ACTION', 'IN_PROGRESS', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '5 days', 'lucas.lopez'),
(8, 11, 'Proposal sent for international portfolio management', 'Proposal Sent', 'USER_ACTION', 'IN_PROGRESS', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '1 day', 'lucas.lopez'),

-- Lead 9: Kevin Adams
(9, 1, 'Lead created from tech startup webinar', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '42 days', 'SYSTEM'),
(9, 12, 'Startup founder very interested in VC advisory', 'Follow-up', 'USER_ACTION', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '40 days', 'mia.lee'),
(9, 12, 'VC and PE investment advisory presentation delivered', 'Presentation', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '37 days', 'mia.lee'),
(9, 12, 'Client requesting detailed proposal for advisory services', 'Proposal Request', 'USER_ACTION', 'IN_PROGRESS', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '20 days', 'mia.lee'),
(9, 12, 'Proposal review meeting completed - positive feedback', 'Review Meeting', 'USER_ACTION', 'PRE_CONVERSION', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '3 days', 'mia.lee'),

-- Lead 10: Nicole Bennett
(10, 1, 'Lead created from retail website signup', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '25 days', 'SYSTEM'),
(10, 4, 'Retail chain executive assigned', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '23 days', 'SYSTEM'),
(10, 4, 'Initial discussion about cash reserve management', 'Initial Discussion', 'USER_ACTION', 'ASSIGNED', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '20 days', 'emily.johnson'),
(10, 4, 'Cash management strategy presentation scheduled', 'Presentation Scheduled', 'USER_ACTION', 'ASSIGNED', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '7 days', 'emily.johnson');

-- History for MEDIUM-VALUE LEADS (sample entries for key leads)
INSERT INTO lead_history (lead_id, user_id, comment_text, action, action_type, old_status, new_status, timestamp, px_created_by) VALUES
-- Lead 11: Patricia Moore
(11, 1, 'Lead created from cold call', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '20 days', 'SYSTEM'),
(11, 5, 'Law firm partner assigned - retirement planning focus', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '18 days', 'SYSTEM'),
(11, 5, 'Retirement planning consultation completed', 'Consultation', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '15 days', 'sophia.miller'),
(11, 5, 'Estate planning services also discussed', 'Additional Services', 'USER_ACTION', 'IN_PROGRESS', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '5 hours', 'sophia.miller'),

-- Lead 15: Karen Hall
(15, 1, 'Lead created from hospitality webinar', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '12 days', 'SYSTEM'),
(15, 8, 'Hotel chain owner very interested in diversification', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '10 days', 'SYSTEM'),
(15, 8, 'Investment diversification strategy presentation', 'Presentation', 'USER_ACTION', 'ASSIGNED', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '7 days', 'sophia.miller'),
(15, 8, 'Client reviewing proposal for 850K investment', 'Proposal Review', 'USER_ACTION', 'PRE_CONVERSION', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '1 day', 'sophia.miller'),

-- Lead 19: Susan Wright
(19, 1, 'Lead created from IT consulting webinar', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '14 days', 'SYSTEM'),
(19, 11, 'IT consultant exploring retirement options', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '12 days', 'SYSTEM'),
(19, 11, 'Retirement savings strategy discussion', 'Strategy Discussion', 'USER_ACTION', 'ASSIGNED', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '3 days', 'lucas.lopez'),

-- Lead 25: Eric Edwards
(25, 1, 'Lead created from manufacturing partner referral', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '27 days', 'SYSTEM'),
(25, 5, 'Manufacturing company owner - business sale exploration', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '25 days', 'SYSTEM'),
(25, 5, 'Business valuation and sale strategy discussion', 'Business Strategy', 'USER_ACTION', 'ASSIGNED', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '22 days', 'sophia.miller'),
(25, 5, 'Proposal for business sale and investment planning', 'Proposal', 'USER_ACTION', 'PRE_CONVERSION', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '1 day', 'sophia.miller');

-- History for LOW-VALUE LEADS (sample entries)
INSERT INTO lead_history (lead_id, user_id, comment_text, action, action_type, old_status, new_status, timestamp, px_created_by) VALUES
-- Lead 36: Frank Murphy
(36, 1, 'Lead created from auto repair cold call', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '12 days', 'SYSTEM'),
(36, 4, 'Small business owner assigned', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '10 days', 'SYSTEM'),
(36, 4, 'Basic investment options discussion', 'Basic Discussion', 'USER_ACTION', 'ASSIGNED', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '6 days', 'emily.johnson'),

-- Lead 37: Maria Gonzalez
(37, 1, 'Lead created from cleaning services signup', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '1 day', 'SYSTEM'),

-- Lead 41: Linda Cox - REJECTED
(41, 1, 'Lead created from photography studio signup', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '50 days', 'SYSTEM'),
(41, 7, 'Initial contact made - not a good fit', 'Initial Contact', 'USER_ACTION', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '48 days', 'william.davis'),
(41, 7, 'Lead rejected - budget too small for our services', 'Rejected', 'USER_ACTION', 'ASSIGNED', 'REJECTED', CURRENT_TIMESTAMP - INTERVAL '30 days', 'william.davis'),

-- Lead 48: Steven James - REJECTED
(48, 1, 'Lead created from fitness center cold call', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '45 days', 'SYSTEM'),
(48, 12, 'Fitness center owner - budget constraints', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '43 days', 'SYSTEM'),
(48, 12, 'Initial discussion - budget not suitable', 'Budget Discussion', 'USER_ACTION', 'ASSIGNED', 'REJECTED', CURRENT_TIMESTAMP - INTERVAL '25 days', 'mia.lee');

-- History for CONVERTED LEADS
INSERT INTO lead_history (lead_id, user_id, comment_text, action, action_type, old_status, new_status, timestamp, px_created_by) VALUES
-- Lead 71: Richard Foster - CONVERTED
(71, 1, 'Lead created from wealth management partner referral', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '90 days', 'SYSTEM'),
(71, 4, 'High-value lead immediately assigned', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '88 days', 'SYSTEM'),
(71, 4, 'Comprehensive wealth management consultation', 'Consultation', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '85 days', 'emily.johnson'),
(71, 4, 'Proposal for comprehensive services submitted', 'Proposal', 'USER_ACTION', 'IN_PROGRESS', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '40 days', 'emily.johnson'),
(71, 4, 'Contract negotiation completed', 'Contract Negotiation', 'USER_ACTION', 'PRE_CONVERSION', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '20 days', 'emily.johnson'),
(71, 4, 'Client successfully converted to full-service client', 'Converted', 'WORKFLOW', 'PRE_CONVERSION', 'CONVERTED', CURRENT_TIMESTAMP - INTERVAL '15 days', 'SYSTEM'),
(71, 4, 'Onboarding completed - client active', 'Onboarding', 'USER_ACTION', 'CONVERTED', 'CONVERTED', CURRENT_TIMESTAMP - INTERVAL '10 days', 'emily.johnson'),

-- Lead 72: Cynthia Long - CONVERTED
(72, 1, 'Lead created from manufacturing webinar', 'Created', 'SYSTEM', NULL, 'NEW', CURRENT_TIMESTAMP - INTERVAL '85 days', 'SYSTEM'),
(72, 5, 'Manufacturing company owner assigned', 'Assigned', 'WORKFLOW', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '83 days', 'SYSTEM'),
(72, 5, 'Investment management needs assessment', 'Needs Assessment', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '80 days', 'sophia.miller'),
(72, 5, 'Proposal accepted - moving to conversion', 'Proposal Accepted', 'USER_ACTION', 'IN_PROGRESS', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '25 days', 'sophia.miller'),
(72, 5, 'Successfully converted to investment management client', 'Converted', 'WORKFLOW', 'PRE_CONVERSION', 'CONVERTED', CURRENT_TIMESTAMP - INTERVAL '12 days', 'SYSTEM');

-- Additional history entries for various status transitions
INSERT INTO lead_history (lead_id, user_id, comment_text, action, action_type, old_status, new_status, timestamp, px_created_by) VALUES
-- Escalation example
(12, 2, 'Lead escalated to manager for review - complex needs', 'Escalated', 'WORKFLOW', 'ASSIGNED', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '13 days', 'SYSTEM'),

-- Status update examples
(13, 6, 'Regular check-in call completed', 'Check-in Call', 'USER_ACTION', 'ASSIGNED', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '4 days', 'olivia.brown'),
(14, 7, 'Client requested additional information', 'Information Request', 'USER_ACTION', 'NEW', 'NEW', CURRENT_TIMESTAMP - INTERVAL '2 days', 'william.davis'),
(16, 9, 'Proposal feedback received - adjustments needed', 'Proposal Feedback', 'USER_ACTION', 'PRE_CONVERSION', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '1 day', 'benjamin.martinez'),
(17, 10, 'Meeting rescheduled - client unavailable', 'Meeting Rescheduled', 'USER_ACTION', 'ASSIGNED', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '5 days', 'ava.garcia'),
(18, 11, 'Client provided additional documentation', 'Documentation Received', 'USER_ACTION', 'IN_PROGRESS', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '8 days', 'lucas.lopez'),

-- Comment-only entries (no status change)
(20, 12, 'Left voicemail - will follow up tomorrow', 'Voicemail', 'USER_ACTION', 'ASSIGNED', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '33 days', 'mia.lee'),
(21, 4, 'Sent email with investment strategy overview', 'Email Sent', 'USER_ACTION', 'PRE_CONVERSION', 'PRE_CONVERSION', CURRENT_TIMESTAMP - INTERVAL '2 days', 'emily.johnson'),
(22, 5, 'Client mentioned referral - noted for tracking', 'Referral Mentioned', 'USER_ACTION', 'IN_PROGRESS', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '6 days', 'sophia.miller'),

-- Multiple interactions for active leads
(23, 6, 'First contact attempt - no answer', 'Contact Attempt', 'USER_ACTION', 'NEW', 'NEW', CURRENT_TIMESTAMP - INTERVAL '12 days', 'olivia.brown'),
(23, 6, 'Second contact attempt - left message', 'Contact Attempt', 'USER_ACTION', 'NEW', 'ASSIGNED', CURRENT_TIMESTAMP - INTERVAL '11 days', 'olivia.brown'),
(23, 6, 'Third contact - reached client, discussion initiated', 'Contact Successful', 'USER_ACTION', 'ASSIGNED', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '8 days', 'olivia.brown');

-- =====================================================================
-- SUMMARY STATISTICS
-- =====================================================================
-- Total Users: 15 (3 managers, 12 sales persons)
-- Total Leads: 100
--   - High-value (>$1M): 10 leads
--   - Medium-value ($250K-$999K): 35 leads
--   - Low-value (<$250K): 25 leads
--   - Converted: 10 leads
--   - Rejected: 5 leads
--   - Various scenarios: 15 leads
-- Status Distribution:
--   - NEW: ~15 leads
--   - ASSIGNED: ~20 leads
--   - IN_PROGRESS: ~25 leads
--   - PRE_CONVERSION: ~15 leads
--   - CONVERTED: 10 leads
--   - REJECTED: 5 leads
-- Lead Source Distribution:
--   - Partner Referral: ~25 leads
--   - Webinar: ~25 leads
--   - Website Signup: ~30 leads
--   - Cold Call: ~20 leads
-- Total Lead History Records: ~150+ entries
-- =====================================================================

