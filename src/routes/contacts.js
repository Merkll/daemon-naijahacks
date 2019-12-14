import express from 'express';
// import { verifyToken } from '../middleware/authorize';
import getContacts from '../controllers/contactController';

const contacts = express.Router();
const contactRouter = '/contacts';

contacts.get(`${contactRouter}`, getContacts);

export default contacts;
