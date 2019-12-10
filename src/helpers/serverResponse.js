/**
   * helper for sending server success messages
   * @param {Object} res - server response
   * @param {Number} statusCode - server status code
   * @param {Object} data - response payload
   * @param {String} message - custom message
   * @returns {Object} custom response
   */
const successResponse = (
  res,
  statusCode,
  data,
  message,
  token,
) => res.status(statusCode).json({ message, token, data });

/**
   * helper for sending server success messages
   * @param {Object} res - server response
   * @param {Number} statusCode - error status code
   * @param {Object} errors - operation error message
   * @returns {Object} custom response
   */
const errorResponse = (res, statusCode = 500, errors = 'An error occurred') => res.status(statusCode).json({ errors });

export { successResponse, errorResponse };
