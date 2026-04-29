// 自定义提示框函数 - 无动画、无延迟
function showAlert(message, callback) {
    const overlay = document.getElementById('customAlertOverlay');
    const messageEl = document.getElementById('customAlertMessage');
    const okBtn = document.getElementById('customAlertOkBtn');
    const cancelBtn = document.getElementById('customAlertCancelBtn');
    
    messageEl.textContent = message;
    cancelBtn.style.display = 'none';
    overlay.style.display = 'block';
    
    function closeAlert() {
        overlay.style.display = 'none';
        if (callback) callback();
    }
    
    okBtn.onclick = closeAlert;
}

// 自定义确认框函数 - 无动画、无延迟
function showConfirm(message, callback) {
    const overlay = document.getElementById('customAlertOverlay');
    const messageEl = document.getElementById('customAlertMessage');
    const okBtn = document.getElementById('customAlertOkBtn');
    const cancelBtn = document.getElementById('customAlertCancelBtn');
    
    messageEl.textContent = message;
    cancelBtn.style.display = 'inline-block';
    overlay.style.display = 'block';
    
    function closeAlert(result) {
        overlay.style.display = 'none';
        if (callback) callback(result);
    }
    
    okBtn.onclick = function() {
        closeAlert(true);
    };
    
    cancelBtn.onclick = function() {
        closeAlert(false);
    };
}

// 重写原生alert和confirm
window.alert = showAlert;
window.confirm = showConfirm;

// 页面加载时检查登录状态
window.addEventListener('load', function() {
    checkLoginAndLoadUser();
});

// 获取Token
function getToken() {
    return localStorage.getItem('token');
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
                const nickName = data.data.nickName || data.data.username;
                document.getElementById('userName').innerHTML = 
                    '<i class="bi bi-person-circle"></i> ' + nickName;
            }
        })
        .catch(error => {
            console.error('加载用户信息失败:', error);
        });
}

// 退出登录
function logout() {
    confirm('确定要退出登录吗？', function(result) {
        if (!result) {
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
            localStorage.removeItem('token');
            window.location.href = '/login.html';
        })
        .catch(error => {
            console.error('退出登录失败:', error);
            // 即使网络错误，也清除本地存储并跳转
            localStorage.removeItem('token');
            window.location.href = '/login.html';
        });
    });
}

// 切换到指定视图
function switchToSection(sectionName) {
    console.log('切换到视图:', sectionName);
    
    // 隐藏所有视图
    document.querySelectorAll('.section-view').forEach(view => {
        view.classList.remove('active');
    });
    
    // 移除所有菜单项的active类
    document.querySelectorAll('.sidebar-menu a').forEach(link => {
        link.classList.remove('active');
    });
    
    // 显示目标视图
    const targetView = document.getElementById('view-' + sectionName);
    if (targetView) {
        targetView.classList.add('active');
        console.log('视图切换成功:', 'view-' + sectionName);
    } else {
        console.error('找不到视图:', 'view-' + sectionName);
    }
    
    // 激活对应菜单项
    const menuMap = {
        'home': 'a[href="/index.html"]',
        'user-manage': '#menu-user-manage',
        'settings': '#menu-settings'
    };
    
    const menuSelector = menuMap[sectionName];
    if (menuSelector) {
        const menuItem = document.querySelector(menuSelector);
        if (menuItem) {
            menuItem.classList.add('active');
        }
    }
    
    // 如果是用户管理视图，加载数据
    if (sectionName === 'user-manage') {
        loadUsers();
    }
    
    // 如果是个人设置视图，加载用户信息
    if (sectionName === 'settings') {
        loadSettingsUserInfo();
    }
}

// 菜单点击事件
document.querySelectorAll('.sidebar-menu a').forEach(link => {
    link.addEventListener('click', function(e) {
        const href = this.getAttribute('href');
        
        // 处理锚点链接
        if (href.startsWith('#')) {
            e.preventDefault();
            const sectionName = href.substring(1);
            switchToSection(sectionName);
        }
        // 其他链接不做处理，让浏览器正常跳转
    });
});

// ==================== 用户管理功能 ====================
let currentPage = 1;
let pageSize = 10;
let totalPages = 0;

