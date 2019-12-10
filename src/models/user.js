import mongoose from 'mongoose';

const userSchema = new mongoose.Schema({
  firstName: {
    type: String,
  },
  lastName: {
    type: String,
  },
  phoneNumber: {
    type: String,
    unique: true,
  },
  password: {
    type: String,
  },
});

userSchema.statics.findByPhone = async function findByPhone(phoneNumber) {
  const user = await this.findOne({
    phoneNumber,
  });
  return user;
};

userSchema.pre('remove', function cascade(next) {
  this.model('Contact').deleteMany({ user: this._id }, next);
});

const User = mongoose.model('User', userSchema);

export default User;
