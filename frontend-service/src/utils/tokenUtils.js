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

export {getAccessToken, getRefreshToken, ACCESS_TOKEN, REFRESH_TOKEN}
