import express from 'express';
// import { verifyToken } from '../middleware/authorize';
import getMessages from '../controllers/messageController';

const messages = express.Router();
const messageRouter = '/messages';

messages.get(`${messageRouter}`, getMessages);

export default messages;
