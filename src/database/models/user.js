import mongoose from 'mongoose';

const userSchema = new mongoose.Schema({
  firstName: {
    type: String,
    required: true,
  },
  lastName: {
    type: String,
    required: true,
  },
  phoneNumber: {
    type: String,
    unique: true,
    required: true,
  },
  password: {
    type: String,
    default: '',
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

const User = mongoose.model('User', userSchema);

export default User;
