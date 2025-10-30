-- =====================================================================
-- Enhanced Oracle DDL for Pega Integration - Lead Management System
-- =====================================================================
-- This DDL script is optimized for Pega Blueprint integration
-- Includes performance indexes, views, and metadata for data class mapping

-- Drop existing objects if they exist, to allow for a clean setup
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE LEAD_HISTORY CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE LEADS CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE APP_USERS CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

-- Drop sequences
BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE APP_USERS_SEQ';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2289 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE LEADS_SEQ';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2289 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE LEAD_HISTORY_SEQ';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2289 THEN
         RAISE;
      END IF;
END;
/

-- Drop views
BEGIN
   EXECUTE IMMEDIATE 'DROP VIEW V_MY_LEADS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP VIEW V_NEW_LEADS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP VIEW V_HIGH_VALUE_LEADS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

-- =====================================================================
-- CREATE SEQUENCES
-- =====================================================================

CREATE SEQUENCE APP_USERS_SEQ START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE LEADS_SEQ START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE LEAD_HISTORY_SEQ START WITH 1 INCREMENT BY 1 NOCACHE;

-- =====================================================================
-- CREATE TABLES
-- =====================================================================

-- Users Table: Stores login information and roles for all system users
-- Pega Data Class: Data-User
CREATE TABLE APP_USERS (
    USER_ID NUMBER PRIMARY KEY,
    USERNAME VARCHAR2(50) NOT NULL UNIQUE,
    PASSWORD VARCHAR2(255) NOT NULL, -- Hashed password using BCrypt
    ROLE VARCHAR2(20) NOT NULL CHECK (ROLE IN ('SALES_PERSON', 'SALES_MANAGER')),
    FIRST_NAME VARCHAR2(50),
    LAST_NAME VARCHAR2(50),
    EMAIL VARCHAR2(100),
    PHONE VARCHAR2(20),
    IS_ACTIVE CHAR(1) DEFAULT 'Y' CHECK (IS_ACTIVE IN ('Y', 'N')),
    CREATED_DATE DATE DEFAULT SYSDATE,
    LAST_LOGIN_DATE DATE,
    -- Audit fields for Pega
    PX_CREATED_DATETIME DATE DEFAULT SYSDATE,
    PX_UPDATED_DATETIME DATE DEFAULT SYSDATE,
    PX_CREATED_BY VARCHAR2(50),
    PX_UPDATED_BY VARCHAR2(50)
);

COMMENT ON TABLE APP_USERS IS 'User accounts for the lead management system. Maps to Pega Data-User class.';
COMMENT ON COLUMN APP_USERS.USER_ID IS 'Primary key for user identification';
COMMENT ON COLUMN APP_USERS.USERNAME IS 'Unique username for login';
COMMENT ON COLUMN APP_USERS.PASSWORD IS 'BCrypt hashed password';
COMMENT ON COLUMN APP_USERS.ROLE IS 'User role: SALES_PERSON or SALES_MANAGER';
COMMENT ON COLUMN APP_USERS.IS_ACTIVE IS 'User account status';
COMMENT ON COLUMN APP_USERS.PX_CREATED_DATETIME IS 'Pega audit field - creation timestamp';
COMMENT ON COLUMN APP_USERS.PX_UPDATED_DATETIME IS 'Pega audit field - last update timestamp';

