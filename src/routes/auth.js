import express from 'express';
// import { verifyToken } from '../middleware/authorize';
import { register, login, verifyOTP } from '../controllers/userController';

const auth = express.Router();
const authRoute = '/auth';

auth.post(`${authRoute}/register`, register);
auth.post(`${authRoute}/login`, login);
auth.get(`${authRoute}/otp`, verifyOTP);

export default auth;
