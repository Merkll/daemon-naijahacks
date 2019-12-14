import jwt from 'jsonwebtoken';
import { config } from 'dotenv';

config();

/**
   * Gnerate Token
   * @param {Object} user
   * @returns {string} token
   */
const generateToken = (id) => jwt.sign(
  {
    id,
  },
  process.env.SECRET_STRING,
  { expiresIn: '7h' },
);

export default generateToken;