-- Leads Table: Stores information about potential clients (leads)
-- Pega Data Class: Data-Lead
CREATE TABLE LEADS (
    LEAD_ID NUMBER PRIMARY KEY,
    LEAD_NAME VARCHAR2(100) NOT NULL,
    COMPANY VARCHAR2(100),
    EMAIL VARCHAR2(100),
    PHONE VARCHAR2(20),
    STATUS VARCHAR2(20) NOT NULL CHECK (STATUS IN ('NEW', 'ASSIGNED', 'IN_PROGRESS', 'PRE_CONVERSION', 'CONVERTED', 'REJECTED')),
    ASSIGNED_TO NUMBER,
    POTENTIAL_VALUE NUMBER(12, 2),
    LEAD_SOURCE VARCHAR2(50) CHECK (LEAD_SOURCE IN ('Partner Referral', 'Webinar', 'Website Signup', 'Cold Call')),
    LEAD_SCORE NUMBER(3) CHECK (LEAD_SCORE BETWEEN 0 AND 100),
    CREATED_DATE DATE DEFAULT SYSDATE,
    UPDATED_DATE DATE DEFAULT SYSDATE,
    -- Additional fields for Pega integration
    DESCRIPTION CLOB,
    INDUSTRY VARCHAR2(50),
    COMPANY_SIZE VARCHAR2(20),
    LOCATION VARCHAR2(100),
    -- Audit fields for Pega
    PX_CREATED_DATETIME DATE DEFAULT SYSDATE,
    PX_UPDATED_DATETIME DATE DEFAULT SYSDATE,
    PX_CREATED_BY VARCHAR2(50),
    PX_UPDATED_BY VARCHAR2(50),
    -- Foreign key constraint
    FOREIGN KEY (ASSIGNED_TO) REFERENCES APP_USERS(USER_ID)
);

COMMENT ON TABLE LEADS IS 'Lead information for potential clients. Maps to Pega Data-Lead class.';
COMMENT ON COLUMN LEADS.LEAD_ID IS 'Primary key for lead identification';
COMMENT ON COLUMN LEADS.LEAD_NAME IS 'Full name of the lead contact';
COMMENT ON COLUMN LEADS.COMPANY IS 'Company name of the lead';
COMMENT ON COLUMN LEADS.STATUS IS 'Current status in the sales pipeline';
COMMENT ON COLUMN LEADS.ASSIGNED_TO IS 'User ID of assigned sales person';
COMMENT ON COLUMN LEADS.POTENTIAL_VALUE IS 'Estimated deal value in USD';
COMMENT ON COLUMN LEADS.LEAD_SOURCE IS 'Source of the lead';
COMMENT ON COLUMN LEADS.LEAD_SCORE IS 'Calculated lead score (0-100)';
COMMENT ON COLUMN LEADS.PX_CREATED_DATETIME IS 'Pega audit field - creation timestamp';
COMMENT ON COLUMN LEADS.PX_UPDATED_DATETIME IS 'Pega audit field - last update timestamp';

-- Lead History Table: A log of all actions and comments related to a lead
-- Pega Data Class: Data-LeadHistory
CREATE TABLE LEAD_HISTORY (
    HISTORY_ID NUMBER PRIMARY KEY,
    LEAD_ID NUMBER NOT NULL,
    USER_ID NUMBER NOT NULL,
    COMMENT_TEXT VARCHAR2(4000),
    ACTION VARCHAR2(100), -- e.g., 'Created', 'Assigned', 'Comment Added', 'Escalated', 'Approved', 'Rejected'
    TIMESTAMP DATE DEFAULT SYSDATE,
    -- Additional fields for Pega
    ACTION_TYPE VARCHAR2(50), -- 'SYSTEM', 'USER_ACTION', 'WORKFLOW'
    OLD_STATUS VARCHAR2(20),
    NEW_STATUS VARCHAR2(20),
    -- Audit fields for Pega
    PX_CREATED_DATETIME DATE DEFAULT SYSDATE,
    PX_UPDATED_DATETIME DATE DEFAULT SYSDATE,
    PX_CREATED_BY VARCHAR2(50),
    PX_UPDATED_BY VARCHAR2(50),
    -- Foreign key constraints
    FOREIGN KEY (LEAD_ID) REFERENCES LEADS(LEAD_ID),
    FOREIGN KEY (USER_ID) REFERENCES APP_USERS(USER_ID)
);

COMMENT ON TABLE LEAD_HISTORY IS 'Audit trail for lead activities and status changes. Maps to Pega Data-LeadHistory class.';
COMMENT ON COLUMN LEAD_HISTORY.HISTORY_ID IS 'Primary key for history record';
COMMENT ON COLUMN LEAD_HISTORY.LEAD_ID IS 'Reference to the lead';
COMMENT ON COLUMN LEAD_HISTORY.USER_ID IS 'User who performed the action';
COMMENT ON COLUMN LEAD_HISTORY.ACTION IS 'Description of the action performed';
COMMENT ON COLUMN LEAD_HISTORY.ACTION_TYPE IS 'Type of action: SYSTEM, USER_ACTION, or WORKFLOW';
COMMENT ON COLUMN LEAD_HISTORY.OLD_STATUS IS 'Previous lead status';
COMMENT ON COLUMN LEAD_HISTORY.NEW_STATUS IS 'New lead status';

