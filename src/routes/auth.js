import express from 'express';
// import { verifyToken } from '../middleware/access';
import { register, login } from '../controllers/userController';

const auth = express.Router();
const authRoute = '/auth';

auth.post(`${authRoute}/register`, register);
auth.post(`${authRoute}/login`, login);

export default auth;
