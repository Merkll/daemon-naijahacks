import Model from '../database/models';
import { encryptString, stringMatchesHash, generateToken } from '../helpers/authHelper';
import { nexmo, sendVerification } from '../services/otpService';
import { successResponse, errorResponse } from '../helpers/serverResponse';

const { User } = Model;

const register = async (req, res) => {
  try {
    const foundUser = await User.findByPhone(req.body.phoneNumber);

    if (foundUser) {
      return errorResponse(res, 400, 'User with phone number already exists');
    }

    const {
      firstName,
      lastName,
      phoneNumber,
    } = req.body;

    const user = await Model.User.create({
      firstName, lastName, phoneNumber,
    });
    sendVerification(req.body.phoneNumber);

    const data = {
      id: user._id,
      firstName: user.firstName,
      lastName: user.lastName,
      phoneNumber: user.phoneNumber,
      verified: user.verified,
    };

    return successResponse(res, 201, data, 'Registration successful. A verification code has been sent to your phone number');
  } catch (error) {
    console.error(error);

    return errorResponse(res, 500, 'An error occured');
  }
};


const login = async (req, res) => {
  const { phoneNumber } = req.body;
  try {
    const user = await User.findByPhone(phoneNumber);

    if (!user) {
      return errorResponse(res, 400, 'Incorrect phone number');
    }

    if (!user.verified) {
      return errorResponse(res, 401, 'Kindly verify your phone number');
    }

    sendVerification(req.body.phoneNumber);

    return successResponse(res, 200, undefined, 'A verification code has been sent to your phone number');
  } catch (error) {
    console.error(error);

    return errorResponse(res, 500, 'An error occured');
  }
};

const verifyOTP = async (req, res) => {
  const { otp, phoneNumber, action } = req.query;
  const user = await Model.User.findByPhone(phoneNumber);

  if (!user) {
    return errorResponse(res, 400, 'Incorrect phone number');
  }

  if (action === 'resend') {
    sendVerification(phoneNumber);
    return successResponse(res, 200, 'success', 'A verification code has been sent.');
  }

  if (action === 'verify') {
    nexmo.verify.check({
      request_id: user.verificationId,
      code: otp,
    }, async (err, result) => {
      if (err) {
        console.error(err);
        return errorResponse(res, 400, 'Error verifying phone ***');
      }

      if (result.status === '0') {
        try {
          await Model.User.findOneAndUpdate({
            phoneNumber,
          }, {
            $set: {
              verificationId: '',
              verified: true,
            },
          }, { returnNewDocument: true });

          const token = generateToken(user._id);
          return successResponse(res, 200, 'success', 'Verified successfully', token);
        } catch (error) {
          console.error(error);
          return errorResponse(res, 500, 'Something went wrong');
        }
      } else {
        console.log(result);
        return errorResponse(res, 400, 'Error verifying phone number');
      }
    });
  }
};

/*
  TODO: add endpoint for resending OTP
 */

export { login, register, verifyOTP };
