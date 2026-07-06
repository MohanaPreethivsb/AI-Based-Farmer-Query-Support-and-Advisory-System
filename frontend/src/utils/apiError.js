export const getApiErrorMessage = (error, fallbackMessage) => {
  if (error.response?.data?.message) {
    return error.response.data.message
  }

  if (error.request) {
    return 'Cannot reach the server. Check that the backend is running and allowed by CORS.'
  }

  return fallbackMessage
}
