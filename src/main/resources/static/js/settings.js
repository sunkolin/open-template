let currentUser = null;

// 页面加载时检查登录状态并加载用户信息
window.addEventListener('load', function() {
    checkLoginAndLoadUser();
});

// 获取Token
function getToken() {
    const userInfo = localStorage.getItem('userInfo');
    if (userInfo) {
        try {
            const user = JSON.parse(userInfo);
            return user.token || null;
        } catch (e) {
            console.error('解析用户信息失败:', e);
        }
    }
    return null;
}

// 检查登录并加载用户信息
function checkLoginAndLoadUser() {
    const token = getToken();
    if (!token) {
        window.location.href = '/login.html';
        return;
    }
    
    // 直接调用需要认证的接口，如果未登录会被拦截器拦截
    loadUserInfo();
}

// 加载用户信息
function loadUserInfo() {
    const token = getToken();
    if (!token) {
        window.location.href = '/login.html';
        return;
    }
    
    fetch('/getUser', {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 0 && data.data) {
                currentUser = data.data;
                
                // 显示用户名
                const nickName = currentUser.nickName || currentUser.username;
                document.getElementById('userName').innerHTML = 
                    '<i class="bi bi-person-circle"></i> ' + nickName;
                
                // 填充表单
                document.getElementById('username').value = currentUser.username;
                document.getElementById('nickName').value = currentUser.nickName || '';
                document.getElementById('email').value = currentUser.email || '';
            }
        })
        .catch(error => {
            console.error('加载用户信息失败:', error);
        });
}

// 隐藏所有提示
function hideAlerts() {
    document.getElementById('successAlert').style.display = 'none';
    document.getElementById('errorAlert').style.display = 'none';
}

// 显示成功信息
function showSuccess(message) {
    hideAlerts();
    const alert = document.getElementById('successAlert');
    alert.textContent = message;
    alert.style.display = 'block';
    
    // 3秒后自动隐藏
    setTimeout(() => {
        alert.style.display = 'none';
    }, 3000);
}

// 显示错误信息
function showError(message) {
    hideAlerts();
    const alert = document.getElementById('errorAlert');
    alert.textContent = message;
    alert.style.display = 'block';
}

// 更新个人信息
document.getElementById('profileForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const nickName = document.getElementById('nickName').value.trim();
    const email = document.getElementById('email').value.trim();
    
    if (!nickName || !email) {
        showError('请填写所有必填字段');
        return;
    }

    const token = getToken();
    if (!token) {
        showError('请先登录');
        setTimeout(() => {
            window.location.href = '/login.html';
        }, 1000);
        return;
    }

    fetch('/personalInformation/profile', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({
            nickName: nickName,
            email: email
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.code === 0) {
            showSuccess('个人信息更新成功');
            // 重新加载用户信息
            loadUserInfo();
        } else {
            showError(data.message || '更新失败');
        }
    })
    .catch(error => {
        console.error('更新失败:', error);
        showError('网络错误，请重试');
    });
});

// 修改密码
document.getElementById('passwordForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const oldPassword = document.getElementById('oldPassword').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    if (!oldPassword || !newPassword || !confirmPassword) {
        showError('请填写所有密码字段');
        return;
    }

    if (newPassword !== confirmPassword) {
        showError('两次输入的新密码不一致');
        return;
    }

    if (newPassword.length < 6) {
        showError('新密码长度不能少于6位');
        return;
    }

    const token = getToken();
    if (!token) {
        showError('请先登录');
        setTimeout(() => {
            window.location.href = '/login.html';
        }, 1000);
        return;
    }

    fetch('/personalInformation/password', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({
            oldPassword: oldPassword,
            newPassword: newPassword,
            confirmPassword: confirmPassword
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.code === 0) {
            showSuccess('密码修改成功，请使用新密码重新登录');
            // 清空表单
            document.getElementById('passwordForm').reset();
            
            // 3秒后跳转到登录页
            setTimeout(() => {
                logout();
            }, 3000);
        } else {
            showError(data.message || '修改失败');
        }
    })
    .catch(error => {
        console.error('修改失败:', error);
        showError('网络错误，请重试');
    });
});

// 退出登录
function logout() {
    if (!confirm('确定要退出登录吗？')) {
        return;
    }
    
    const token = getToken();
    fetch('/logout', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => response.json())
    .then(data => {
        // 无论成功与否，都清除本地存储并跳转
        localStorage.removeItem('userInfo');
        window.location.href = '/login.html';
    })
    .catch(error => {
        console.error('退出登录失败:', error);
        // 即使网络错误，也清除本地存储并跳转
        localStorage.removeItem('userInfo');
        window.location.href = '/login.html';
    });
}
