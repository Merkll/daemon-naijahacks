import express from 'express';
import auth from './auth';
import ussd from './ussd';
import users from './users';
import contacts from './contacts';
import messages from './messages';

const router = express.Router();

router.use('/', auth, users, contacts, messages, ussd);

export default router;
