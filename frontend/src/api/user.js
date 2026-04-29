import request from '@/utils/request'

/**
 * 用户登录
 */
export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

/**
 * 获取用户信息
 */
export function getUserInfo() {
  return request({
    url: '/getUser',
    method: 'get'
  })
}

/**
 * 更新用户信息
 */
export function updateUserInfo(data) {
  return request({
    url: '/personalInformation/profile',
    method: 'put',
    data
  })
}

/**
 * 修改密码
 */
export function changePassword(data) {
  return request({
    url: '/personalInformation/password',
    method: 'put',
    data
  })
}

/**
 * 获取用户列表（分页）
 */
export function getUserList(params) {
  return request({
    url: '/userManage/page',
    method: 'post',
    data: params
  })
}

/**
 * 创建用户
 */
export function createUser(data) {
  return request({
    url: '/userManage/create',
    method: 'post',
    data
  })
}

/**
 * 更新用户
 */
export function updateUser(data) {
  return request({
    url: '/userManage/update',
    method: 'post',
    data
  })
}

/**
 * 删除用户
 */
export function deleteUser(id) {
  return request({
    url: `/userManage/${id}`,
    method: 'delete'
  })
}
