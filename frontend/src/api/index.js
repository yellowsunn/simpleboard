import axios from 'axios';

axios.interceptors.response.use(
  response => {
    return response;
  }, error => {
    return Promise.reject(error.response);
  }
);

// const server = getEnv('VUE_APP_API_DOMAIN');

const config = {
  baseURL: `/api`,
  withCredentials: true,
};

const fetchLogin = async (account) => {
  return await axios.post('http://localhost:8000/api/login', account, {
    withCredentials: false
  });
};

const fetchLogout = async () => {
  return await axios.post('/users/logout', null, config);
};

const fetchRegister = async (account) => {
  return await axios.post('/users', account, config);
};

const fetchCurrentUser = async () => {
  return await axios.get("/users/current", config);
}

const fetchData = async (url) => {
  return await axios.get(url, config);
}

const fetchAdminUpdate = async (data) => {
  return await axios.put('/admin/update', data, config);
}

const fetchAdminDelete = async (data) => {
  return await axios.delete('/admin/delete', {
    ...config,
    data
  });
}

const fetchSearch = async (search, page) => {
  return await axios.get("/admin/list", {
    ...config,
    params: {
      search, page
    }
  })
}

const fetchBoard = async (title, username, page) => {
  return await axios.get("/posts", {
    ...config,
    params: {
      title, username, page, size: 10
    }
  })
}

const uploadPostData =  async (formData) => {
  return await axios.post("/posts", formData, {
    ...config,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

const getPostData = async (postId) => {
  return await axios.get(`/posts/${postId}`, config);
}

const getPostHit = async (postId) => {
  return await axios.get(`/posts/${postId}/hit`, config);
}

const updatePostData = async (postId, formData) => {
  return await axios.put(`/posts/${postId}`, formData, {
    ...config,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

const deletePostData = async (postId) => {
  return await axios.delete(`/posts/${postId}`, config);
}

const uploadCommentData = async (commentData) => {
  return axios.post("/comments", commentData, config);
}

const getCommentData = async (postId, cursor) => {
  return axios.get(`/comments`, {
    ...config,
    params: { postId, cursor }
  })
}

const deleteCommentData = async (commentId) => {
  return axios.delete(`/comments/${commentId}`, {
    ...config
  });
}

const getChatData = async (page) => {
  return axios.get("/chat", {
    ...config,
    params: { page }
  });
}

const changePassword = async (password, newPassword) => {
  return axios.patch("/users/current", { password, newPassword }, config)
}

const deleteCurrentUser = async () => {
  return axios.delete("/users/current", config);
}

const deleteUser = async (userId) => {
  return axios.delete(`/users/${userId}`, config);
}

const getUsers = async (search, cursor) => {
  return axios.get('/users', {
    ...config,
    params: { search, cursor }
  })
}

export {
  fetchLogin,
  fetchLogout,
  fetchRegister,
  fetchCurrentUser,
  fetchData,
  fetchAdminUpdate,
  fetchAdminDelete,
  fetchSearch,
  fetchBoard,
  uploadPostData, getPostData, getPostHit, updatePostData, deletePostData,
  uploadCommentData, getCommentData, deleteCommentData,
  getChatData,
  changePassword,
  deleteCurrentUser, deleteUser,
  getUsers
};
