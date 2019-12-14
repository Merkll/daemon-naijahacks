import { config } from 'dotenv';
import Nexmo from 'nexmo';

config();


const nexmo = new Nexmo({
  apiKey: process.env.NEXMO_API_KEY,
  apiSecret: process.env.NEXMO_SECRET,
}, { debug: true });

// const text = 'A text message sent using the Nexmo SMS API';

let errorMessage;

const sendSms = (from, to, text) => new Promise((resolve, reject) => {
  nexmo.message.sendSms(from, to, text, (err, responseData) => {
    if (err) {
      console.log(err);
      errorMessage = err;
      reject(errorMessage);
    } else if (responseData.messages[0].status === '0') {
      console.log('Message sent successfully.');
      resolve({
        success: true,
        message: 'sent',
      });
    } else {
      errorMessage = `Message failed with error: ${responseData.messages[0]['error-text']}`;
      console.log(errorMessage);
      reject(errorMessage);
    }
  });
});

export default sendSms;
