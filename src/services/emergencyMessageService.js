import { config } from 'dotenv';
import sendSms from '../helpers/sendSms';
import Model from '../database/models';

const { Contact, Message, Location } = Model;

config();

const EMERGENCY_TYPES = {
  1: 'robbery/assult',
  2: 'accident',
  3: 'fire',
};

const composeMessage = (firstName, lastName, contactName, emergencyType, location) => `This is an EMERGENCY!
Hello ${contactName}, this is ${firstName} ${lastName}. I'm currently in danger and in need of your urgent assistance.
You got this text bcos I added you as an emergency contact.
Emergency-type: ${emergencyType}
Location: ${location}`;

const sendEmergencyMessage = async (ussdId, emergencyType, locationResponse) => {
  // find all contacts of user
  /* TODO: optimize query */
  const allContacts = await Contact.find({}).populate('user');
  const contacts = allContacts.filter((contact) => contact.user.ussdId === ussdId);
  const { user } = contacts[0];
  console.log('TCL: sendEmergencyMessage -> contacts', contacts);
  const location = await Location.findOne({ user: user._id });

  // filter by emergency type

  // send a message to contacts
  let message;

  contacts.forEach((contact) => {
    message = composeMessage(
      user.firstName,
      user.lastName,
      contact.name,
      EMERGENCY_TYPES[emergencyType],
      location[locationResponse] || locationResponse,
    );

    sendSms(user.phoneNumber, contact.phoneNumber, message);
    Message.create({
      content: message,
      category: EMERGENCY_TYPES[emergencyType],
      user: contact.user._id,
      contact: contact._id,
    });
  });

  // create and save message
  return 'success';
};

export default sendEmergencyMessage;
