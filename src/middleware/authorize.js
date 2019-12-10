import jwt from 'jsonwebtoken';
import { config } from 'dotenv';
import Nexmo from 'nexmo';
import { errorResponse } from '../helpers/serverResponse';
import Model from '../database/models';

config();


const nexmo = new Nexmo({
  apiKey: process.env.NEXMO_API_KEY,
  apiSecret: process.env.NEXMO_SECRET,
}, { debug: true });

const BRAND_NAME = 'Watcher';

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

const sendVerification = (reciepientNumber) => nexmo.verify.request({
  number: reciepientNumber,
  brand: BRAND_NAME,
  workflow_id: 6, // specifies sms
  // pin_expiry: 3600,
}, (err, result) => {
  if (err) {
    console.error(err);
    return null;
  }
  const verifyRequestId = result.request_id;
  console.log('request_id', verifyRequestId);
  return verifyRequestId;
});


const checkVerificationCode = (requestId, code) => nexmo.verify.check({
  request_id: requestId,
  code,
}, (err, result) => {
  if (err) {
    console.error(err);
  } else {
    console.log(result);
  }
});

export {
  verifyToken, authorizeAdmin, authourizeUser, sendVerification, checkVerificationCode,
};
