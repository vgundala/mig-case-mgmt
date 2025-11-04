-- =====================================================================
-- Updated for BOOLEAN is_active and Hibernate/JPA compatibility (2025-10-30)
-- =====================================================================
-- This DDL script is optimized for Pega Blueprint integration
-- Includes performance indexes, views, and metadata for data class mapping
-- Converted from Oracle DDL to PostgreSQL

-- Drop existing objects if they exist, to allow for a clean setup
DROP TABLE IF EXISTS lead_history CASCADE;
DROP TABLE IF EXISTS leads CASCADE;
DROP TABLE IF EXISTS app_users CASCADE;

-- Drop sequences
DROP SEQUENCE IF EXISTS app_users_seq CASCADE;
DROP SEQUENCE IF EXISTS leads_seq CASCADE;
DROP SEQUENCE IF EXISTS lead_history_seq CASCADE;

-- Drop views
DROP VIEW IF EXISTS v_my_leads CASCADE;
DROP VIEW IF EXISTS v_new_leads CASCADE;
DROP VIEW IF EXISTS v_high_value_leads CASCADE;

-- =====================================================================
-- CREATE SEQUENCES
-- =====================================================================

CREATE SEQUENCE app_users_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE leads_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE lead_history_seq START WITH 1 INCREMENT BY 1 NO CYCLE;

-- =====================================================================
-- CREATE TABLES
-- =====================================================================

-- Users Table: Stores login information and roles for all system users
-- Pega Data Class: Data-User
CREATE TABLE app_users (
    user_id BIGINT PRIMARY KEY DEFAULT nextval('app_users_seq'),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Hashed password using BCrypt
    role VARCHAR(20) NOT NULL CHECK (role IN ('SALES_PERSON', 'SALES_MANAGER')),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_date TIMESTAMP,
    -- Audit fields for Pega
    px_created_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    px_updated_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    px_created_by VARCHAR(50),
    px_updated_by VARCHAR(50)
);

COMMENT ON TABLE app_users IS 'User accounts for the lead management system. Maps to Pega Data-User class.';
COMMENT ON COLUMN app_users.user_id IS 'Primary key for user identification';
COMMENT ON COLUMN app_users.username IS 'Unique username for login';
COMMENT ON COLUMN app_users.password IS 'BCrypt hashed password';
COMMENT ON COLUMN app_users.role IS 'User role: SALES_PERSON or SALES_MANAGER';
COMMENT ON COLUMN app_users.is_active IS 'User account status';
COMMENT ON COLUMN app_users.px_created_datetime IS 'Pega audit field - creation timestamp';
COMMENT ON COLUMN app_users.px_updated_datetime IS 'Pega audit field - last update timestamp';

-- Leads Table: Stores information about potential clients (leads)
-- Pega Data Class: Data-Lead
CREATE TABLE leads (
    lead_id BIGINT PRIMARY KEY DEFAULT nextval('leads_seq'),
    lead_name VARCHAR(100) NOT NULL,
    company VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    status VARCHAR(20) NOT NULL CHECK (status IN ('NEW', 'ASSIGNED', 'IN_PROGRESS', 'PRE_CONVERSION', 'CONVERTED', 'REJECTED')),
    assigned_to BIGINT,
    potential_value DECIMAL(12, 2),
    lead_source VARCHAR(50),
    lead_score INTEGER CHECK (lead_score BETWEEN 0 AND 100),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- Additional fields for Pega integration
    description TEXT,
    industry VARCHAR(50),
    company_size VARCHAR(20),
    location VARCHAR(100),
    -- Audit fields for Pega
    px_created_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    px_updated_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    px_created_by VARCHAR(50),
    px_updated_by VARCHAR(50),
    -- Foreign key constraint
    FOREIGN KEY (assigned_to) REFERENCES app_users(user_id)
);

COMMENT ON TABLE leads IS 'Lead information for potential clients. Maps to Pega Data-Lead class.';
COMMENT ON COLUMN leads.lead_id IS 'Primary key for lead identification';
COMMENT ON COLUMN leads.lead_name IS 'Full name of the lead contact';
COMMENT ON COLUMN leads.company IS 'Company name of the lead';
COMMENT ON COLUMN leads.status IS 'Current status in the sales pipeline';
COMMENT ON COLUMN leads.assigned_to IS 'User ID of assigned sales person';
COMMENT ON COLUMN leads.potential_value IS 'Estimated deal value in USD';
COMMENT ON COLUMN leads.lead_source IS 'Source of the lead';
COMMENT ON COLUMN leads.lead_score IS 'Calculated lead score (0-100)';
COMMENT ON COLUMN leads.px_created_datetime IS 'Pega audit field - creation timestamp';
COMMENT ON COLUMN leads.px_updated_datetime IS 'Pega audit field - last update timestamp';

