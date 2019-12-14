import sendEmergencyMessage from '../services/emergencyMessageService';

const root = ('*', (req, res) => {
  res.send('Welcome to Watcher USSD');
});

const handleUssd = ('*', (req, res) => {
  const { /* phoneNumber, */ text } = req.body;

  const defaultResponse = `What is your emergency?
  1. Robbery/Assault
  2. Accident
  3. Fire`;

  const locationResponse = `Enter location?
  1. Home
  2. Work
  3. Landmark`;

  const landMarkResponse = `Enter your location starting with "LOC". Example:
  LOC Obalende under bridge`;

  const messageSentResponse = 'Emergency message sent! Help is coming.';

  const STATUS = 200;

  const userId = 'HS1489';

  let response;

  if (!text) {
    response = `CON ${defaultResponse}`;
  } else if (text === '1') {
    response = `CON ${locationResponse}`;
  } else if (text === '1*1') {
    /* set location of to home */
    sendEmergencyMessage(userId, 1, 1);
    response = `END ${messageSentResponse}`;
  } else if (text === '1*2') {
    response = `END ${messageSentResponse}`;
  } else if (text === '1*3') {
    response = `CON ${landMarkResponse}`;
  } else if (text.includes('1*3') && text.includes('LOC')) {
    response = `END ${messageSentResponse}`;
    console.log(text);
  } else {
    response = `CON INVALID INPUT.
    ${defaultResponse}`;
  }

  return res.status(STATUS).send(response);
});

export { root, handleUssd };
