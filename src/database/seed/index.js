import models from '../models';

const seedDB = async () => {
  const user1 = new models.User({
    firstName: 'Han',
    lastName: 'solo',
    phoneNumber: '07031844444',
    password: 'uncracked',
    role: 'admin',
  });

  const contact1 = new models.Contact({
    name: 'Jacen Solo',
    phoneNumber: '07031844445',
    user: user1.id,
  });

  const message1 = new models.Message({
    content: 'Emergency!!',
    user: user1.id,
    contact1: contact1.id,
  });

  await user1.save();
  await contact1.save();
  await message1.save();
};

export default seedDB;
