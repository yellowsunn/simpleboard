const isNotFoundUser = (errorResponse) => {
  return errorResponse.code === 'NOT_FOUND_USER'
}

const alertErrorMessage = (errorResponse) => {
  alert(errorResponse.message)
}

const isAccessTokenExpired = (errorResponse) => {
  return errorResponse.code === 'ACCESS_TOKEN_EXPIRED'
}

const isInvalidUser = (errorResponse) => {
  return errorResponse.code === 'NOT_FOUND_USER' || errorResponse.code === 'REQUIRE_LOGIN'
}

const isUnknownServerError = (errorResponse) => {
  return errorResponse.code === 'UNKNOWN_ERROR'
}

export {isNotFoundUser, alertErrorMessage, isAccessTokenExpired, isInvalidUser, isUnknownServerError}
