import models from '../models';
import { uid1, uid2 } from './user';

const contact1 = {
  name: 'Jacen Solo',
  phoneNumber: '2348022658730',
  contactType: 'faf',
  user: uid2,
};

const contact2 = {
  name: 'Cheif Harper',
  phoneNumber: '2347031841489',
  contactType: 'police-emergency',
  user: uid2,
};

const contact3 = {
  name: 'Ma Rythae',
  phoneNumber: '2348038809980',
  contactType: 'police-emergency',
  user: uid2,
};


const contact4 = {
  name: 'Miss Odunayo',
  phoneNumber: '2348081811957',
  contactType: 'health-emergency',
  user: uid2,
};

const contact5 = {
  name: 'Engr. Miracle',
  phoneNumber: '2348132149729',
  contactType: 'health-emergency',
  user: uid2,
};

const seedContact = () => models.Contact.insertMany([
  contact1, contact2, contact3, contact4, contact5,
]);

export default seedContact;