-- =====================================================================
-- CREATE INDEXES FOR PERFORMANCE
-- =====================================================================

-- Indexes for APP_USERS table
CREATE INDEX IDX_USERS_USERNAME ON APP_USERS(USERNAME);
CREATE INDEX IDX_USERS_ROLE ON APP_USERS(ROLE);
CREATE INDEX IDX_USERS_ACTIVE ON APP_USERS(IS_ACTIVE);

-- Indexes for LEADS table
CREATE INDEX IDX_LEADS_STATUS ON LEADS(STATUS);
CREATE INDEX IDX_LEADS_ASSIGNED_TO ON LEADS(ASSIGNED_TO);
CREATE INDEX IDX_LEADS_CREATED_DATE ON LEADS(CREATED_DATE);
CREATE INDEX IDX_LEADS_LEAD_SCORE ON LEADS(LEAD_SCORE);
CREATE INDEX IDX_LEADS_POTENTIAL_VALUE ON LEADS(POTENTIAL_VALUE);
CREATE INDEX IDX_LEADS_LEAD_SOURCE ON LEADS(LEAD_SOURCE);
CREATE INDEX IDX_LEADS_COMPANY ON LEADS(COMPANY);
-- Composite index for common queries
CREATE INDEX IDX_LEADS_STATUS_ASSIGNED ON LEADS(STATUS, ASSIGNED_TO);
CREATE INDEX IDX_LEADS_SCORE_STATUS ON LEADS(LEAD_SCORE DESC, STATUS);

-- Indexes for LEAD_HISTORY table
CREATE INDEX IDX_HISTORY_LEAD_ID ON LEAD_HISTORY(LEAD_ID);
CREATE INDEX IDX_HISTORY_USER_ID ON LEAD_HISTORY(USER_ID);
CREATE INDEX IDX_HISTORY_TIMESTAMP ON LEAD_HISTORY(TIMESTAMP);
CREATE INDEX IDX_HISTORY_ACTION ON LEAD_HISTORY(ACTION);
-- Composite index for lead history queries
CREATE INDEX IDX_HISTORY_LEAD_TIMESTAMP ON LEAD_HISTORY(LEAD_ID, TIMESTAMP DESC);

-- =====================================================================
-- CREATE VIEWS FOR COMMON QUERIES
-- =====================================================================

-- View for sales person's assigned leads
CREATE VIEW V_MY_LEADS AS
SELECT 
    l.LEAD_ID,
    l.LEAD_NAME,
    l.COMPANY,
    l.EMAIL,
    l.PHONE,
    l.STATUS,
    l.POTENTIAL_VALUE,
    l.LEAD_SOURCE,
    l.LEAD_SCORE,
    l.CREATED_DATE,
    l.UPDATED_DATE,
    l.DESCRIPTION,
    l.INDUSTRY,
    l.COMPANY_SIZE,
    l.LOCATION,
    u.USERNAME as ASSIGNED_USERNAME,
    u.FIRST_NAME as ASSIGNED_FIRST_NAME,
    u.LAST_NAME as ASSIGNED_LAST_NAME
FROM LEADS l
LEFT JOIN APP_USERS u ON l.ASSIGNED_TO = u.USER_ID
WHERE l.STATUS IN ('ASSIGNED', 'IN_PROGRESS', 'PRE_CONVERSION')
ORDER BY l.LEAD_SCORE DESC, l.CREATED_DATE DESC;

COMMENT ON VIEW V_MY_LEADS IS 'View for sales persons to see their assigned leads with user details';

-- View for new leads (unassigned)
CREATE VIEW V_NEW_LEADS AS
SELECT 
    l.LEAD_ID,
    l.LEAD_NAME,
    l.COMPANY,
    l.EMAIL,
    l.PHONE,
    l.STATUS,
    l.POTENTIAL_VALUE,
    l.LEAD_SOURCE,
    l.LEAD_SCORE,
    l.CREATED_DATE,
    l.DESCRIPTION,
    l.INDUSTRY,
    l.COMPANY_SIZE,
    l.LOCATION
