import Model from '../database/models';
import { encryptString, stringMatchesHash, generateToken } from '../helpers/authHelper';
import { successResponse, errorResponse } from '../helpers/serverResponse';

const { User } = Model;

const register = async (req, res) => {
  try {
    const foundUser = await User.findByPhone(req.body.phoneNumber);

    if (foundUser) {
      return errorResponse(res, 400, 'User with phone number already exists');
    }

    const user = await Model.User.create({ ...req.body, password: encryptString(req.body.password), role: 'user' });
    const token = generateToken(user._id);

    const data = {
      id: user._id,
      firstName: user.firstName,
      lastName: user.lastName,
      phoneNumber: user.phoneNumber,
      role: user.role,
    };

    return successResponse(res, 201, data, 'registration successful', token);
  } catch (error) {
    return errorResponse(res, 500, error);
  }
};


const login = async (req, res) => {
  const { phoneNumber, password } = req.body;
  try {
    const user = await User.findByPhone(phoneNumber);

    if (!user) {
      return errorResponse(res, 400, 'Incorrect phone number of password');
    }

    if (!stringMatchesHash(password, user.password)) {
      return errorResponse(res, 400, 'Incorrect phone number of password');
    }

    const token = generateToken(user._id);

    const data = {
      id: user._id,
      phoneNumber: user.phoneNumber,
    };

    return successResponse(res, 201, data, 'login successful', token);
  } catch (error) {
    return errorResponse(res, 500, error);
  }
};

export { login, register };
