const ACCESS_TOKEN = "board-access-token"
const REFRESH_TOKEN = "board-refresh-token"

function getCookieValue(key) {
  return document.cookie.split("; ")
    .find((row) => row.startsWith(`${key}=`))
    ?.split("=")[1]
}

function getAccessToken() {
  return getCookieValue(ACCESS_TOKEN)
}

function getRefreshToken() {
  return getCookieValue(REFRESH_TOKEN)
}

function setToken({accessToken, refreshToken}) {
  if (accessToken) {
    document.cookie = `${ACCESS_TOKEN}=${accessToken}; SameSite=Strict; Domain=${process.env.VUE_APP_DOMAIN}; path=/;`
  }
  if (refreshToken) {
    document.cookie = `${REFRESH_TOKEN}=${refreshToken}; SameSite=Strict; Domain=${process.env.VUE_APP_DOMAIN}; path=/;`
  }
}

function clearToken() {
  document.cookie = `${ACCESS_TOKEN}=; max-age=0; Domain=${process.env.VUE_APP_DOMAIN}`
  document.cookie = `${REFRESH_TOKEN}=; max-age=0; Domain=${process.env.VUE_APP_DOMAIN}`
}

export {getAccessToken, getRefreshToken, setToken, clearToken, ACCESS_TOKEN, REFRESH_TOKEN}
