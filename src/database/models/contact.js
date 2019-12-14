import { Schema, model } from 'mongoose';

const contactSchema = new Schema({
  name: {
    type: String,
  },
  phoneNumber: {
    type: String,
    unique: true,
  },
  contactType: {
    type: String,
    default: 'faf',
  },
  user: {
    type: Schema.Types.ObjectId,
    ref: 'User',
  },
});

const Contact = model('Contact', contactSchema);

export default Contact;
