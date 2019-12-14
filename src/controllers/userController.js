import Model from '../database/models';
import generateToken from '../helpers/authHelper';
import { nexmo, sendVerification } from '../services/otpService';
import createUser from '../services/userService';
import { successResponse, errorResponse } from '../helpers/serverResponse';

const { User } = Model;

const verificationMessage = 'A verification code has been sent to your phone number';
const verificationError = 'Error verifying phone number';

const register = async (req, res) => {
  const {
    firstName,
    lastName,
    phoneNumber,
  } = req.body;

  try {
    const foundUser = await User.findByPhone(phoneNumber);

    if (foundUser) {
      sendVerification(phoneNumber);
      return successResponse(res, 200, undefined, verificationMessage);
    }

    const user = await createUser({
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

    return successResponse(res, 201, data, `Registration successful. ${verificationMessage}`);
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

    sendVerification(phoneNumber);

    return successResponse(res, 200, undefined, verificationMessage);
  } catch (error) {
    console.error(error);

    return errorResponse(res, 500, 'An error occured');
  }
};

const verifyOTP = async (req, res) => {
  const { otp, phoneNumber, action } = req.query;

  let user;

  try {
    user = await Model.User.findByPhone(phoneNumber);
  } catch (error) {
    console.error(error);
  }


  if (!user) {
    return errorResponse(res, 400, 'Incorrect phone number');
  }

  if (action === 'resend') {
    sendVerification(phoneNumber);
    return successResponse(res, 200, 'success', verificationMessage);
  }

  if (action === 'verify') {
    nexmo.verify.check({
      request_id: user.verificationId,
      code: otp,
    }, async (err, result) => {
      if (err) {
        console.error(err);
        return errorResponse(res, 400, verificationError);
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
        return errorResponse(res, 400, verificationError);
      }
    });
  }
};

/*
  TODO: add endpoint for resending OTP
 */

export { login, register, verifyOTP };