FROM LEADS l
WHERE l.STATUS = 'NEW'
ORDER BY l.LEAD_SCORE DESC, l.CREATED_DATE DESC;

COMMENT ON VIEW V_NEW_LEADS IS 'View for managers to see new unassigned leads';

-- View for high-value leads (potential value >= $1M)
CREATE VIEW V_HIGH_VALUE_LEADS AS
SELECT 
    l.LEAD_ID,
    l.LEAD_NAME,
    l.COMPANY,
    l.EMAIL,
    l.PHONE,
    l.STATUS,
    l.POTENTIAL_VALUE,
    l.LEAD_SOURCE,
    l.LEAD_SCORE,
    l.CREATED_DATE,
    l.UPDATED_DATE,
    l.DESCRIPTION,
    l.INDUSTRY,
    l.COMPANY_SIZE,
    l.LOCATION,
    u.USERNAME as ASSIGNED_USERNAME,
    u.FIRST_NAME as ASSIGNED_FIRST_NAME,
    u.LAST_NAME as ASSIGNED_LAST_NAME
FROM LEADS l
LEFT JOIN APP_USERS u ON l.ASSIGNED_TO = u.USER_ID
WHERE l.POTENTIAL_VALUE >= 1000000
ORDER BY l.POTENTIAL_VALUE DESC, l.LEAD_SCORE DESC;

COMMENT ON VIEW V_HIGH_VALUE_LEADS IS 'View for high-value leads requiring special handling';

-- =====================================================================
-- CREATE TRIGGERS FOR AUDIT TRAIL
-- =====================================================================

-- Trigger to update PX_UPDATED_DATETIME on APP_USERS
CREATE OR REPLACE TRIGGER TRG_USERS_UPDATE
    BEFORE UPDATE ON APP_USERS
    FOR EACH ROW
BEGIN
    :NEW.PX_UPDATED_DATETIME := SYSDATE;
END;
/

-- Trigger to update PX_UPDATED_DATETIME on LEADS
CREATE OR REPLACE TRIGGER TRG_LEADS_UPDATE
    BEFORE UPDATE ON LEADS
    FOR EACH ROW
BEGIN
    :NEW.PX_UPDATED_DATETIME := SYSDATE;
    :NEW.UPDATED_DATE := SYSDATE;
END;
/

-- Trigger to update PX_UPDATED_DATETIME on LEAD_HISTORY
CREATE OR REPLACE TRIGGER TRG_HISTORY_UPDATE
    BEFORE UPDATE ON LEAD_HISTORY
    FOR EACH ROW
BEGIN
    :NEW.PX_UPDATED_DATETIME := SYSDATE;
END;
/

-- =====================================================================
-- SAMPLE DATA FOR TESTING
-- =====================================================================

-- Insert sample users
INSERT INTO APP_USERS (USER_ID, USERNAME, PASSWORD, ROLE, FIRST_NAME, LAST_NAME, EMAIL, PHONE, PX_CREATED_BY) 
VALUES (APP_USERS_SEQ.NEXTVAL, 'manager', '$2a$10$H.V2q.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T', 'SALES_MANAGER', 'John', 'Manager', 'manager@mig.com', '555-0101', 'SYSTEM');

INSERT INTO APP_USERS (USER_ID, USERNAME, PASSWORD, ROLE, FIRST_NAME, LAST_NAME, EMAIL, PHONE, PX_CREATED_BY) 
VALUES (APP_USERS_SEQ.NEXTVAL, 'sales1', '$2a$10$H.V2q.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T', 'SALES_PERSON', 'Jane', 'Smith', 'jane.smith@mig.com', '555-0102', 'SYSTEM');

INSERT INTO APP_USERS (USER_ID, USERNAME, PASSWORD, ROLE, FIRST_NAME, LAST_NAME, EMAIL, PHONE, PX_CREATED_BY) 
VALUES (APP_USERS_SEQ.NEXTVAL, 'sales2', '$2a$10$H.V2q.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T.T', 'SALES_PERSON', 'Bob', 'Johnson', 'bob.johnson@mig.com', '555-0103', 'SYSTEM');

