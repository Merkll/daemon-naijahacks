import Model from '../database/models';
import { encryptString, generateToken } from '../helpers/authHelper';
import { successResponse, errorResponse } from '../helpers/serverResponse';

const { User } = Model;

const signUp = async (req, res) => {
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
      password: undefined,
      role: user.role,
    };

    return successResponse(res, 201, data, 'registration successful', token);
  } catch (error) {
    return errorResponse(res, 500, error);
  }
};

export default signUp;
