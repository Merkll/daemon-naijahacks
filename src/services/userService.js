import Model from '../database/models';

const generateUssdId = (firstName, lastName, phoneNumber) => `${firstName.substring(0, 1)}${lastName.substring(0, 1)}${phoneNumber.substring(phoneNumber.length - 4)}`;

const createUser = (firstName, lastName, phoneNumber) => Model.User.create({
  firstName, lastName, phoneNumber, ussdId: generateUssdId(firstName, lastName, phoneNumber),
});


export default createUser;
