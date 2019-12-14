import models from '../models';

export const uid1 = '5df39475cedc7e7d4dd3092f';
export const uid2 = '5df3c9ad22f291403c4b6958';

const user1 = {
  _id: uid1,
  firstName: 'Han',
  lastName: 'solo',
  phoneNumber: '2347031844444',
  verified: true,
  verificationId: '',
  role: 'admin',
  ussdId: 'HS4444',
};

const user2 = {
  _id: uid2,
  firstName: 'Han',
  lastName: 'solo',
  phoneNumber: '2347031841489',
  verified: true,
  verificationId: '',
  role: 'user',
  ussdId: 'HS1489',
};

const seedUser = () => models.User.insertMany([user1, user2]);

export default seedUser;
