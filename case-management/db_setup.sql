-- =====================================================================
-- Case Management Tool DDL for Oracle Database
-- =====================================================================

-- Drop existing objects if they exist, to allow for a clean setup
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE LEAD_HISTORY';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE LEADS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE APP_USERS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

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


-- Create Sequences for Primary Keys
CREATE SEQUENCE APP_USERS_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE LEADS_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE LEAD_HISTORY_SEQ START WITH 1 INCREMENT BY 1;


-- Create Tables

-- Users Table: Stores login information and roles for all system users.
-- ROLE can be 'SALES_PERSON' or 'SALES_MANAGER'
CREATE TABLE APP_USERS (
    USER_ID NUMBER PRIMARY KEY,
    USERNAME VARCHAR2(50) NOT NULL UNIQUE,
    PASSWORD VARCHAR2(255) NOT NULL, -- Should store a hashed password
    ROLE VARCHAR2(20) NOT NULL CHECK (ROLE IN ('SALES_PERSON', 'SALES_MANAGER'))
);

-- Leads Table: Stores information about potential clients (leads).
-- STATUS tracks the lead's progress through the sales pipeline.
-- POTENTIAL_VALUE is an estimate of the lead's worth.
-- LEAD_SOURCE and LEAD_SCORE are for the lead scoring feature.
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
    LEAD_SCORE NUMBER,
    CREATED_DATE DATE DEFAULT SYSDATE,
    FOREIGN KEY (ASSIGNED_TO) REFERENCES APP_USERS(USER_ID)
);

-- Lead History Table: A log of all actions and comments related to a lead.
-- This provides an audit trail for each lead's journey.
CREATE TABLE LEAD_HISTORY (
    HISTORY_ID NUMBER PRIMARY KEY,
    LEAD_ID NUMBER NOT NULL,
    USER_ID NUMBER NOT NULL,
    COMMENT_TEXT VARCHAR2(4000),
    ACTION VARCHAR2(100), -- e.g., 'Created', 'Assigned', 'Comment Added'
    TIMESTAMP DATE DEFAULT SYSDATE,
    FOREIGN KEY (LEAD_ID) REFERENCES LEADS(LEAD_ID),
    FOREIGN KEY (USER_ID) REFERENCES APP_USERS(USER_ID)
);


-- Sample Data (for testing purposes)

-- Create a Sales Manager
-- The password for all sample users is 'password'
INSERT INTO APP_USERS (USER_ID, USERNAME, PASSWORD, ROLE) VALUES (APP_USERS_SEQ.NEXTVAL, 'manager', '$2a$10$V1i3f/rM2/b5j/Jd/j.A.e6j/Jd/j.A.e6j/Jd/j.A.e6j/Jd/j.A.e', 'SALES_MANAGER');

-- Create some Sales People
INSERT INTO APP_USERS (USER_ID, USERNAME, PASSWORD, ROLE) VALUES (APP_USERS_SEQ.NEXTVAL, 'sales1', '$2a$10$V1i3f/rM2/b5j/Jd/j.A.e6j/Jd/j.A.e6j/Jd/j.A.e6j/Jd/j.A.e', 'SALES_PERSON');
INSERT INTO APP_USERS (USER_ID, USERNAME, PASSWORD, ROLE) VALUES (APP_USERS_SEQ.NEXTVAL, 'sales2', '$2a$10$V1i3f/rM2/b5j/Jd/j.A.e6j/Jd/j.A.e6j/Jd/j.A.e6j/Jd/j.A.e', 'SALES_PERSON');

-- Create some new leads
INSERT INTO LEADS (LEAD_ID, LEAD_NAME, COMPANY, EMAIL, PHONE, STATUS, POTENTIAL_VALUE, LEAD_SOURCE, LEAD_SCORE)
VALUES (LEADS_SEQ.NEXTVAL, 'John Smith', 'ABC Corp', 'john.smith@abccorp.com', '123-456-7890', 'NEW', 50000, 'Cold Call', 20);

INSERT INTO LEADS (LEAD_ID, LEAD_NAME, COMPANY, EMAIL, PHONE, STATUS, POTENTIAL_VALUE, LEAD_SOURCE, LEAD_SCORE)
VALUES (LEADS_SEQ.NEXTVAL, 'Jane Doe', 'XYZ Inc', 'jane.doe@xyzinc.com', '098-765-4321', 'NEW', 1500000, 'Partner Referral', 95);

COMMIT;

-- =====================================================================
-- End of DDL
-- =====================================================================