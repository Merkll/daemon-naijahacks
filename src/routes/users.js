import express from 'express';
// import { verifyToken } from '../middleware/authorize';
import { getUsers } from '../controllers/userController';

const users = express.Router();
const userRouter = '/users';

users.get(`${userRouter}`, getUsers);

export default users;
