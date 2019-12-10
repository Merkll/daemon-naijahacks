import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import { config } from 'dotenv';

config();

/**
   * Hash Password Method
   * @param {string} password
   * @returns {string} hashed password
   */
const encryptString = (string) => {
  const saltRounds = 4;
  return bcrypt.hashSync(string, bcrypt.genSaltSync(saltRounds));
};

/**
   * comparePassword
   * @param {string} password
   * @param {string} hashPassword
   * @returns {Boolean} True or False
   */
const compareHashedString = (password, hashedString) => bcrypt.compareSync(password, hashedString);

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

export { encryptString, compareHashedString, generateToken };