-- Insert sample leads
INSERT INTO LEADS (LEAD_ID, LEAD_NAME, COMPANY, EMAIL, PHONE, STATUS, POTENTIAL_VALUE, LEAD_SOURCE, LEAD_SCORE, DESCRIPTION, INDUSTRY, COMPANY_SIZE, LOCATION, PX_CREATED_BY) 
VALUES (LEADS_SEQ.NEXTVAL, 'John Smith', 'ABC Corp', 'john.smith@abccorp.com', '123-456-7890', 'NEW', 50000, 'Cold Call', 20, 'Small business owner interested in investment services', 'Manufacturing', 'Small', 'New York, NY', 'SYSTEM');

INSERT INTO LEADS (LEAD_ID, LEAD_NAME, COMPANY, EMAIL, PHONE, STATUS, POTENTIAL_VALUE, LEAD_SOURCE, LEAD_SCORE, DESCRIPTION, INDUSTRY, COMPANY_SIZE, LOCATION, PX_CREATED_BY) 
VALUES (LEADS_SEQ.NEXTVAL, 'Jane Doe', 'XYZ Inc', 'jane.doe@xyzinc.com', '098-765-4321', 'NEW', 1500000, 'Partner Referral', 95, 'Large enterprise looking for comprehensive investment portfolio management', 'Technology', 'Large', 'San Francisco, CA', 'SYSTEM');

INSERT INTO LEADS (LEAD_ID, LEAD_NAME, COMPANY, EMAIL, PHONE, STATUS, POTENTIAL_VALUE, LEAD_SOURCE, LEAD_SCORE, DESCRIPTION, INDUSTRY, COMPANY_SIZE, LOCATION, PX_CREATED_BY) 
VALUES (LEADS_SEQ.NEXTVAL, 'Mike Wilson', 'TechStart LLC', 'mike@techstart.com', '555-123-4567', 'NEW', 250000, 'Webinar', 45, 'Startup founder seeking seed funding and investment advice', 'Technology', 'Small', 'Austin, TX', 'SYSTEM');

-- Insert sample lead history
INSERT INTO LEAD_HISTORY (HISTORY_ID, LEAD_ID, USER_ID, COMMENT_TEXT, ACTION, ACTION_TYPE, OLD_STATUS, NEW_STATUS, PX_CREATED_BY) 
VALUES (LEAD_HISTORY_SEQ.NEXTVAL, 1, 1, 'Lead created from cold call', 'Created', 'SYSTEM', NULL, 'NEW', 'SYSTEM');

INSERT INTO LEAD_HISTORY (HISTORY_ID, LEAD_ID, USER_ID, COMMENT_TEXT, ACTION, ACTION_TYPE, OLD_STATUS, NEW_STATUS, PX_CREATED_BY) 
VALUES (LEAD_HISTORY_SEQ.NEXTVAL, 2, 1, 'High-value lead from partner referral', 'Created', 'SYSTEM', NULL, 'NEW', 'SYSTEM');

INSERT INTO LEAD_HISTORY (HISTORY_ID, LEAD_ID, USER_ID, COMMENT_TEXT, ACTION, ACTION_TYPE, OLD_STATUS, NEW_STATUS, PX_CREATED_BY) 
VALUES (LEAD_HISTORY_SEQ.NEXTVAL, 3, 1, 'Lead created from webinar attendance', 'Created', 'SYSTEM', NULL, 'NEW', 'SYSTEM');

COMMIT;

-- =====================================================================
-- GRANT PERMISSIONS (Adjust as needed for your environment)
-- =====================================================================

-- Grant permissions to the application user
-- GRANT SELECT, INSERT, UPDATE, DELETE ON APP_USERS TO lead_mgmt_user;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON LEADS TO lead_mgmt_user;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON LEAD_HISTORY TO lead_mgmt_user;
-- GRANT SELECT ON V_MY_LEADS TO lead_mgmt_user;
-- GRANT SELECT ON V_NEW_LEADS TO lead_mgmt_user;
-- GRANT SELECT ON V_HIGH_VALUE_LEADS TO lead_mgmt_user;

-- =====================================================================
-- END OF DDL
-- =====================================================================

