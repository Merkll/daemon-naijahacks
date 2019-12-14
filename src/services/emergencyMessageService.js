import { config } from 'dotenv';
import sendSms from '../helpers/sendSms';
import Model from '../database/models';

const { Contact } = Model;

config();

const EMERGENCY_TYPES = {
  1: 'robbery/assult',
  2: 'accident',
  3: 'fire',
};

const sendEmergencyMessage = async (ussdId, emergencyType, location) => {
  // find all contacts of user
  /* TODO: optimize query */
  const allContacts = await Contact.find({}).populate('user');
  const contacts = allContacts.filter((contact) => contact.user.ussdId === ussdId);
  console.log('TCL: sendEmergencyMessage -> contacts', contacts);

  // filter by emergency type

  // send a message to contacts

  // creatte and save message
  // return success.
};

export default sendEmergencyMessage;