-- Lead History Table: A log of all actions and comments related to a lead
-- Pega Data Class: Data-LeadHistory
CREATE TABLE lead_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('lead_history_seq'),
    lead_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    comment_text VARCHAR(4000),
    action VARCHAR(100), -- e.g., 'Created', 'Assigned', 'Comment Added', 'Escalated', 'Approved', 'Rejected'
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- Additional fields for Pega
    action_type VARCHAR(50), -- 'SYSTEM', 'USER_ACTION', 'WORKFLOW'
    old_status VARCHAR(20),
    new_status VARCHAR(20),
    -- Audit fields for Pega
    px_created_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    px_updated_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    px_created_by VARCHAR(50),
    px_updated_by VARCHAR(50),
    -- Foreign key constraints
    FOREIGN KEY (lead_id) REFERENCES leads(lead_id),
    FOREIGN KEY (user_id) REFERENCES app_users(user_id)
);

COMMENT ON TABLE lead_history IS 'Audit trail for lead activities and status changes. Maps to Pega Data-LeadHistory class.';
COMMENT ON COLUMN lead_history.history_id IS 'Primary key for history record';
COMMENT ON COLUMN lead_history.lead_id IS 'Reference to the lead';
COMMENT ON COLUMN lead_history.user_id IS 'User who performed the action';
COMMENT ON COLUMN lead_history.action IS 'Description of the action performed';
COMMENT ON COLUMN lead_history.action_type IS 'Type of action: SYSTEM, USER_ACTION, or WORKFLOW';
COMMENT ON COLUMN lead_history.old_status IS 'Previous lead status';
COMMENT ON COLUMN lead_history.new_status IS 'New lead status';

-- =====================================================================
-- CREATE INDEXES FOR PERFORMANCE
-- =====================================================================

-- Indexes for app_users table
CREATE INDEX idx_users_username ON app_users(username);
CREATE INDEX idx_users_role ON app_users(role);
CREATE INDEX idx_users_active ON app_users(is_active);

-- Indexes for leads table
CREATE INDEX idx_leads_status ON leads(status);
CREATE INDEX idx_leads_assigned_to ON leads(assigned_to);
CREATE INDEX idx_leads_created_date ON leads(created_date);
CREATE INDEX idx_leads_lead_score ON leads(lead_score);
CREATE INDEX idx_leads_potential_value ON leads(potential_value);
CREATE INDEX idx_leads_lead_source ON leads(lead_source);
CREATE INDEX idx_leads_company ON leads(company);
-- Composite index for common queries
CREATE INDEX idx_leads_status_assigned ON leads(status, assigned_to);
CREATE INDEX idx_leads_score_status ON leads(lead_score DESC, status);

-- Indexes for lead_history table
CREATE INDEX idx_history_lead_id ON lead_history(lead_id);
CREATE INDEX idx_history_user_id ON lead_history(user_id);
CREATE INDEX idx_history_timestamp ON lead_history(timestamp);
CREATE INDEX idx_history_action ON lead_history(action);
-- Composite index for lead history queries
CREATE INDEX idx_history_lead_timestamp ON lead_history(lead_id, timestamp DESC);

-- =====================================================================
-- CREATE VIEWS FOR COMMON QUERIES
-- =====================================================================

-- View for sales person's assigned leads
CREATE VIEW v_my_leads AS
SELECT 
    l.lead_id,
    l.lead_name,
    l.company,
    l.email,
    l.phone,
    l.status,
    l.potential_value,
    l.lead_source,
    l.lead_score,
    l.created_date,
    l.updated_date,
    l.description,
    l.industry,
    l.company_size,
    l.location,
    u.username as assigned_username,
    u.first_name as assigned_first_name,
    u.last_name as assigned_last_name
FROM leads l
LEFT JOIN app_users u ON l.assigned_to = u.user_id
WHERE l.status IN ('ASSIGNED', 'IN_PROGRESS', 'PRE_CONVERSION')
ORDER BY l.lead_score DESC, l.created_date DESC;

COMMENT ON VIEW v_my_leads IS 'View for sales persons to see their assigned leads with user details';

-- View for new leads (unassigned)
CREATE VIEW v_new_leads AS
SELECT 
    l.lead_id,
    l.lead_name,
    l.company,
    l.email,
    l.phone,
    l.status,
    l.potential_value,
    l.lead_source,
    l.lead_score,
    l.created_date,
    l.description,
    l.industry,
    l.company_size,
    l.location
FROM leads l
WHERE l.status = 'NEW'
ORDER BY l.lead_score DESC, l.created_date DESC;

COMMENT ON VIEW v_new_leads IS 'View for managers to see new unassigned leads';

-- View for high-value leads (potential value >= $1M)
CREATE VIEW v_high_value_leads AS
SELECT 
    l.lead_id,
    l.lead_name,
    l.company,
    l.email,
    l.phone,
    l.status,
    l.potential_value,
    l.lead_source,
    l.lead_score,
    l.created_date,
    l.updated_date,
    l.description,
    l.industry,
    l.company_size,
    l.location,
    u.username as assigned_username,
    u.first_name as assigned_first_name,
    u.last_name as assigned_last_name
FROM leads l
LEFT JOIN app_users u ON l.assigned_to = u.user_id
WHERE l.potential_value >= 1000000
ORDER BY l.potential_value DESC, l.lead_score DESC;

