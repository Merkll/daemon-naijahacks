import express from 'express';
import { root, handleUssd } from '../controllers/ussdController';

const auth = express.Router();
const ussdRoute = '/ussd';

auth.post(`${ussdRoute}`, handleUssd);
auth.get(`${ussdRoute}`, root);

export default auth;
