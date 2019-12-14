import express from 'express';
import auth from './auth';
import ussd from './ussd';

const router = express.Router();

router.use('/', auth, ussd);

export default router;
