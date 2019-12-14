import Model from '../database/models';

const generateUssdId = (phoneNumber) => `${phoneNumber.substring(phoneNumber.length - 4)}`;

const createUser = ({ firstName, lastName, phoneNumber }) => Model.User.create({
  firstName, lastName, phoneNumber, ussdId: generateUssdId(phoneNumber),
});


export default createUser;
