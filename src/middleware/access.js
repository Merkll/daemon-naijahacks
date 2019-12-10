import jwt from 'jsonwebtoken';
import { errorResponse } from '../helpers/serverResponse';
import Model from '../database/models';

const authorizationErrorMessage = 'You need permission to access resource';
const verifyToken = async (req, res, next) => {
  let token = req.headers['x-access-token'] || req.headers.authorization || '';
  if (token.startsWith('Bearer')) token = token.slice(7, token.length);
  if (token) {
    try {
      const decoded = jwt.verify(token, process.env.SECRET_STRING);
      const user = await Model.User.findById(decoded.id);
      if (user) {
        req.user = user;

        next();
      } else errorResponse(res, 404, authorizationErrorMessage);
    } catch (error) {
      errorResponse(res, 401, error);
    }
  } else {
    errorResponse(res, 401, 'Authorization token not provided');
  }
};


/**
 *
 * @param {object} roleArray an array of roles
 * @returns {*} json or next
 */
const authorize = (roleArray) => async (req, res, next) => {
  const { role } = req.user;
  if (!roleArray.includes(role)) {
    return errorResponse(res, 403, authorizationErrorMessage);
  }
  return next();
};

const authorizeAdmin = authorize(['admin']);

const authourizeUser = authorize(['user', 'admin']);

export { verifyToken, authorizeAdmin, authourizeUser };