// Load users
async function loadUsers() {
    console.log('开始加载用户列表...');
    
    const searchNickName = document.getElementById('searchNickName').value;
    const searchMobile = document.getElementById('searchMobile').value;
    const searchEmail = document.getElementById('searchEmail').value;

    const requestBody = {
        pageNum: currentPage,
        pageSize: pageSize
    };

    if (searchNickName) requestBody.nickName = searchNickName;
    if (searchMobile) requestBody.mobile = searchMobile;
    if (searchEmail) requestBody.email = searchEmail;

    try {
        console.log('发送请求到 /userManage/page');
        const response = await fetch('/userManage/page', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getToken()
            },
            body: JSON.stringify(requestBody)
        });

        console.log('响应状态:', response.status);
        
        if (!response.ok) {
            console.error('HTTP错误:', response.status, response.statusText);
            alert('请求失败: ' + response.status + ' ' + response.statusText);
            return;
        }

        const result = await response.json();
        console.log('用户列表响应:', result);
        
        if (!result) {
            console.error('响应数据为空');
            return;
        }
        
        if (result.code === 0) {
            if (!result.data || !result.data.records) {
                console.error('数据结构错误:', result);
                alert('数据格式错误');
                return;
            }
            console.log('准备渲染表格，记录数:', result.data.records.length);
            renderTable(result.data.records);
            totalPages = result.data.pages;
            console.log('总页数:', totalPages);
            renderPagination();
            console.log('用户列表加载完成');
        } else {
            alert('加载失败: ' + result.message);
        }
    } catch (error) {
        console.error('加载用户列表错误:', error);
        console.error('错误详情:', error.message);
        alert('加载失败，请检查网络连接');
    }
}

// Render table
function renderTable(users) {
    console.log('渲染表格，用户数量:', users ? users.length : 0);
    const tbody = document.getElementById('userTableBody');
    
    if (!tbody) {
        console.error('找不到 userTableBody 元素');
        return;
    }
    
    if (!users || users.length === 0) {
        console.log('没有数据，显示暂无数据');
        tbody.innerHTML = '<tr><td colspan="6" class="empty">暂无数据</td></tr>';
        return;
    }

    console.log('开始生成表格HTML');
    const html = users.map(user => `
        <tr>
            <td>${user.id}</td>
            <td>${user.nickName || '-'}</td>
            <td>${user.email || '-'}</td>
            <td>${user.mobile || '-'}</td>
            <td>${formatDate(user.createTime)}</td>
            <td>
                <button class="btn btn-edit" onclick="editUser(${user.id})">编辑</button>
                <button class="btn btn-danger" onclick="deleteUser(${user.id})">删除</button>
            </td>
        </tr>
    `).join('');
    
    console.log('设置表格HTML');
    tbody.innerHTML = html;
    console.log('表格渲染完成');
}

// Get sex text
function getSexText(sex) {
    if (sex === 0) return '女';
    if (sex === 1) return '男';
    if (sex === 2) return '未知';
    return '-';
}

// Format date
function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleString('zh-CN');
}

// Render pagination
function renderPagination() {
    const pagination = document.getElementById('pagination');
    
    if (totalPages <= 1) {
        pagination.innerHTML = '';
        return;
    }

    let html = `
        <button onclick="goToPage(${currentPage - 1})" ${currentPage === 1 ? 'disabled' : ''}>上一页</button>
        <span>第 ${currentPage} / ${totalPages} 页</span>
        <button onclick="goToPage(${currentPage + 1})" ${currentPage === totalPages ? 'disabled' : ''}>下一页</button>
    `;
    
    pagination.innerHTML = html;
}

// Go to page
function goToPage(page) {
    if (page < 1 || page > totalPages) return;
    currentPage = page;
    loadUsers();
}

// Search users
function searchUsers() {
    currentPage = 1;
    loadUsers();
}

// Reset search
function resetSearch() {
    document.getElementById('searchNickName').value = '';
    document.getElementById('searchMobile').value = '';
    document.getElementById('searchEmail').value = '';
    currentPage = 1;
    loadUsers();
}

// Show create modal
function showCreateModal() {
    document.getElementById('modalTitle').textContent = '新增用户';
    document.getElementById('userForm').reset();
    document.getElementById('userId').value = '';
    document.getElementById('passwordGroup').style.display = 'block';
    document.getElementById('userPassword').required = true;
    document.getElementById('userModal').style.display = 'block';
}