COMMENT ON VIEW v_high_value_leads IS 'View for high-value leads requiring special handling';

-- =====================================================================
-- CREATE TRIGGERS FOR AUDIT TRAIL
-- =====================================================================

-- Function to update px_updated_datetime
CREATE OR REPLACE FUNCTION update_updated_datetime()
RETURNS TRIGGER AS $$
BEGIN
    NEW.px_updated_datetime = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to update px_updated_datetime on app_users
CREATE TRIGGER trg_users_update
    BEFORE UPDATE ON app_users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_datetime();

-- Trigger to update px_updated_datetime on leads
CREATE TRIGGER trg_leads_update
    BEFORE UPDATE ON leads
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_datetime();

-- Trigger to update px_updated_datetime on lead_history
CREATE TRIGGER trg_history_update
    BEFORE UPDATE ON lead_history
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_datetime();

-- =====================================================================
-- SAMPLE DATA FOR TESTING
-- =====================================================================

-- Insert sample users
INSERT INTO app_users (user_id, username, password, role, first_name, last_name, email, phone, is_active, px_created_by) 
VALUES (nextval('app_users_seq'), 'manager', '$2a$10$H.V2q.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T', 'SALES_MANAGER', 'John', 'Manager', 'manager@mig.com', '555-0101', TRUE, 'SYSTEM');

INSERT INTO app_users (user_id, username, password, role, first_name, last_name, email, phone, is_active, px_created_by) 
VALUES (nextval('app_users_seq'), 'sales1', '$2a$10$H.V2q.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T', 'SALES_PERSON', 'Jane', 'Smith', 'jane.smith@mig.com', '555-0102', TRUE, 'SYSTEM');

INSERT INTO app_users (user_id, username, password, role, first_name, last_name, email, phone, is_active, px_created_by) 
VALUES (nextval('app_users_seq'), 'sales2', '$2a$10$H.V2q.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T', 'SALES_PERSON', 'Bob', 'Johnson', 'bob.johnson@mig.com', '555-0103', TRUE, 'SYSTEM');

-- Insert sample leads
INSERT INTO leads (lead_id, lead_name, company, email, phone, status, potential_value, lead_source, lead_score, description, industry, company_size, location, px_created_by) 
VALUES (nextval('leads_seq'), 'John Smith', 'ABC Corp', 'john.smith@abccorp.com', '123-456-7890', 'NEW', 50000, 'Cold Call', 20, 'Small business owner interested in investment services', 'Manufacturing', 'Small', 'New York, NY', 'SYSTEM');

INSERT INTO leads (lead_id, lead_name, company, email, phone, status, potential_value, lead_source, lead_score, description, industry, company_size, location, px_created_by) 
VALUES (nextval('leads_seq'), 'Jane Doe', 'XYZ Inc', 'jane.doe@xyzinc.com', '098-765-4321', 'NEW', 1500000, 'Partner Referral', 95, 'Large enterprise looking for comprehensive investment portfolio management', 'Technology', 'Large', 'San Francisco, CA', 'SYSTEM');

INSERT INTO leads (lead_id, lead_name, company, email, phone, status, potential_value, lead_source, lead_score, description, industry, company_size, location, px_created_by) 
VALUES (nextval('leads_seq'), 'Mike Wilson', 'TechStart LLC', 'mike@techstart.com', '555-123-4567', 'NEW', 250000, 'Webinar', 45, 'Startup founder seeking seed funding and investment advice', 'Technology', 'Small', 'Austin, TX', 'SYSTEM');

-- Insert sample lead history
INSERT INTO lead_history (history_id, lead_id, user_id, comment_text, action, action_type, old_status, new_status, px_created_by) 
VALUES (nextval('lead_history_seq'), 1, 1, 'Lead created from cold call', 'Created', 'SYSTEM', NULL, 'NEW', 'SYSTEM');

INSERT INTO lead_history (history_id, lead_id, user_id, comment_text, action, action_type, old_status, new_status, px_created_by) 
VALUES (nextval('lead_history_seq'), 2, 1, 'High-value lead from partner referral', 'Created', 'SYSTEM', NULL, 'NEW', 'SYSTEM');

INSERT INTO lead_history (history_id, lead_id, user_id, comment_text, action, action_type, old_status, new_status, px_created_by) 
VALUES (nextval('lead_history_seq'), 3, 1, 'Lead created from webinar attendance', 'Created', 'SYSTEM', NULL, 'NEW', 'SYSTEM');

-- =====================================================================
-- GRANT PERMISSIONS (Adjust as needed for your environment)
-- =====================================================================

-- Grant permissions to the application user
-- GRANT SELECT, INSERT, UPDATE, DELETE ON app_users TO lead_mgmt_user;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON leads TO lead_mgmt_user;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON lead_history TO lead_mgmt_user;
-- GRANT SELECT ON v_my_leads TO lead_mgmt_user;
-- GRANT SELECT ON v_new_leads TO lead_mgmt_user;
-- GRANT SELECT ON v_high_value_leads TO lead_mgmt_user;

-- =====================================================================
-- END OF DDL
-- =====================================================================
