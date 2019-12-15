import { Schema, model } from 'mongoose';

const userSchema = new Schema({
  firstName: {
    type: String,
  },
  lastName: {
    type: String,
  },
  phoneNumber: {
    type: String,
    unique: true,
    required: true,
  },
  verified: {
    type: Boolean,
    default: false,
  },
  verificationId: {
    type: String,
    default: '',
  },
  role: {
    type: String,
    default: 'user',
  },
  ussdId: {
    type: String,
    unique: true,
  },
});

userSchema.statics.findByPhone = async function findByPhone(phoneNumber) {
  try {
    const user = await this.findOne({
      phoneNumber,
    });
    return user;
  } catch (error) {
    return null;
  }
};

userSchema.pre('remove', function cascade(next) {
  this.model('Contact').deleteMany({ user: this._id }, next);
});

const User = model('User', userSchema);

export default User;
