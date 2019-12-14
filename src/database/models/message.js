import { Schema, model } from 'mongoose';

const messageSchema = new Schema({
  content: {
    type: String,
    required: true,
  },
  category: {
    type: String,
    required: true,
  },
  user: {
    type: Schema.Types.ObjectId,
    ref: 'User',
  },
  contact: {
    type: Schema.Types.ObjectId,
    ref: 'Contact',
  },
});

const Message = model('Message', messageSchema);

export default Message;