// Edit user
async function editUser(id) {
    try {
        const response = await fetch(`/userManage/${id}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getToken()
            }
        });

        const result = await response.json();
        
        if (result.code === 0) {
            const user = result.data;
            document.getElementById('modalTitle').textContent = '编辑用户';
            document.getElementById('userId').value = user.id;
            document.getElementById('passwordGroup').style.display = 'none';
            document.getElementById('userPassword').required = false;
            document.getElementById('userNickName').value = user.nickName || '';
            document.getElementById('userMobile').value = user.mobile || '';
            document.getElementById('userEmail').value = user.email || '';
            document.getElementById('userModal').style.display = 'block';
        } else {
            alert('获取用户信息失败: ' + result.message);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('获取用户信息失败');
    }
}

// Delete user
async function deleteUser(id) {
    if (!confirm('确定要删除该用户吗？')) return;

    try {
        const response = await fetch(`/userManage/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + getToken()
            }
        });

        const result = await response.json();
        
        if (result.code === 0) {
            alert('删除成功');
            loadUsers();
        } else {
            alert('删除失败: ' + result.message);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('删除失败');
    }
}

// Close modal
function closeModal() {
    document.getElementById('userModal').style.display = 'none';
}

// Handle form submit
document.getElementById('userForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const userId = document.getElementById('userId').value;
    const isEdit = !!userId;

    const data = {
        nickName: document.getElementById('userNickName').value,
        mobile: document.getElementById('userMobile').value,
        email: document.getElementById('userEmail').value
    };

    if (!isEdit) {
        data.password = document.getElementById('userPassword').value;
    } else {
        data.id = parseInt(userId);
    }

    try {
        const url = isEdit ? '/userManage/update' : '/userManage/create';
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getToken()
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();
        
        if (result.code === 0) {
            alert(isEdit ? '更新成功' : '创建成功');
            closeModal();
            loadUsers();
        } else {
            alert((isEdit ? '更新' : '创建') + '失败: ' + result.message);
        }
    } catch (error) {
        console.error('Error:', error);
        alert((isEdit ? '更新' : '创建') + '失败');
    }
});

// ==================== 个人设置功能 ====================

// 加载个人设置用户信息
function loadSettingsUserInfo() {
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
                const user = data.data;
                document.getElementById('settingsNickName').value = user.nickName || '';
                document.getElementById('settingsEmail').value = user.email || '';
                document.getElementById('settingsMobile').value = user.mobile || '';
            }
        })
        .catch(error => {
            console.error('加载用户信息失败:', error);
        });
}

// 显示提示信息
function showSettingsAlert(type, message) {
    const successAlert = document.getElementById('settingsSuccessAlert');
    const errorAlert = document.getElementById('settingsErrorAlert');
    
    successAlert.style.display = 'none';
    errorAlert.style.display = 'none';
    
    if (type === 'success') {
        successAlert.textContent = message;
        successAlert.style.display = 'block';
    } else {
        errorAlert.textContent = message;
        errorAlert.style.display = 'block';
    }
}

// 更新个人信息
document.getElementById('profileForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const nickName = document.getElementById('settingsNickName').value.trim();
    const email = document.getElementById('settingsEmail').value.trim();
    const mobile = document.getElementById('settingsMobile').value.trim();
    
    if (!nickName || !email) {
        showSettingsAlert('error', '请填写所有必填字段');
        return;
    }

    const token = getToken();
    if (!token) {
        showSettingsAlert('error', '请先登录');
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
            email: email,
            mobile: mobile
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.code === 0) {
            showSettingsAlert('success', '个人信息更新成功');
            loadSettingsUserInfo();
        } else {
            showSettingsAlert('error', data.message || '更新失败');
        }
    })
    .catch(error => {
        console.error('更新失败:', error);
        showSettingsAlert('error', '网络错误，请重试');
    });
});

// 修改密码
document.getElementById('passwordForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const oldPassword = document.getElementById('oldPassword').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    if (!oldPassword || !newPassword || !confirmPassword) {
        showSettingsAlert('error', '请填写所有密码字段');
        return;
    }

    if (newPassword !== confirmPassword) {
        showSettingsAlert('error', '两次输入的新密码不一致');
        return;
    }

    if (newPassword.length < 6) {
        showSettingsAlert('error', '新密码长度不能少于6位');
        return;
    }

    const token = getToken();
    if (!token) {
        showSettingsAlert('error', '请先登录');
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
            showSettingsAlert('success', '密码修改成功，请使用新密码重新登录');
            document.getElementById('passwordForm').reset();
                            
            localStorage.removeItem('token');
            window.location.href = '/login.html';
        } else {
            showSettingsAlert('error', data.message || '修改失败');
        }
    })
    .catch(error => {
        console.error('修改失败:', error);
        showSettingsAlert('error', '网络错误，请重试');
    });
});
