import sendEmergencyMessage from '../services/emergencyMessageService';

const root = ('*', (req, res) => {
  res.send('Welcome to Watcher USSD');
});

const handleUssd = ('*', async (req, res) => {
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

  const userId = 'HS1489'; /* TODO: dynamically generate on register */

  let response;

  if (!text) {
    response = `CON ${defaultResponse}`;
  } else if (text === '1' || text === '2' || text === '3') {
    response = `CON ${locationResponse}`;
  } else if (text === '1*1') {
    /* set location of to home */
    await sendEmergencyMessage(userId, 1, 'home');
    response = `END ${messageSentResponse}`;
  } else if (text === '1*2') {
    await sendEmergencyMessage(userId, 1, 'work');
    response = `END ${messageSentResponse}`;
  } else if (text === '1*3' || text === '2*3' || text === '3*3') {
    response = `CON ${landMarkResponse}`;
  } else if (text.includes('1*3') && text.includes('LOC')) {
    const sections = text.split('*');
    const location = sections[sections.length - 1];
    await sendEmergencyMessage(userId, 1, location);
    response = `END ${messageSentResponse}`;
  } else if (text === '2*1') {
    /* set location of to home */
    await sendEmergencyMessage(userId, 2, 'home');
    response = `END ${messageSentResponse}`;
  } else if (text === '2*2') {
    /* set location of to home */
    await sendEmergencyMessage(userId, 2, 'work');
    response = `END ${messageSentResponse}`;
  } else if (text.includes('2*3') && text.includes('LOC')) {
    const sections = text.split('*');
    const location = sections[sections.length - 1];
    await sendEmergencyMessage(userId, 2, location);
    response = `END ${messageSentResponse}`;
  } else if (text === '3*1') {
    /* set location of to home */
    await sendEmergencyMessage(userId, 3, 'home');
    response = `END ${messageSentResponse}`;
  } else if (text === '3*2') {
    /* set location of to home */
    await sendEmergencyMessage(userId, 3, 'work');
    response = `END ${messageSentResponse}`;
  } else if (text.includes('3*3') && text.to().includes('LOC')) {
    const sections = text.split('*');
    const location = sections[sections.length - 1];
    await sendEmergencyMessage(userId, 3, location);
    response = `END ${messageSentResponse}`;
  } else {
    response = `CON INVALID INPUT.
    ${defaultResponse}`;
  }

  return res.status(STATUS).send(response);
});

export { root, handleUssd };
