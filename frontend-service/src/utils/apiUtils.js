import axios from "axios";
import {isAccessTokenExpired} from "@/utils/httpErrorHandler";
import AccessTokenExpiredError from "@/utils/AccessTokenExpiredError";
import {getAccessToken, getRefreshToken} from "@/utils/tokenUtils";

async function callBoardApi(method, url, data, isRequireAuth, headers = {"Content-Type": "application/json"}) {
  if (isRequireAuth) {
    const accessToken = getAccessToken()
    if (accessToken) {
      headers = {
        ...headers,
        'Authorization': 'bearer ' + accessToken,
      }
    }
  }

  try {
    const response = await axios({
      baseURL: process.env.VUE_APP_BOARD_API_BASE_URL,
      url,
      method,
      data,
      headers,
    });
    return {
      isError: false,
      data: response?.data,
    }
  } catch (e) {
    const message = e?.response?.status === 429 ?
      "허용된 API 요청 횟수를 초과하였습니다. 잠시 후 다시 시도해주세요" : "알 수 없는 에러가 발생하였습니다."
    const errorResponse = e?.response?.data
    checkAccessTokenExpired(isRequireAuth, errorResponse)
    return {
      isError: true,
      data: {
        message,
        ...errorResponse
      }
    }
  }
}

function checkAccessTokenExpired(isRequireAuth, errorResponse) {
  if (isRequireAuth && isAccessTokenExpired(errorResponse)) {
    throw new AccessTokenExpiredError()
  }
}

async function reAcquireAccessToken() {
  const accessToken = getAccessToken()
  const refreshToken = getRefreshToken();
  return axios({
    baseURL: process.env.VUE_APP_BOARD_API_BASE_URL,
    url: '/api/v2/auth/token',
    method: "POST",
    data: {
      accessToken, refreshToken,
    }
  });
}

export {callBoardApi, reAcquireAccessToken}
