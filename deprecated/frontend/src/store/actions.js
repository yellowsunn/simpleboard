import {
  fetchAdminDelete,
  fetchAdminUpdate,
  fetchLogin,
  fetchLogout,
  fetchRegister, fetchSearch,
  fetchBoard,
  uploadPostData, getPostData, deletePostData,
  uploadCommentData, getCommentData, deleteCommentData, updatePostData,
  getChatData, changePassword,
  fetchCurrentUser, getPostHit,
  deleteCurrentUser, deleteUser, getUsers,
} from '@/api';

export default {
  async FETCH_LOGIN(context, { account }) {
    return await fetchLogin(account);
  },
  async FETCH_LOGOUT() {
    return await fetchLogout();
  },
  async FETCH_REGISTER(context, account) {
    return await fetchRegister(account);
  },
  async FETCH_CURRENT_USER({ commit, state }) {
    try {
      const response = await fetchCurrentUser();
      commit('SET_USER_INFO', response.data);
      state.isLoggedIn = true;
    } catch (error) {
      state.isLoggedIn = false;
    }
  },
  async FETCH_ADMIN_UPDATE(context, data) {
    return await fetchAdminUpdate(data);
  },
  async FETCH_ADMIN_DELETE(context, data) {
    return await fetchAdminDelete(data);
  },
  async FETCH_SEARCH({ commit }, search) {
    try {
      const response = await fetchSearch(search);
      commit('SET_ADMIN_DATA', response.data);
    } catch (error) {
      console.log(error);
    }
  },
  async FETCH_SEARCH_SCROLL({ commit }, { search, page }) {
    try {
      const response = await fetchSearch(search, page);
      commit('ADD_ADMIN_DATA', response.data);
      return response;
    } catch (error) {
      console.log(error);
    }
  },
  async FETCH_BOARD({ commit }, { title, username , page }) {
    try {
      const response = await fetchBoard(title, username, page);
      commit('SET_BOARD', response.data);
      return response;
    } catch(error) {
      console.log(error);
      throw Error("fetch board data error");
    }
  },
  async UPLOAD_POST_DATA(context, formData) {
    try {
      return await uploadPostData(formData);
    } catch (error) {
      console.log(error);
      throw new Error("post upload error");
    }
  },
  async GET_POST_DATA({ commit }, postId) {
    const response = await getPostData(postId);
    commit('SET_POST_DTO', response.data);
    return response;
  },
  async GET_POST_HIT({ commit }, postId) {
    const response = await getPostHit(postId);
    commit('SET_POST_HIT', response);
    return response;
  },
  async UPDATE_POST_DATA(context, {postId, formData}) {
    try {
      return await updatePostData(postId, formData);
    } catch (error) {
      console.log(error);
      throw new Error("post update error");
    }
  },
  async DELETE_POST_DATA(content, postId) {
    return await deletePostData(postId);
  },
  async INIT_POST_DATA({ commit }) {
    commit('INIT_POST_DTO');
  },
  async UPLOAD_COMMENT_DATA(content, commentData) {
    try {
      return await uploadCommentData(commentData);
    } catch (error) {
      alert(error.data.message);
      throw error;
    }
  },
  async GET_COMMENT_DATA({ state, commit }, { postId, lastCommentIndex }) {
    try {
      const response = await getCommentData(postId, getCursor(state, lastCommentIndex));
      commit('UPDATE_COMMENT_DTO', { data: response.data , lastCommentIndex });
      return response;
    } catch (error) {
      console.log(error);
      throw new Error("get comment error.");
    }
  },
  async DELETE_COMMENT_DATA(content, commentId) {
    return await deleteCommentData(commentId);
  },
  async GET_CHAT_DATA({ commit }) {
    try {
      const response = await getChatData();
      commit('SET_CHAT_DTO', response.data);
    } catch (error) {
      console.log(error);
    }
  },
  // 추가된 경우
  async INSERT_CHAT_DATA({ commit }, page) {
    // 과거의 채팅을 보는 경우 (스크롤을 위로 올리는 경우)
    const isHistory = (page !== undefined && page > 0)
    try {
      const response = await getChatData(page);
      commit('INSERT_CHAT_DTO', { data: response.data, isHistory });
    } catch (error) {
      console.log(error);
    }
  },
  async CHANGE_PASSWORD(content, { password, newPassword }) {
    return await changePassword(password, newPassword);
  },
  async DELETE_CURRENT_USER() {
    return await deleteCurrentUser();
  },
  async DELETE_USER(content, userId) {
    return await deleteUser(userId);
  },
  async GET_USERS({ commit }, { search, cursor }) {
    const response = await getUsers(search, cursor);
    await commit('SET_USERS_DTO', { data: response.data, lastIndex: cursor })
  }
};

function getCursor(state, lastCommentIndex) {
  if (!lastCommentIndex || !state.commentDto) return null;

  const lastComment = state.commentDto.content[lastCommentIndex];
  if (!lastComment) return '';
  return lastComment.parentId.toString().padStart(20, '0') + lastComment.id.toString().padStart(20, '0');
}

