import models from '../models';
import { uid1, uid2 } from './user';

const contact1 = {
  name: 'Jacen Solo',
  phoneNumber: '07031844445',
  contactType: 'faf',
  user: uid2,
};

const contact2 = {
  name: 'Cheif Harper',
  phoneNumber: '07031841489',
  contactType: 'police-emergency',
  user: uid2,
};

const contact3 = {
  name: 'Doctor Ola',
  phoneNumber: '07031841488',
  contactType: 'health-emergency',
  user: uid1,
};

const seedContact = () => models.Contact.insertMany([contact1, contact2, contact3]);

export default seedContact;
