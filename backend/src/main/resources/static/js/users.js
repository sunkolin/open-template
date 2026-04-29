let currentPage = 1;
let pageSize = 10;
let totalPages = 0;

// Get Token from localStorage
function getToken() {
    return localStorage.getItem('token');
}

// Check authentication
function checkAuth() {
    const token = getToken();
    if (!token) {
        window.location.href = '/login.html';
        return false;
    }
    return true;
}

// Load users
async function loadUsers() {
    if (!checkAuth()) return;

    const searchUserName = document.getElementById('searchUserName').value;
    const searchMobile = document.getElementById('searchMobile').value;
    const searchEmail = document.getElementById('searchEmail').value;
    const searchNickName = document.getElementById('searchNickName').value;

    const requestBody = {
        pageNum: currentPage,
        pageSize: pageSize
    };

    if (searchUserName) requestBody.userName = searchUserName;
    if (searchMobile) requestBody.mobile = searchMobile;
    if (searchEmail) requestBody.email = searchEmail;
    if (searchNickName) requestBody.nickName = searchNickName;

    try {
        const response = await fetch('/userManage/page', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getToken()
            },
            body: JSON.stringify(requestBody)
        });

        const result = await response.json();
        
        if (result.code === 0) {
            renderTable(result.data.records);
            totalPages = result.data.pages;
            renderPagination();
        } else {
            alert('加载失败: ' + result.message);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('加载失败，请检查网络连接');
    }
}

// Render table
function renderTable(users) {
    const tbody = document.getElementById('userTableBody');
    
    if (!users || users.length === 0) {
        tbody.innerHTML = '<tr><td colspan="10" class="empty">暂无数据</td></tr>';
        return;
    }

    tbody.innerHTML = users.map(user => `
        <tr>
            <td>${user.id}</td>
            <td>${user.userName || '-'}</td>
            <td>${user.nickName || '-'}</td>
            <td>${user.fullName || '-'}</td>
            <td>${user.mobile || '-'}</td>
            <td>${user.email || '-'}</td>
            <td>${getSexText(user.sex)}</td>
            <td>${user.remark || '-'}</td>
            <td>${formatDate(user.createTime)}</td>
            <td>
                <button class="btn btn-edit" onclick="editUser(${user.id})">编辑</button>
                <button class="btn btn-danger" onclick="deleteUser(${user.id})">删除</button>
            </td>
        </tr>
    `).join('');
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
    document.getElementById('searchUserName').value = '';
    document.getElementById('searchMobile').value = '';
    document.getElementById('searchEmail').value = '';
    document.getElementById('searchNickName').value = '';
    currentPage = 1;
    loadUsers();
}

// Show create modal
function showCreateModal() {
    document.getElementById('modalTitle').textContent = '新增用户';
    document.getElementById('userForm').reset();
    document.getElementById('userId').value = '';
    document.getElementById('passwordGroup').style.display = 'block';
    document.getElementById('password').required = true;
    document.getElementById('userName').readOnly = false;
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
            document.getElementById('userName').value = user.userName || '';
            document.getElementById('userName').readOnly = true;
            document.getElementById('passwordGroup').style.display = 'none';
            document.getElementById('password').required = false;
            document.getElementById('nickName').value = user.nickName || '';
            document.getElementById('fullName').value = user.fullName || '';
            document.getElementById('mobile').value = user.mobile || '';
            document.getElementById('email').value = user.email || '';
            document.getElementById('sex').value = user.sex !== null ? user.sex : '';
            document.getElementById('birthday').value = user.birthday ? user.birthday.split('T')[0] : '';
            document.getElementById('remark').value = user.remark || '';
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
        userName: document.getElementById('userName').value,
        nickName: document.getElementById('nickName').value,
        fullName: document.getElementById('fullName').value,
        mobile: document.getElementById('mobile').value,
        email: document.getElementById('email').value,
        sex: document.getElementById('sex').value ? parseInt(document.getElementById('sex').value) : null,
        birthday: document.getElementById('birthday').value || null,
        remark: document.getElementById('remark').value
    };

    if (!isEdit) {
        data.password = document.getElementById('password').value;
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

// Close modal when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('userModal');
    if (event.target === modal) {
        closeModal();
    }
}

// Load users on page load
window.onload = function() {
    loadUsers();
};
