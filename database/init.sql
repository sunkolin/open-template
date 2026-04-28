-- 创建用户表
CREATE TABLE IF NOT EXISTS t_user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mobile VARCHAR(20),
    password VARCHAR(100) NOT NULL,
    nick_name VARCHAR(50),
    user_name VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(50),
    email VARCHAR(100),
    sex INTEGER DEFAULT 0,
    birthday DATE,
    remark TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    modify_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 插入默认管理员账户（密码: admin123，MD5加密后: 0192023a7bbd73250516f069df18b500）
INSERT OR IGNORE INTO t_user (user_name, password, nick_name, email, create_time, modify_time) 
VALUES ('admin', '0192023a7bbd73250516f069df18b500', '管理员', 'admin@mailman.com', datetime('now'), datetime('now'));

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_user_name ON t_user(user_name);
CREATE INDEX IF NOT EXISTS idx_email ON t_user(email);

-- 创建发件人表
CREATE TABLE IF NOT EXISTS t_sender (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sender_email VARCHAR(100) NOT NULL UNIQUE,
    email_password VARCHAR(200),
    auth_code VARCHAR(200),
    status INTEGER DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    modify_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_sender_email ON t_sender(sender_email);
CREATE INDEX IF NOT EXISTS idx_sender_status ON t_sender(status);

-- 创建收件人表
CREATE TABLE IF NOT EXISTS t_receiver (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    receiver VARCHAR(100) NOT NULL UNIQUE,
    status INTEGER DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_receiver ON t_receiver(receiver);
CREATE INDEX IF NOT EXISTS idx_receiver_status ON t_receiver(status);

-- 创建邮件推送任务表
CREATE TABLE IF NOT EXISTS t_email_push_task (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_name VARCHAR(100) NOT NULL,
    email_title VARCHAR(200) NOT NULL,
    email_content TEXT,
    sender_type INTEGER DEFAULT 1,
    sender_id INTEGER,
    sender_email VARCHAR(100),
    sender_password VARCHAR(100),
    receiver_type INTEGER DEFAULT 1,
    receiver_ids TEXT,
    receiver_emails TEXT,
    status INTEGER DEFAULT 0,
    total_emails INTEGER DEFAULT 0,
    success_count INTEGER DEFAULT 0,
    fail_count INTEGER DEFAULT 0,
    create_user_id INTEGER,
    create_user_name VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    modify_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    start_time DATETIME,
    end_time DATETIME
);

CREATE INDEX IF NOT EXISTS idx_task_status ON t_email_push_task(status);
CREATE INDEX IF NOT EXISTS idx_create_user ON t_email_push_task(create_user_id);

-- 创建邮件发送记录表
CREATE TABLE IF NOT EXISTS t_email_send_record (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    sender_email VARCHAR(100) NOT NULL,
    receiver_email VARCHAR(100) NOT NULL,
    email_title VARCHAR(200),
    email_content TEXT,
    send_status INTEGER DEFAULT 0,
    fail_reason TEXT,
    retry_count INTEGER DEFAULT 0,
    send_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    modify_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_record_task ON t_email_send_record(task_id);
CREATE INDEX IF NOT EXISTS idx_record_status ON t_email_send_record(send_status);
