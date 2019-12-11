
import { config } from 'dotenv';
import Nexmo from 'nexmo';
import Model from '../database/models';

config();


const nexmo = new Nexmo({
  apiKey: process.env.NEXMO_API_KEY,
  apiSecret: process.env.NEXMO_SECRET,
}, { debug: true });

const BRAND_NAME = 'Watcher';

const sendVerification = (reciepientNumber) => nexmo.verify.request({
  number: reciepientNumber,
  brand: BRAND_NAME,
  workflow_id: 6, // specifies sms
  pin_expiry: 3600,
}, async (err, result) => {
  if (err) {
    console.error(err);
  }
  const verificationId = result.request_id;

  try {
    await Model.User.findOneAndUpdate({
      phoneNumber: reciepientNumber,
    }, {
      $set: {
        verificationId,
      },
    }, { returnNewDocument: true });
  } catch (error) {
    console.error(error);
  }
});


export {
  nexmo,
  sendVerification,
};
