-- 创建用户表
CREATE TABLE IF NOT EXISTS t_user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mobile VARCHAR(20),
    password VARCHAR(100) NOT NULL,
    nick_name VARCHAR(50),
    user_name VARCHAR(50),
    full_name VARCHAR(50),
    email VARCHAR(100) NOT NULL,
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
