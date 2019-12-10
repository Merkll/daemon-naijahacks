import express from 'express';
// import { verifyToken } from '../middleware/access';
import signUp from '../controllers/userController';

const auth = express.Router();
const authRoute = '/auth';

auth.post(`${authRoute}/register`, signUp);

export default auth;
