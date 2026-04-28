// 页面加载时检查是否已登录
window.addEventListener('load', function() {
    checkLoginStatus();
});

// 检查登录状态
function checkLoginStatus() {
    const userInfo = localStorage.getItem('userInfo');
    if (!userInfo) {
        return;
    }
    
    try {
        const user = JSON.parse(userInfo);
        if (!user.token) {
            return;
        }
        
        // 直接调用需要认证的接口来检查登录状态
        fetch('/getUser', {
            headers: {
                'Authorization': 'Bearer ' + user.token
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 0 && data.data) {
                // 已登录，跳转到主页
                window.location.href = '/index.html';
            }
        })
        .catch(error => {
            console.error('检查登录状态失败:', error);
        });
    } catch (e) {
        console.error('解析用户信息失败:', e);
    }
}

// 登录表单提交
document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    const rememberMe = document.getElementById('rememberMe').checked;
    
    // 隐藏之前的提示
    hideAlerts();
    
    // 验证输入
    if (!username || !password) {
        showError('请输入用户名和密码');
        return;
    }
    
    // 显示加载状态
    showLoading(true);
    
    // 发送登录请求
    fetch('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            password: password,
            rememberMe: rememberMe
        })
    })
    .then(response => response.json())
    .then(data => {
        showLoading(false);
        
        if (data.code === 0) {
            // 登录成功
            showSuccess('登录成功，正在跳转...');
            
            // 保存用户信息到localStorage（始终保存，用于维持会话）
            if (data.data) {
                localStorage.setItem('userInfo', JSON.stringify(data.data));
            }
            
            // 延迟跳转到主页
            setTimeout(() => {
                window.location.href = '/index.html';
            }, 1000);
        } else {
            // 登录失败
            showError(data.message || '登录失败，请重试');
        }
    })
    .catch(error => {
        showLoading(false);
        showError('网络错误，请重试');
        console.error('登录失败:', error);
    });
});

// 显示错误信息
function showError(message) {
    const errorAlert = document.getElementById('errorAlert');
    errorAlert.textContent = message;
    errorAlert.style.display = 'block';
}

// 显示成功信息
function showSuccess(message) {
    const successAlert = document.getElementById('successAlert');
    successAlert.textContent = message;
    successAlert.style.display = 'block';
}

// 隐藏所有提示
function hideAlerts() {
    document.getElementById('errorAlert').style.display = 'none';
    document.getElementById('successAlert').style.display = 'none';
}

// 显示/隐藏加载状态
function showLoading(show) {
    const loginBtn = document.getElementById('loginBtn');
    const btnText = loginBtn.querySelector('.btn-text');
    const loading = loginBtn.querySelector('.loading');
    
    if (show) {
        btnText.style.display = 'none';
        loading.style.display = 'inline';
        loginBtn.disabled = true;
    } else {
        btnText.style.display = 'inline';
        loading.style.display = 'none';
        loginBtn.disabled = false;
    }
}

// 回车键提交表单
document.getElementById('password').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        document.getElementById('loginForm').dispatchEvent(new Event('submit'));
    }
});
